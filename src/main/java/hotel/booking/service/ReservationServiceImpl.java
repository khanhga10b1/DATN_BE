package hotel.booking.service;

import hotel.booking.domain.ReservationDomain;
import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.SendMailDomain;
import hotel.booking.domain.request.CheckValidRequest;
import hotel.booking.domain.request.ReservationRequest;
import hotel.booking.entity.GuestEntity;
import hotel.booking.entity.ReservationEntity;
import hotel.booking.entity.RoomEntity;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.GuestRepository;
import hotel.booking.repository.ReservationRepository;
import hotel.booking.repository.RoomRepository;
import hotel.booking.utils.DateTimeUtils;
import hotel.booking.utils.Error;
import hotel.booking.utils.SendMailUtils;
import hotel.booking.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static hotel.booking.utils.DateTimeUtils.getDateWithoutTimeUsingFormat;
import static hotel.booking.utils.DateTimeUtils.isBetween;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SendMailUtils sendMailUtils;
    @Autowired
    private GuestRepository guestRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public ResponseEntity<ResponseByName<String, Object>> checkValidReservation(CheckValidRequest checkValidRequest) {
        Long roomId = checkValidRequest.getRoomId();
        if (roomId == null) {
            logger.error(StringUtils.buildLog(Error.REQUIRED_FIELD, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.REQUIRED_FIELD.getMessage(), Error.REQUIRED_FIELD.getCode(), HttpStatus.BAD_REQUEST);
        }

        boolean result = checkValid(checkValidRequest.getCheckIn(), checkValidRequest.getCheckOut(), roomId);
        if (result) {
            return ResponseEntity.ok(ResponseByName.Builder("validate", "ok").build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseByName.Builder("validate", "ok").build());
    }

    @Override
    @Transactional
    public ReservationDomain editReservation(Long id, ReservationRequest request) throws ParseException {
        Date checkIn = DateTimeUtils.convertStringToDate(request.getCheckIn(), "dd/MM/yyyy");
        Date checkOut = DateTimeUtils.convertStringToDate(request.getCheckOut(), "dd/MM/yyyy");
        if (id == null && !checkValid(checkIn, checkOut, request.getRoomId())) {
            logger.error(StringUtils.buildLog(Error.BAD_REQUEST, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.BAD_REQUEST.getMessage(), Error.BAD_REQUEST.getCode(),
                    HttpStatus.BAD_REQUEST);
        } else {
            ReservationEntity reservationEntity;
            GuestEntity guestEntity;
            if (id != null) {
                reservationEntity = reservationRepository.findById(id).orElseThrow(() -> {
                    logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
                    return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                            HttpStatus.BAD_REQUEST);
                });
                guestEntity = reservationEntity.getGuestEntity();
            } else {
                reservationEntity = new ReservationEntity();
                guestEntity = new GuestEntity();

            }
            String oldStatus = reservationEntity.getStatus();
            guestEntity.setChildren(request.getChildren());
            guestEntity.setAdult(request.getAdult());
            guestEntity = guestRepository.save(guestEntity);
            reservationEntity.setGuestEntity(guestEntity);
            reservationEntity.setRoomId(request.getRoomId());
            reservationEntity.setCancelReason(request.getCancelReason());
            reservationEntity.setCustomerId(request.getCustomerId());
            reservationEntity.setEmail(request.getEmail());
            reservationEntity.setCode(request.getCode());
            reservationEntity.setName(request.getName());
            reservationEntity.setAddress(request.getAddress());
            reservationEntity.setNote(request.getNote());
            reservationEntity.setCheckIn(checkIn);
            reservationEntity.setCheckOut(checkOut);
            reservationEntity.setCost(request.getCost());
            reservationEntity.setStatus( id == null ? "confirmed" : request.getStatus());
            reservationEntity.setPhone(request.getPhone());
            reservationEntity = reservationRepository.save(reservationEntity);

            if(oldStatus !=null && !oldStatus.equals(request.getStatus())) {
                Map<String, Object> params = new HashMap<>();
                params.put("cancelReason", request.getCancelReason());
                params.put("status", request.getStatus());
                SendMailDomain sendMailDomain = new SendMailDomain();
                sendMailDomain.setText("Your reservation has been " + request.getStatus());
                sendMailDomain.setSubject("Your reservation has been " + request.getStatus());
                sendMailDomain.setToEmail(Collections.singletonList(request.getEmail()));
                sendMailDomain.setParams(params);
                sendMailDomain.setTemp("send-mail-cancel");
                jmsTemplate.convertAndSend("sendMail",sendMailDomain);
            }

            ReservationDomain reservationDomain = modelMapper.map(reservationEntity, ReservationDomain.class);
            reservationDomain.setAdult(reservationEntity.getGuestEntity().getAdult());
            reservationDomain.setChildren(reservationEntity.getGuestEntity().getChildren());
            RoomEntity roomEntity = roomRepository.getById(reservationEntity.getRoomId());
            reservationDomain.setHotelId(roomEntity.getHotelId());
            return reservationDomain;

        }
    }

    @Override
    public List<ReservationDomain> getListReservations(String code) {
        if (StringUtils.isEmpty(code)) {
            List<ReservationEntity> reservationEntities = reservationRepository.findAll();
            return reservationEntities.stream().map(reservationEntity -> {
                ReservationDomain reservationDomain = modelMapper.map(reservationEntity, ReservationDomain.class);
                reservationDomain.setAdult(reservationEntity.getGuestEntity().getAdult());
                reservationDomain.setChildren(reservationEntity.getGuestEntity().getChildren());
                RoomEntity roomEntity = roomRepository.getById(reservationEntity.getRoomId());
                reservationDomain.setHotelId(roomEntity.getHotelId());
                return reservationDomain;
            }).collect(Collectors.toList());
        } else {
            List<ReservationEntity> reservationEntities = reservationRepository.findByCode(code);
            return reservationEntities.stream().map(reservationEntity -> {
                ReservationDomain reservationDomain = modelMapper.map(reservationEntity, ReservationDomain.class);
                reservationDomain.setAdult(reservationEntity.getGuestEntity().getAdult());
                reservationDomain.setChildren(reservationEntity.getGuestEntity().getChildren());
                RoomEntity roomEntity = roomRepository.getById(reservationEntity.getRoomId());
                reservationDomain.setHotelId(roomEntity.getHotelId());
                return reservationDomain;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public List<ReservationDomain> getListFilter(Long hotelId, Long customerId, Date checkInFrom, Date checkInTo, Date checkOutFrom, Date checkOutTo, List<Long> roomIds, List<String> status) {
        List<ReservationEntity> reservationEntities = reservationRepository.findAll();

        return reservationEntities.stream().filter(r -> customerId == null || customerId.equals(r.getCustomerId()))
                .filter(r -> hotelId == null || hotelId.equals(r.getRoomEntity().getHotelId()))
                .filter(r -> CollectionUtils.isEmpty(status) || status.contains(r.getStatus()))
                .filter(r -> CollectionUtils.isEmpty(roomIds) || roomIds.contains(r.getRoomId()))
                .filter(r -> {
                    try {
                        return checkInFrom == null || getDateWithoutTimeUsingFormat(r.getCheckIn()).getTime() >= getDateWithoutTimeUsingFormat(checkInFrom).getTime();
                    } catch (ParseException e) {
                        throw new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                                HttpStatus.BAD_REQUEST);
                    }
                }).filter(r -> {
                    try {
                        return checkInTo == null || getDateWithoutTimeUsingFormat(r.getCheckIn()).getTime() <= getDateWithoutTimeUsingFormat(checkInTo).getTime();
                    } catch (ParseException e) {
                        throw new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                                HttpStatus.BAD_REQUEST);
                    }
                }).filter(r -> {
                    try {
                        return checkOutFrom == null || getDateWithoutTimeUsingFormat(r.getCheckOut()).getTime() >= getDateWithoutTimeUsingFormat(checkOutFrom).getTime();
                    } catch (ParseException e) {
                        throw new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                                HttpStatus.BAD_REQUEST);
                    }
                }).filter(r -> {
                    try {
                        return checkOutTo == null || getDateWithoutTimeUsingFormat(r.getCheckOut()).getTime() <= getDateWithoutTimeUsingFormat(checkOutTo).getTime();
                    } catch (ParseException e) {
                        throw new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                                HttpStatus.BAD_REQUEST);
                    }
                }).map(reservationEntity -> {
                    ReservationDomain reservationDomain = modelMapper.map(reservationEntity, ReservationDomain.class);
                    reservationDomain.setAdult(reservationEntity.getGuestEntity().getAdult());
                    reservationDomain.setChildren(reservationEntity.getGuestEntity().getChildren());
                    RoomEntity roomEntity = roomRepository.getById(reservationEntity.getRoomId());
                    reservationDomain.setHotelId(roomEntity.getHotelId());
                    return reservationDomain;
                }).collect(Collectors.toList());
    }

    @Override
    public void sendMail(ReservationRequest reservationRequest) {
        RoomEntity room = roomRepository.getById(reservationRequest.getRoomId());
        SendMailDomain sendMailDomain = new SendMailDomain();
        sendMailDomain.setToEmail(Collections.singletonList(reservationRequest.getEmail()));
        sendMailDomain.setSubject("Your reservation has been send to hotel manager");
        sendMailDomain.setText("Thanks for using our hotel reservation system.The manager will confirm your reservation soon.");
        Map<String, Object> params = new HashMap<>();
        params.put("roomImage", room.getRoomImageEntities().get(0).getImageLink());
        params.put("hotelName", room.getHotelEntity().getName());
        params.put("hotelCity", room.getHotelEntity().getCity());
        params.put("code", reservationRequest.getCode());
        params.put("hotelAddress", room.getHotelEntity().getAddress());
        params.put("roomName", room.getName());
        params.put("checkIn", reservationRequest.getCheckIn());
        params.put("checkOut", reservationRequest.getCheckOut());
        params.put("cost", reservationRequest.getCost());
        params.put("adult", reservationRequest.getAdult());
        params.put("children", reservationRequest.getChildren());
        params.put("name", reservationRequest.getName());
        params.put("email", reservationRequest.getEmail());
        params.put("phone", reservationRequest.getPhone());
        params.put("address", reservationRequest.getAddress());
        params.put("note", reservationRequest.getNote());
        params.put("diffDays", reservationRequest.getDiffDays());
        params.put("hotelId", room.getHotelId());
        sendMailDomain.setTemp("send-mail-reservation");
        sendMailDomain.setParams(params);
        sendMailUtils.sendMailWithTemplate(sendMailDomain);
        sendMailDomain.setSubject("RATING THE HOTEL SERVICES");
        sendMailDomain.setText("Please rate the hotel services");
        sendMailDomain.setTemp("send-mail-rating");
        sendMailUtils.sendMailWithTemplate(sendMailDomain);


    }


    public boolean checkValid(Date start, Date end, Long roomId) {
        boolean result = true;
        try {
            Long checkIn = getDateWithoutTimeUsingFormat(start).getTime();
            Long checkOut = getDateWithoutTimeUsingFormat(end).getTime();
            List<ReservationEntity> reservationEntities = reservationRepository.findByRoomIdAndStatus(roomId, "canceled");
            for (ReservationEntity r : reservationEntities) {
                Long in = getDateWithoutTimeUsingFormat(r.getCheckIn()).getTime();
                Long out = getDateWithoutTimeUsingFormat(r.getCheckOut()).getTime();
                if (isBetween(checkIn, in, out) || isBetween(checkOut, in, out) || (isBetween(in, checkIn, checkOut) && isBetween(out, checkIn, checkOut))
                ) {
                    result = false;
                }
            }
        } catch (ParseException e) {
            return false;
        }

        return result;
    }
}

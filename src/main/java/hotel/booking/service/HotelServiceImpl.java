package hotel.booking.service;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.RoomDomain;
import hotel.booking.domain.UserDomain;
import hotel.booking.domain.request.HotelRequest;
import hotel.booking.entity.*;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.*;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private HotelImageRepository hotelImageRepository;
    @Autowired
    private HotelAmenitiesRepository hotelAmenitiesRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoomImageRepository roomImageRepository;
    @Autowired
    private RoomAmenitiesRepository roomAmenitiesRepository;
    @Autowired
    private RuleRepository ruleRepository;


    @Override
    @Transactional
    public HotelDomain editHotel(Long id, HotelRequest hotelRequest) {
        HotelEntity hotelEntity;
        if (id != null) {
            hotelEntity = hotelRepository.findById(id).orElseThrow(() -> {
                logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
                return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                        HttpStatus.BAD_REQUEST);
            });
        } else {
            hotelEntity = new HotelEntity();
        }

        hotelEntity.setAccountId(hotelRequest.getAccountId());
        hotelEntity.setAddress(hotelRequest.getAddress());
        hotelEntity.setCity(hotelRequest.getCity());
        hotelEntity.setDescription(hotelRequest.getDescription());
        hotelEntity.setEmail(hotelRequest.getEmail());
        hotelEntity.setName(hotelRequest.getName());
        hotelEntity.setPhone(hotelRequest.getPhone());
        hotelEntity = hotelRepository.save(hotelEntity);
        final Long hotelId = hotelEntity.getId();
        HotelDomain hotelDomain = convertEntityToDomain(hotelEntity);
        List<HotelImageEntity> imageEntities = hotelImageRepository.findByHotelId(hotelId);
        hotelImageRepository.deleteAll(imageEntities);
        List<HotelAmenitiesEntity> amenitiesEntities = hotelAmenitiesRepository.findByHotelId(hotelId);
        hotelAmenitiesRepository.deleteAll(amenitiesEntities);
        if (!CollectionUtils.isEmpty(hotelRequest.getImage())) {
            imageEntities = hotelRequest.getImage().stream().map(image -> new HotelImageEntity(image, hotelId)).collect(Collectors.toList());
            imageEntities = hotelImageRepository.saveAll(imageEntities);
            hotelDomain.setImage(imageEntities.stream().map(HotelImageEntity::getImageLink).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(hotelRequest.getAmenities())) {
            amenitiesEntities = hotelRequest.getAmenities().stream().map(a -> new HotelAmenitiesEntity(a, hotelId)).collect(Collectors.toList());
            amenitiesEntities = hotelAmenitiesRepository.saveAll(amenitiesEntities);
            hotelDomain.setAmenities(amenitiesEntities.stream().map(HotelAmenitiesEntity::getName).collect(Collectors.toList()));
        }
        return hotelDomain;
    }

    @Override
    public List<HotelDomain> getListHotels() {
        return null;
    }

    @Override
    public HotelDomain getHotelById(Long id) {
        HotelEntity hotelEntity = hotelRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        HotelDomain hotelDomain = convertEntityToDomain(hotelEntity);
        List<String> image = hotelImageRepository.findByHotelId(hotelEntity.getId()).stream().map(HotelImageEntity::getImageLink).collect(Collectors.toList());
        hotelDomain.setImage(image);
        List<String> amenities = hotelAmenitiesRepository.findByHotelId(hotelEntity.getId()).stream().map(HotelAmenitiesEntity::getName).collect(Collectors.toList());
        hotelDomain.setAmenities(amenities);
        List<RoomEntity> roomEntities = roomRepository.getAllByHotelId(hotelEntity.getId());
        hotelDomain.setRooms(roomEntities.size());
        return hotelDomain;
    }

    @Override
    public Map<Long, List<RoomDomain>> getAvailableList(Date checkIn, Date checkOut) {
        List<RoomEntity> roomEntities = roomRepository.findAll();
        List<RoomEntity> roomValid = roomEntities.stream().filter(room -> checkValid(checkIn, checkOut, room.getId())).collect(Collectors.toList());
        return roomValid.stream().map(room -> {
            RoomDomain roomDomain = modelMapper.map(room, RoomDomain.class);
            List<String> amenities = roomAmenitiesRepository.findByRoomId(room.getId()).stream().map(RoomAmenitiesEntity::getName).collect(Collectors.toList());
            roomDomain.setAmenities(amenities);
            List<String> image = roomImageRepository.findByRoomId(room.getId()).stream().map(RoomImageEntity::getImageLink).collect(Collectors.toList());
            roomDomain.setImage(image);
            List<String> rules = ruleRepository.findByRoomId(room.getId()).stream().map(RuleEntity::getName).collect(Collectors.toList());
            roomDomain.setRules(rules);
            return roomDomain;
        }).collect(Collectors.groupingBy(RoomDomain::getHotelId));
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

    public boolean isBetween(Long date, Long min, Long max) {
        return date >= min && date <= max;
    }

    public static Date getDateWithoutTimeUsingFormat(Date date)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
    }

    @Override
    public Map<String, Object> getHotels(Long userId, Integer offset, Integer limit, String city, String search) {
        offset = (offset != null && offset >= 0) ? offset : 0;
        limit = (limit != null && limit > 0) ? limit : Integer.MAX_VALUE;
        city = city == null ? "" : city;
        search =StringUtils.replaceSpecialCharacter(search).trim().toLowerCase();
        Pageable pageable = PageRequest.of(offset, limit);
        Page<HotelEntity> hotelEntities;
        if (userId != null) {
            hotelEntities = hotelRepository.getAllByAccountId(userId, pageable);
        } else {
            hotelEntities = hotelRepository.getHotelEntities(city,search,pageable);
        }

        List<HotelDomain> hotelDomains = hotelEntities.stream().map(hotelEntity -> {
            HotelDomain hotelDomain = convertEntityToDomain(hotelEntity);
            List<String> image = hotelImageRepository.findByHotelId(hotelEntity.getId()).stream().map(HotelImageEntity::getImageLink).collect(Collectors.toList());
            hotelDomain.setImage(image);
            List<String> amenities = hotelAmenitiesRepository.findByHotelId(hotelEntity.getId()).stream().map(HotelAmenitiesEntity::getName).collect(Collectors.toList());
            hotelDomain.setAmenities(amenities);
            List<RoomEntity> roomEntities = roomRepository.getAllByHotelId(hotelEntity.getId());
            hotelDomain.setRooms(roomEntities.size());
            return hotelDomain;
        }).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("hotels", hotelDomains);
        response.put("count", hotelEntities.getTotalElements());
        return response;
    }

    @Override
    @Transactional
    public void deleteHotel(Long hotelId) {
        HotelEntity hotelEntity = hotelRepository.findById(hotelId).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        hotelRepository.delete(hotelEntity);

    }

    private HotelDomain convertEntityToDomain(HotelEntity hotelEntity) {
        HotelDomain hotelDomain = new HotelDomain();
        hotelDomain.setId(hotelEntity.getId());
        hotelDomain.setAccountId(hotelEntity.getAccountId());
        hotelDomain.setName(hotelEntity.getName());
        hotelDomain.setAddress(hotelEntity.getAddress());
        hotelDomain.setCity(hotelEntity.getCity());
        hotelDomain.setEmail(hotelEntity.getEmail());
        hotelDomain.setDescription(hotelEntity.getDescription());
        hotelDomain.setPhone(hotelEntity.getPhone());
        hotelDomain.setRate(hotelEntity.getRate());
        return hotelDomain;
    }
}

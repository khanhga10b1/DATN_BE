package hotel.booking.service;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.UserDomain;
import hotel.booking.domain.request.HotelRequest;
import hotel.booking.entity.HotelAmenitiesEntity;
import hotel.booking.entity.HotelEntity;
import hotel.booking.entity.HotelImageEntity;
import hotel.booking.entity.RoomEntity;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.HotelAmenitiesRepository;
import hotel.booking.repository.HotelImageRepository;
import hotel.booking.repository.HotelRepository;
import hotel.booking.repository.RoomRepository;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
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


    @Override
    @Transactional
    public HotelDomain createHotel(HotelRequest hotelRequest) {

        HotelEntity hotelEntity = new HotelEntity();
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
        if (!CollectionUtils.isEmpty(hotelRequest.getImage())) {
            List<HotelImageEntity> imageEntities = hotelRequest.getImage().stream().map(image -> new HotelImageEntity(image, hotelId)).collect(Collectors.toList());
            imageEntities = hotelImageRepository.saveAll(imageEntities);
            hotelDomain.setImage(imageEntities.stream().map(HotelImageEntity::getImageLink).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(hotelRequest.getAmenities())) {
            List<HotelAmenitiesEntity> amenitiesEntities = hotelRequest.getAmenities().stream().map(a -> new HotelAmenitiesEntity(a, hotelId)).collect(Collectors.toList());
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
    public List<HotelDomain> getAvailableList(Date checkIn, Date checkOut) {
        List<RoomEntity> roomEntities = roomRepository.findAll();
        return null;
    }

    @Override
    public List<HotelDomain> getHotels(Long userId) {
        List<HotelEntity> hotelEntities;
        if (userId != null) {
            hotelEntities = hotelRepository.getAllByAccountId(userId);
        } else {
            hotelEntities = hotelRepository.findAll();
        }
        return hotelEntities.stream().map(hotelEntity -> {
            HotelDomain hotelDomain = convertEntityToDomain(hotelEntity);
            List<String> image = hotelImageRepository.findByHotelId(hotelEntity.getId()).stream().map(HotelImageEntity::getImageLink).collect(Collectors.toList());
            hotelDomain.setImage(image);
            List<String> amenities = hotelAmenitiesRepository.findByHotelId(hotelEntity.getId()).stream().map(HotelAmenitiesEntity::getName).collect(Collectors.toList());
            hotelDomain.setAmenities(amenities);
            List<RoomEntity> roomEntities = roomRepository.getAllByHotelId(hotelEntity.getId());
            hotelDomain.setRooms(roomEntities.size());
            return hotelDomain;
        }).collect(Collectors.toList());
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

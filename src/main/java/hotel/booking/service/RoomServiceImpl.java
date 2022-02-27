package hotel.booking.service;

import hotel.booking.domain.RoomDomain;
import hotel.booking.domain.request.RoomRequest;
import hotel.booking.entity.RoomAmenitiesEntity;
import hotel.booking.entity.RoomEntity;
import hotel.booking.entity.RoomImageEntity;
import hotel.booking.entity.RuleEntity;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.RoomAmenitiesRepository;
import hotel.booking.repository.RoomImageRepository;
import hotel.booking.repository.RoomRepository;
import hotel.booking.repository.RuleRepository;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomAmenitiesRepository roomAmenitiesRepository;
    @Autowired
    private RoomImageRepository roomImageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public Map<String, Object> getListRooms(Long hotelId, Integer offset, Integer limit, String typeSort, String columnSort, String search) {
        offset = (offset != null && offset >= 0) ? offset : 0;
        limit = (limit != null && limit > 0) ? limit : Integer.MAX_VALUE;
        typeSort = StringUtils.isEmpty(typeSort) || (!"ASC".equals(typeSort) && !"DESC".equals(typeSort)) ? "ASC" : typeSort;
        search = StringUtils.replaceSpecialCharacter(search).trim().toLowerCase();

        Pageable pageable;
        if (StringUtils.isEmpty(columnSort)) {
            pageable = PageRequest.of(offset, limit);
        } else {
            pageable = PageRequest.of(offset, limit, "DESC".equals(typeSort) ? Sort.by(columnSort).descending() : Sort.by(columnSort).ascending());
        }
        Page<RoomEntity> roomEntities;
        if (hotelId == null) {
            roomEntities = roomRepository.findRoomEntities(search, pageable);
        } else {
            roomEntities = roomRepository.findRoomEntitiesByHotelId(search, hotelId, pageable);
        }

        List<RoomDomain> roomDomains = roomEntities.stream().map(room -> {
            RoomDomain roomDomain = modelMapper.map(room, RoomDomain.class);
            List<String> amenities = roomAmenitiesRepository.findByRoomId(room.getId()).stream().map(RoomAmenitiesEntity::getName).collect(Collectors.toList());
            roomDomain.setAmenities(amenities);
            List<String> image = roomImageRepository.findByRoomId(room.getId()).stream().map(RoomImageEntity::getImageLink).collect(Collectors.toList());
            roomDomain.setImage(image);
            List<String> rules = ruleRepository.findByRoomId(room.getId()).stream().map(RuleEntity::getName).collect(Collectors.toList());
            roomDomain.setRules(rules);
            return roomDomain;
        }).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("rooms", roomDomains);
        result.put("count", roomEntities.getTotalElements());
        return result;
    }

    @Override
    public RoomDomain getRoomById(Long roomId) {
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });;

        RoomDomain roomDomain = modelMapper.map(roomEntity, RoomDomain.class);
        List<String> amenities = roomAmenitiesRepository.findByRoomId(roomEntity.getId()).stream().map(RoomAmenitiesEntity::getName).collect(Collectors.toList());
        roomDomain.setAmenities(amenities);
        List<String> image = roomImageRepository.findByRoomId(roomEntity.getId()).stream().map(RoomImageEntity::getImageLink).collect(Collectors.toList());
        roomDomain.setImage(image);
        List<String> rules = ruleRepository.findByRoomId(roomEntity.getId()).stream().map(RuleEntity::getName).collect(Collectors.toList());
        roomDomain.setRules(rules);
        return roomDomain;
    }

    @Override
    @Transactional
    public RoomDomain editRoom(Long id, RoomRequest roomRequest) {
        RoomEntity roomEntity;
        if(id != null) {
            roomEntity = roomRepository.findById(id).orElseThrow(() -> {
                logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
                return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                        HttpStatus.BAD_REQUEST);
            });
        } else  {
            roomEntity = new RoomEntity();
        }
        roomEntity.setHotelId(roomRequest.getHotelId());
        roomEntity.setName(roomRequest.getName());
        roomEntity.setPrepay(roomRequest.getPrepay());
        roomEntity.setStatus(roomRequest.getStatus());
        roomEntity.setDescription(roomRequest.getDescription());
        roomEntity.setPrice(roomRequest.getPrice());
        roomEntity.setArea(roomRequest.getArea());
        roomEntity  = roomRepository.save(roomEntity);
        RoomDomain  roomDomain = modelMapper.map(roomEntity, RoomDomain.class);
        List<RoomImageEntity> roomImageEntities = roomEntity.getRoomImageEntities();
        List<RuleEntity> ruleEntities = roomEntity.getRuleEntities();
        List<RoomAmenitiesEntity> roomAmenitiesEntities = roomEntity.getRoomAmenitiesEntities();
        ruleEntities.clear();
        roomAmenitiesEntities.clear();
        roomImageEntities.clear();
        final Long roomId = roomEntity.getId();

        if(!CollectionUtils.isEmpty(roomRequest.getImage())) {
            roomImageEntities.addAll(roomRequest.getImage().stream().map(room -> new RoomImageEntity(roomId, room)).collect(Collectors.toList()));
            roomDomain.setImage(roomImageEntities.stream().map(RoomImageEntity::getImageLink).collect(Collectors.toList()));
        }

        if(!CollectionUtils.isEmpty(roomRequest.getAmenities())) {
            roomAmenitiesEntities.addAll(roomRequest.getAmenities().stream().map(room -> new RoomAmenitiesEntity(roomId, room)).collect(Collectors.toList()));
            roomDomain.setAmenities(roomAmenitiesEntities.stream().map(RoomAmenitiesEntity::getName).collect(Collectors.toList()));
        }

        if(!CollectionUtils.isEmpty(roomRequest.getRules())) {
            ruleEntities.addAll(roomRequest.getRules().stream().map(room -> new RuleEntity(roomId, room)).collect(Collectors.toList()));
            roomDomain.setRules(ruleEntities.stream().map(RuleEntity::getName).collect(Collectors.toList()));
        }
        roomRepository.save(roomEntity);
        return roomDomain;
    }

    @Override
    public void deleteRoom(Long id) {
        RoomEntity roomEntity = roomRepository.findById(id).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        roomRepository.delete(roomEntity);
    }
}

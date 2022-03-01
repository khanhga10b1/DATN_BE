package hotel.booking.service;

import hotel.booking.domain.RatingDomain;
import hotel.booking.domain.request.RatingRequest;
import hotel.booking.entity.HotelEntity;
import hotel.booking.entity.RatingEntity;
import hotel.booking.exception.CustomException;
import hotel.booking.repository.HotelRepository;
import hotel.booking.repository.RatingRepository;
import hotel.booking.utils.Error;
import hotel.booking.utils.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<RatingDomain> getListRatings(Long hotelId, String code) {
        List<RatingEntity> ratingEntities;
        if (hotelId == null && StringUtils.isEmpty(code)) {
            ratingEntities = ratingRepository.findAll();
        } else if (hotelId != null && !StringUtils.isEmpty(code)) {
            ratingEntities = ratingRepository.findByHotelIdAndCode(hotelId, code);
        } else if (hotelId != null) {
            ratingEntities = ratingRepository.findByHotelId(hotelId);
        } else {
            ratingEntities = ratingRepository.findByCode(code);
        }

        return ratingEntities.stream().map(ratingEntity -> modelMapper.map(ratingEntity, RatingDomain.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RatingDomain createRating(RatingRequest request) {
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setHotelId(request.getHotelId());
        ratingEntity.setRate(request.getRate());
        ratingEntity.setCode(request.getCode());
        ratingEntity.setName(request.getName());
        ratingEntity.setText(request.getText());
        ratingEntity = ratingRepository.save(ratingEntity);
        List<RatingEntity> ratingEntities = ratingRepository.findByHotelId(request.getHotelId());
        double count = ratingEntities.stream().mapToDouble(RatingEntity::getRate).sum();
        HotelEntity hotelEntity = hotelRepository.findById(ratingEntity.getHotelId()).orElseThrow(() -> {
            logger.error(StringUtils.buildLog(Error.DATA_NOT_FOUND, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            return new CustomException(Error.DATA_NOT_FOUND.getMessage(), Error.DATA_NOT_FOUND.getCode(),
                    HttpStatus.BAD_REQUEST);
        });
        hotelEntity.setRate(count/ratingEntities.size());
        hotelRepository.save(hotelEntity);
        return modelMapper.map(ratingEntity, RatingDomain.class);
    }
}

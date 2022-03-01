package hotel.booking.service;

import hotel.booking.domain.RatingDomain;
import hotel.booking.domain.request.RatingRequest;

import java.util.List;

public interface RatingService {

    List<RatingDomain> getListRatings(Long hotelId, String code);
    RatingDomain createRating(RatingRequest request);
}

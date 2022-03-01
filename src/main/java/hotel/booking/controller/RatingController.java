package hotel.booking.controller;


import hotel.booking.domain.RatingDomain;
import hotel.booking.domain.request.RatingRequest;
import hotel.booking.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rating")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<RatingDomain>> getListRatings(@RequestParam(value = "code", required = false) String code,
                                                            @RequestParam(value = "hotelId", required = false) Long hotelId) {
        return ResponseEntity.ok(ratingService.getListRatings(hotelId, code));
    }

    @PostMapping
    public ResponseEntity<RatingDomain> createRating(@RequestBody RatingRequest request) {
        return ResponseEntity.ok(ratingService.createRating(request));
    }
}

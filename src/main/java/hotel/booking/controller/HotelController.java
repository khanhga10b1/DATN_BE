package hotel.booking.controller;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.request.HotelRequest;
import hotel.booking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDomain> createHotel(@RequestBody HotelRequest hotelRequest) {
        return ResponseEntity.ok(hotelService.createHotel(hotelRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDomain> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }
}

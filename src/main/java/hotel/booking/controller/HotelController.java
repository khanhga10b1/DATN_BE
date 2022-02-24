package hotel.booking.controller;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.request.HotelRequest;
import hotel.booking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDomain> createHotel(@RequestBody HotelRequest hotelRequest) {
        return ResponseEntity.ok(hotelService.createHotel(hotelRequest));
    }

    @GetMapping
    public ResponseEntity<List<HotelDomain>> getListHotels(@RequestParam(value = "accountId", required = false) Long userId) {
        return ResponseEntity.ok(hotelService.getHotels(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDomain> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<HotelDomain>> getAvailableList(@RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkIn, @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkOut ) {
        hotelService.getAvailableList(checkIn, checkOut);
        return null;
    }
}

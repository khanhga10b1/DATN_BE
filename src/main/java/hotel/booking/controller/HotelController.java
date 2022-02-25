package hotel.booking.controller;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.RoomDomain;
import hotel.booking.domain.request.HotelRequest;
import hotel.booking.entity.RoomEntity;
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
        return ResponseEntity.ok(hotelService.editHotel(null, hotelRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelDomain> editHotel(@RequestBody HotelRequest hotelRequest, @PathVariable("id") Long id) {
        return ResponseEntity.ok(hotelService.editHotel(id, hotelRequest));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getListHotels(@RequestParam(value = "accountId", required = false) Long userId,
                                                             @RequestParam(value = "offset", required = false) Integer offset,
                                                             @RequestParam(value = "limit", required = false) Integer limit,
                                                             @RequestParam(value = "city", required = false) String city,
                                                             @RequestParam(value = "name",required = false) String search) {
        return ResponseEntity.ok(hotelService.getHotels(userId, offset, limit, city, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDomain> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<Map<Long, List<RoomDomain>>> getAvailableList(@RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkIn, @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkOut) {
        return ResponseEntity.ok(hotelService.getAvailableList(checkIn, checkOut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseByName<String, Object>> deleteHotel(@PathVariable("id") Long hotelId) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.ok(ResponseByName.Builder("id", hotelId).build());
    }
}

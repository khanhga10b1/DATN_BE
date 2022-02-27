package hotel.booking.controller;


import hotel.booking.domain.ReservationDomain;
import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.request.CheckValidRequest;
import hotel.booking.domain.request.ReservationRequest;
import hotel.booking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/checkdate")
    public ResponseEntity<ResponseByName<String, Object>> check(@RequestBody CheckValidRequest checkValidRequest) {
        return reservationService.checkValidReservation(checkValidRequest);
    }

    @PostMapping
    public ResponseEntity<ReservationDomain> createReservation(@RequestBody ReservationRequest reservationRequest) throws ParseException {
        return ResponseEntity.ok(reservationService.editReservation(null, reservationRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDomain> createReservation(@RequestBody ReservationRequest reservationRequest, @PathVariable("id") Long id) throws ParseException {
        return ResponseEntity.ok(reservationService.editReservation(id, reservationRequest));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDomain>> getListReservations(@RequestParam(value = "code", required = false) String code) {
        return ResponseEntity.ok(reservationService.getListReservations(code));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReservationDomain>> getListReservationsByFilter(@RequestParam(value = "customerId", required = false) Long customerId,
                                                                               @RequestParam(value = "hotelId", required = false) Long hotelId,
                                                                               @RequestParam(value = "checkInFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkInFrom,
                                                                               @RequestParam(value = "checkInTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkInTo,
                                                                               @RequestParam(value = "checkOutFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkOutFrom,
                                                                               @RequestParam(value = "checkOutTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkOutTo,
                                                                               @RequestParam(value = "roomIds", required = false) List<Long> roomIds,
                                                                               @RequestParam(value = "status", required = false) List<String> status) {
        return ResponseEntity.ok(reservationService.getListFilter(hotelId, customerId, checkInFrom, checkInTo, checkOutFrom, checkOutTo, roomIds, status));
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendMail(@RequestBody ReservationRequest reservationRequest) {
        reservationService.sendMail(reservationRequest);
        return ResponseEntity.ok().build();
    }
}

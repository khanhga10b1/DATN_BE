package hotel.booking.service;

import hotel.booking.domain.ReservationDomain;
import hotel.booking.domain.ResponseByName;
import hotel.booking.domain.request.CheckValidRequest;
import hotel.booking.domain.request.ReservationRequest;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface ReservationService {

    ResponseEntity<ResponseByName<String, Object>> checkValidReservation(CheckValidRequest checkValidRequest);

    ReservationDomain editReservation(Long id, ReservationRequest reservationRequest) throws ParseException;

    List<ReservationDomain> getListReservations(String code);

    List<ReservationDomain> getListFilter(Long hotelId, Long customerId, Date checkInFrom, Date checkInTo, Date checkOutFrom, Date checkOutTo, List<Long> roomIds, List<String> status);

    void sendMail(ReservationRequest reservationRequest);
}

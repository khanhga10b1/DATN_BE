package hotel.booking.service;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.request.HotelRequest;

import java.util.Date;
import java.util.List;

public interface HotelService {

    HotelDomain createHotel(HotelRequest hotelRequest);
    List<HotelDomain> getListHotels();
    HotelDomain getHotelById(Long id);
    List<HotelDomain> getAvailableList(Date checkIn, Date checkOut);
    List<HotelDomain> getHotels( Long userId);
}

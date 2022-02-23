package hotel.booking.service;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.request.HotelRequest;

import java.util.List;

public interface HotelService {

    HotelDomain createHotel(HotelRequest hotelRequest);
    List<HotelDomain> getListHotel();
    HotelDomain getHotelById(Long id);
}

package hotel.booking.service;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.RoomDomain;
import hotel.booking.domain.request.HotelRequest;
import hotel.booking.entity.RoomEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HotelService {

    HotelDomain editHotel(Long hotelId, HotelRequest hotelRequest);

    List<HotelDomain> getListHotels();

    HotelDomain getHotelById(Long id);

    Map<Long, List<RoomDomain>> getAvailableList(Date checkIn, Date checkOut);

    Map<String, Object> getHotels(Long userId, Integer offset, Integer limit, String city, String search);

    void deleteHotel(Long hotelId);
}

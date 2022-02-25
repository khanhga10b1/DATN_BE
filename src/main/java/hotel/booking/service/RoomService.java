package hotel.booking.service;

import hotel.booking.domain.RoomDomain;
import hotel.booking.domain.request.RoomRequest;

import java.util.Map;

public interface RoomService {
    Map<String, Object> getListRooms(Long hotelId, Integer offset, Integer limit, String typeSort, String columnSort, String search);
    RoomDomain getRoomById(Long roomId);
    RoomDomain editRoom(Long id, RoomRequest roomRequest);
    void deleteRoom(Long id);
}

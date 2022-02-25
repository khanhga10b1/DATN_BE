package hotel.booking.repository;

import hotel.booking.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    List<RoomEntity> getAllByHotelId(Long hotelId);

    @Query("select r from RoomEntity r where lower(r.name) like %?1%")
    Page<RoomEntity> findRoomEntities(String search, Pageable pageable);

    @Query("select r from RoomEntity r where r.hotelId = ?2 and lower(r.name) like %?1%")
    Page<RoomEntity> findRoomEntitiesByHotelId(String search, Long hotelId, Pageable pageable);

}

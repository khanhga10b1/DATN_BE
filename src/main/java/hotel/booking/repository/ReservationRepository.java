package hotel.booking.repository;

import hotel.booking.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByRoomId(Long roomId);

    @Query("select r from ReservationEntity r where r.roomId = ?1 and nullif('',r.status) <> ?2")
    List<ReservationEntity> findByRoomIdAndStatus(Long roomId, String status);

    List<ReservationEntity> findByCode(String code);

    @Query("select rv from ReservationEntity rv inner join RoomEntity r on rv.roomId = r.id and r.hotelId = ?1")
    List<ReservationEntity> findByHotelId(Long hotelId);

    @Query("select count (rv) from ReservationEntity rv inner join RoomEntity r on rv.roomId = ?2 and rv.roomId = r.id and r.hotelId = ?1")
    long countByHotelIdAndRoomId(Long hotelId, Long roomId);

    @Query("select count(rv) from ReservationEntity rv inner join RoomEntity r on rv.roomId = r.id and r.hotelId = ?1 where rv.createdDate >= ?2 and rv.createdDate < ?3")
    long countByHotelIAndCreatedDate(Long hotelId, Date min, Date max);
}

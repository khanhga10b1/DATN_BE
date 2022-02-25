package hotel.booking.repository;

import hotel.booking.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository  extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByRoomId(Long roomId);

    @Query("select r from ReservationEntity r where r.roomId = ?1 and r.status <> ?2")
    List<ReservationEntity> findByRoomIdAndStatus(Long roomId, String status);
}

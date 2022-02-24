package hotel.booking.repository;

import hotel.booking.entity.RoomImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Long> {
    List<RoomImageEntity> findByRoomId(Long roomId);
}

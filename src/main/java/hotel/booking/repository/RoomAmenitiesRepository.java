package hotel.booking.repository;

import hotel.booking.entity.RoomAmenitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomAmenitiesRepository extends JpaRepository<RoomAmenitiesEntity, Long> {

    List<RoomAmenitiesEntity> findByRoomId(Long roomId);
}

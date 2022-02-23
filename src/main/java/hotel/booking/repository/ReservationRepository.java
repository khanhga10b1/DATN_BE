package hotel.booking.repository;

import hotel.booking.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository  extends JpaRepository<ReservationEntity, Long> {
}

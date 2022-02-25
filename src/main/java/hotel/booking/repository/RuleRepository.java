package hotel.booking.repository;
import hotel.booking.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<RuleEntity, Long> {

    List<RuleEntity> findByRoomId(Long roomId);
}

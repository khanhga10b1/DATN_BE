package hotel.booking.repository;
import hotel.booking.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByCode(String code);
}

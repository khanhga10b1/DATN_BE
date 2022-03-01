package hotel.booking.repository;

import hotel.booking.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query("select u from UserEntity u where u.roleEntity.code in(?1)")
    List<UserEntity> findAllByRole(List<String> codes);

    @Query("select count(u) from UserEntity u where u.roleEntity.code in(?1)")
    long countByRole(List<String> codes);
}

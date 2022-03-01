package hotel.booking.repository;

import hotel.booking.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    List<RatingEntity> findByCode(String code);

    List<RatingEntity> findByHotelId(Long hotelId);

    List<RatingEntity> findByHotelIdAndCode(Long hotelId, String code);
}

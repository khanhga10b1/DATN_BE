package hotel.booking.repository;

import hotel.booking.entity.HotelAmenitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelAmenitiesRepository extends JpaRepository<HotelAmenitiesEntity, Long> {

    List<HotelAmenitiesEntity> findByHotelId(Long hotelId);
}

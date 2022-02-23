package hotel.booking.repository;

import hotel.booking.entity.HotelAmenitiesEntity;
import hotel.booking.entity.HotelImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelImageRepository extends JpaRepository<HotelImageEntity, Long> {
    List<HotelImageEntity> findByHotelId(Long hotelId);
}

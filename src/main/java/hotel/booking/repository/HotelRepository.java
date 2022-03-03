package hotel.booking.repository;

import hotel.booking.domain.CountHotelDomain;
import hotel.booking.entity.HotelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {
    Page<HotelEntity> getAllByAccountId(Long userId, Pageable pageable);

    @Query("select h from HotelEntity h where h.city like %?1% and h.name like %?2%")
    Page<HotelEntity> getHotelEntities(String city, String search, Pageable pageable);

    @Query(value = "select * from hotel h order by h.rate desc nulls last limit 10", nativeQuery = true)
    List<HotelEntity> getTopHotels();

    HotelEntity findFirstByAccountId(Long accountId);


    @Query("select new hotel.booking.domain.CountHotelDomain(h.city, count(h.city)) from HotelEntity h group by h.city")
    List<CountHotelDomain> countHotelCity();
}

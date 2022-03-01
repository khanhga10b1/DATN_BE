package hotel.booking.service;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.LocationDomain;
import hotel.booking.domain.OverViewDomain;
import hotel.booking.domain.StatisticYearDomain;

import java.util.List;
import java.util.Map;

public interface StatisticService {

    List<HotelDomain> getTopHotels();

    OverViewDomain getOverview(Long userId);

    Map<String, Object> getBestSeller(Long userId);

    List<StatisticYearDomain> getByMonth(Long userId, Integer year);

    Map<String, Object> getOverviewAdmin();

    List<LocationDomain> getLocation();
}

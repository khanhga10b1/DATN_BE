package hotel.booking.controller;

import hotel.booking.domain.HotelDomain;
import hotel.booking.domain.LocationDomain;
import hotel.booking.domain.OverViewDomain;
import hotel.booking.domain.StatisticYearDomain;
import hotel.booking.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stat")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/top-hotels")
    public ResponseEntity<List<HotelDomain>> getTopHotels() {
        return ResponseEntity.ok(statisticService.getTopHotels());
    }

    @GetMapping("/overview")
    public ResponseEntity<OverViewDomain> getOverview(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(statisticService.getOverview(userId));
    }

    @GetMapping("/bestSellerRoom")
    public ResponseEntity<Map<String, Object>>  getBestSeller(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(statisticService.getBestSeller(userId));
    }

    @GetMapping("/yearData")
    public ResponseEntity<List<StatisticYearDomain>> getYearData(@RequestParam("userId") Long userId,
                                                                 @RequestParam("year") Integer year) {
        return ResponseEntity.ok(statisticService.getByMonth(userId, year));
    }

    @GetMapping("/admin-overview")
    public ResponseEntity<Map<String, Object>> getOverviewAdmin() {
        return ResponseEntity.ok(statisticService.getOverviewAdmin());
    }

    @GetMapping("/location")
    public ResponseEntity<List<LocationDomain>> getLocation(){
        return ResponseEntity.ok(statisticService.getLocation());
    }

}

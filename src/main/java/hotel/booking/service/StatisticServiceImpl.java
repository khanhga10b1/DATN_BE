package hotel.booking.service;

import hotel.booking.constant.Role;
import hotel.booking.domain.*;
import hotel.booking.domain.CountHotelDomain;
import hotel.booking.entity.*;
import hotel.booking.repository.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<HotelDomain> getTopHotels() {
        return hotelRepository.getTopHotels().stream().map(hotelEntity -> {
            HotelDomain hotelDomain = modelMapper.map(hotelEntity, HotelDomain.class);
            hotelDomain.setAmenities(hotelEntity.getHotelAmenitiesEntities().stream().map(HotelAmenitiesEntity::getName).collect(Collectors.toList()));
            hotelDomain.setImage(hotelEntity.getHotelImageEntities().stream().map(HotelImageEntity::getImageLink).collect(Collectors.toList()));
            hotelDomain.setPaypalId(hotelEntity.getUserEntity().getPaypalId());
            return hotelDomain;
        }).collect(Collectors.toList());
    }

    @Override
    public OverViewDomain getOverview(Long userId) {
        OverViewDomain overViewDomain = new OverViewDomain();
        HotelEntity hotelEntity = hotelRepository.findFirstByAccountId(userId);
        if (hotelEntity != null) {
            long rooms = roomRepository.countByHotelId(hotelEntity.getId());
            overViewDomain.setRooms(rooms);
            List<ReservationEntity> reservationEntities = reservationRepository.findByHotelId(hotelEntity.getId());
            overViewDomain.setReservations(reservationEntities.size());
            overViewDomain.setRevenue(reservationEntities.stream().mapToDouble(ReservationEntity::getCost).sum());
            List<RatingDomain> ratingDomains = ratingRepository.findByHotelId(hotelEntity.getId()).stream().map(rating -> modelMapper.map(rating, RatingDomain.class)).collect(Collectors.toList());
            overViewDomain.setRatings(ratingDomains);
        } else {
            overViewDomain.setRooms(0L);
            overViewDomain.setReservations(0);
            overViewDomain.setRatings(new ArrayList<>());
            overViewDomain.setRevenue(0D);
        }

        return overViewDomain;
    }

    @Override
    public Map<String, Object> getBestSeller(Long userId) {

        HotelEntity hotelEntity = hotelRepository.findFirstByAccountId(userId);
        Map<String, Object> result = new HashMap<>();
        if (hotelEntity != null) {
            List<RoomEntity> roomEntities = roomRepository.getAllByHotelId(hotelEntity.getId());
            List<Long> data = new ArrayList<>();
            roomEntities.forEach(roomEntity -> {
                data.add(reservationRepository.countByHotelIdAndRoomId(hotelEntity.getId(), roomEntity.getId()));
            });
            result.put("data", data);
            result.put("categories", roomEntities.stream().map(RoomEntity::getName).collect(Collectors.toList()));
        } else {
            result.put("categories", new ArrayList<>());
            result.put("data", new ArrayList<>());
        }
        return result;
    }

    @Override
    public List<StatisticYearDomain> getByMonth(Long userId, Integer year) {
        List<StatisticYearDomain> yearDomains = new ArrayList<>();
        HotelEntity hotelEntity = hotelRepository.findFirstByAccountId(userId);
        if (hotelEntity != null) {
            yearDomains = countNewsByMonth(hotelEntity.getId());
        } else {
            yearDomains.add(new StatisticYearDomain(2022, new ArrayList<>()));
        }
        return yearDomains;
    }

    @Override
    public Map<String, Object> getOverviewAdmin() {
        Long hotels = hotelRepository.count();
        Long accounts = userRepository.countByRole(Collections.singletonList(Role.USER.getCode()));
        Long admins = userRepository.countByRole(Arrays.asList(Role.ADMIN.getCode(), Role.SUPER_ADMIN.getCode()));
        Map<String, Object> result = new HashMap<>();
        result.put("hotels", hotels);
        result.put("accounts", accounts);
        result.put("admins", admins);
        return result;
    }

    @Override
    public List<LocationDomain> getLocation() {
        List<LocationDomain> result;
        long hotels = hotelRepository.count();
        List<CountHotelDomain> countHotelDomains = hotelRepository.countHotelCity();
        countHotelDomains.sort(Comparator.comparing(CountHotelDomain::getCount).reversed());
        if (countHotelDomains.size() <= 3) {
            result = countHotelDomains.stream().map(hotel -> {
                float div = (float) 100 * hotel.getCount() / hotels;
                float percent = (float) Math.round(div * 10) / 10;
                return new LocationDomain(hotel.getCity(), percent);
            }).collect(Collectors.toList());
        } else {
            result = countHotelDomains.stream().map(hotel -> {
                float div = (float) 100 * hotel.getCount() / hotels;
                float percent = (float) Math.round(div * 10) / 10;
                return new LocationDomain(hotel.getCity(), percent);
            }).limit(3).collect(Collectors.toList());
            long other = hotels - countHotelDomains.stream().mapToLong(CountHotelDomain::getCount).sum();

            float div = (float) 100 * other / hotels;
            float percent = (float) Math.round(div * 10) / 10;
            result.add(new LocationDomain("other", percent));
        }
        return result;
    }

    private List<StatisticYearDomain> countNewsByMonth(Long hotelId) {
        List<StatisticYearDomain> yearDomains = getYear();
        for (StatisticYearDomain yearDomain : yearDomains) {
            List<Long> data = new ArrayList<>();
            for (int i = 1; i < 13; i++) {
                Date dateG, dateL;
                dateG = setTimes(1, i, yearDomain.getYear());
                if (i == 12) {
                    dateL = setTimes(1, i + 1, yearDomain.getYear() + 1);
                } else {
                    dateL = setTimes(1, i + 1, yearDomain.getYear());
                }
                Long totalNewsByMonth = reservationRepository.countByHotelIAndCreatedDate(hotelId, dateG, dateL);
                data.add(totalNewsByMonth);
            }
            yearDomain.setData(data);
        }

        return yearDomains;
    }

    private List<StatisticYearDomain> getYear() {
        List<ReservationEntity> reservationEntities = reservationRepository.findAll();
        List<StatisticYearDomain> result = new ArrayList<>();
        reservationEntities.forEach(reservationEntity -> {
            Date create = reservationEntity.getCreatedDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(create);
            Integer year = calendar.get(Calendar.YEAR);
            boolean check = true;
            for (StatisticYearDomain domain : result) {
                if (domain.getYear().equals(year)) {
                    check = false;
                    break;
                }
            }
            if (check) {
                result.add(new StatisticYearDomain(year, new ArrayList<>()));
            }
        });

        return result;
    }

    private Date setTimes(Integer day, Integer month, Integer year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, 7, 0, 0);
        return calendar.getTime();
    }
}

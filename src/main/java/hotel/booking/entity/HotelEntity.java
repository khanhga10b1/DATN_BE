package hotel.booking.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotel")
public class HotelEntity extends BaseEntity {

    private static final long serialVersionUID = 8646083379591166393L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "hotel_seq", sequenceName = "hotel_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hotel_seq")
    private Long id;
    @Column(name = "account_id")
    private Long accountId;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "description")
    private String description;
    @Column(name = "rate")
    private Double rate;

    @OneToOne
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "hotel_id")
    private List<HotelAmenitiesEntity> hotelAmenitiesEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "hotel_id")
    private List<HotelImageEntity> hotelImageEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "hotel_id")
    private List<RatingEntity> ratingEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "hotel_id")
    private List<RoomEntity> roomEntities = new ArrayList<>();

    public HotelEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public List<HotelAmenitiesEntity> getHotelAmenitiesEntities() {
        return hotelAmenitiesEntities;
    }

    public void setHotelImageEntities(List<HotelImageEntity> hotelImageEntities) {
        this.hotelImageEntities = hotelImageEntities;
    }

    public List<HotelImageEntity> getHotelImageEntities() {
        return hotelImageEntities;
    }

    public void setHotelAmenitiesEntities(List<HotelAmenitiesEntity> hotelAmenitiesEntities) {
        this.hotelAmenitiesEntities = hotelAmenitiesEntities;
    }


    public List<RatingEntity> getRatingEntities() {
        return ratingEntities;
    }

    public void setRatingEntities(List<RatingEntity> ratingEntities) {
        this.ratingEntities = ratingEntities;
    }

    public List<RoomEntity> getRoomEntities() {
        return roomEntities;
    }

    public void setRoomEntities(List<RoomEntity> roomEntities) {
        this.roomEntities = roomEntities;
    }
}

package hotel.booking.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
public class RoomEntity extends BaseEntity {


    private static final long serialVersionUID = 8441579541097369715L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "room_seq", sequenceName = "room_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "room_seq")
    private Long id;
    @Column(name = "hotel_id")
    private Long hotelId;
    @Column(name = "name")
    private String name;
    @Column(name = "prepay")
    private Double prepay;
    @Column(name = "status")
    private String status;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private Double price;
    @Column(name = "area")
    private Double area;

    @ManyToOne
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private HotelEntity hotelEntity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<RoomAmenitiesEntity> roomAmenitiesEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<RoomImageEntity> roomImageEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<ReservationEntity> reservationEntities = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "room_id")
    private List<RuleEntity> ruleEntities = new ArrayList<>();

    public RoomEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrepay() {
        return prepay;
    }

    public void setPrepay(Double prepay) {
        this.prepay = prepay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public HotelEntity getHotelEntity() {
        return hotelEntity;
    }

    public void setHotelEntity(HotelEntity hotelEntity) {
        this.hotelEntity = hotelEntity;
    }

    public List<RoomAmenitiesEntity> getRoomAmenitiesEntities() {
        return roomAmenitiesEntities;
    }

    public void setRoomAmenitiesEntities(List<RoomAmenitiesEntity> roomAmenitiesEntities) {
        this.roomAmenitiesEntities = roomAmenitiesEntities;
    }

    public List<RoomImageEntity> getRoomImageEntities() {
        return roomImageEntities;
    }

    public void setRoomImageEntities(List<RoomImageEntity> roomImageEntities) {
        this.roomImageEntities = roomImageEntities;
    }

    public List<ReservationEntity> getReservationEntities() {
        return reservationEntities;
    }

    public void setReservationEntities(List<ReservationEntity> reservationEntities) {
        this.reservationEntities = reservationEntities;
    }

    public List<RuleEntity> getRuleEntities() {
        return ruleEntities;
    }

    public void setRuleEntities(List<RuleEntity> ruleEntities) {
        this.ruleEntities = ruleEntities;
    }
}

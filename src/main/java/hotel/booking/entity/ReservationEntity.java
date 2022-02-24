package hotel.booking.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class ReservationEntity extends BaseEntity {


    private static final long serialVersionUID = 155049212519243044L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "reservation_seq")
    private Long id;
    @Column(name = "room_id")
    private Long roomId;
    @Column(name = "hotel_id")
    private Long hotelId;
    @Column(name = "cancel_reason")
    private String cancelReason;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "email")
    private String email;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "note")
    private String note;
    @Column(name = "check_in")
    private Date checkIn;
    @Column(name = "check_out")
    private Date checkOut;
    @Column(name = "cost")
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "room_id", updatable = false, insertable = false)
    private RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn(name = "hotel_id", updatable = false, insertable = false)
    private HotelEntity hotelEntity;

    public ReservationEntity() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public HotelEntity getHotelEntity() {
        return hotelEntity;
    }

    public void setHotelEntity(HotelEntity hotelEntity) {
        this.hotelEntity = hotelEntity;
    }
}

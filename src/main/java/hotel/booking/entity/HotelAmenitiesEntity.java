package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "hotel_amenities")
public class HotelAmenitiesEntity extends BaseEntity {

    private static final long serialVersionUID = -771926412599360789L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "hotel_amenities_seq", sequenceName = "hotel_amenities_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hotel_amenities_seq")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "hotel_id")
    private Long hotelId;
    @ManyToOne
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private HotelEntity hotelEntity;

    public HotelAmenitiesEntity() {
    }

    public HotelAmenitiesEntity(String name, Long hotelId) {
        this.name = name;
        this.hotelId = hotelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public HotelEntity getHotelEntity() {
        return hotelEntity;
    }

    public void setHotelEntity(HotelEntity hotelEntity) {
        this.hotelEntity = hotelEntity;
    }
}

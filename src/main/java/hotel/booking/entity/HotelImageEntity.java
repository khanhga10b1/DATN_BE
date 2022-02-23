package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "hotel_image")
public class HotelImageEntity extends BaseEntity {
    private static final long serialVersionUID = -5942612259595519841L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "hotel_image_seq", sequenceName = "hotel_image_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hotel_image_seq")
    private Long id;
    @Column(name = "image_link")
    private String imageLink;
    @Column(name = "hotel_id")
    private Long hotelId;
    @ManyToOne
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private HotelEntity hotelEntity;

    public HotelImageEntity() {}

    public HotelImageEntity(String imageLink, Long hotelId) {
        this.imageLink = imageLink;
        this.hotelId = hotelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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

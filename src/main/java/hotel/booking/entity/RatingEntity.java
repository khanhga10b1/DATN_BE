package hotel.booking.entity;

import javax.persistence.*;

@Entity
@Table(name = "rating")
public class RatingEntity extends BaseEntity {


    private static final long serialVersionUID = 155049212519243044L;

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "rating_seq", sequenceName = "rating_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "rating_seq")
    private Long id;
    @Column(name = "hotel_id")
    private Long hotelId;
    @Column(name = "rate")
    private Double rate;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private HotelEntity hotelEntity;

    public RatingEntity() {}

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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HotelEntity getHotelEntity() {
        return hotelEntity;
    }

    public void setHotelEntity(HotelEntity hotelEntity) {
        this.hotelEntity = hotelEntity;
    }
}

package hotel.booking.domain;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

public class RoomDomain {
    private Long id;
    private Long hotelId;
    private String name;
    private Double prepay;
    private String status;
    private String description;
    private Double price;
    private Double area;
    private List<String> image = new ArrayList<>();
    private List<String> amenities = new ArrayList<>();
    private List<String> rules = new ArrayList<>();

    public RoomDomain() {}

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

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}

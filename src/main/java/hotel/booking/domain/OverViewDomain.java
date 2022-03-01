package hotel.booking.domain;

import java.util.List;

public class OverViewDomain {
    private Long rooms;
    private Integer reservations;
    private List<RatingDomain> ratings;
    private Double revenue;

    public OverViewDomain(){}

    public Long getRooms() {
        return rooms;
    }

    public void setRooms(Long rooms) {
        this.rooms = rooms;
    }

    public Integer getReservations() {
        return reservations;
    }

    public void setReservations(Integer reservations) {
        this.reservations = reservations;
    }

    public List<RatingDomain> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingDomain> ratings) {
        this.ratings = ratings;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}

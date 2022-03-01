package hotel.booking.domain;

public class CountHotelDomain {
    private String city;
    private Long count;

    public CountHotelDomain() {}

    public CountHotelDomain(String city, Long count){
        this.city = city;
        this.count = count;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "CountHotelDomain{" +
                "city='" + city + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}

package hotel.booking.domain;

public class LocationDomain {
    private String name;
    private Float percent;

    public LocationDomain(){}

    public LocationDomain(String name, Float percent){
        this.name = name;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }
}

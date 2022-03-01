package hotel.booking.domain;

import java.util.List;

public class StatisticYearDomain {
    private Integer year;
    private List<Long> data;

    public StatisticYearDomain() {}

    public StatisticYearDomain(Integer year, List<Long> data) {
        this.year = year;
        this.data = data;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setData(List<Long> data) {
        this.data = data;
    }

    public List<Long> getData() {
        return data;
    }
}

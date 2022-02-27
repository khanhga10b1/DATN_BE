package hotel.booking.domain.request;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CheckValidRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date checkIn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date checkOut;
    private Long roomId;

    public CheckValidRequest() {}

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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "CheckValidRequest{" +
                "checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", roomId=" + roomId +
                '}';
    }
}


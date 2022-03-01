package hotel.booking.domain.request;

public class ChangeStatusRequest {
    private Boolean status;

    public ChangeStatusRequest() {}

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

package hotel.booking.domain.request;

public class PasswordRequest {
    private String oldPass;
    private String newPass;

    public PasswordRequest() {}

    public String getOldPass() {
        return oldPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }
}

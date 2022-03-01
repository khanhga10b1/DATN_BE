package hotel.booking.domain.request;

public class AdminRequest {
    private String avatar;
    private String name;
    private String email;
    private String password;
    private String roleCode;
    private boolean status;
    private boolean changeStatus;

    public AdminRequest() {
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }


    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }
}

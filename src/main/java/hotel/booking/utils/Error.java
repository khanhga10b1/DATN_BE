package hotel.booking.utils;

public enum Error {

    DATA_NOT_FOUND("S0001", "Data not found"),
    REQUIRED_FIELD("S0002", "Required"),
    BAD_CREDENTIALS("S0003", "Bad credentials"),
    TOKEN_INVALID("S0004", "Expired or invalid JWT token"),
    BAD_REQUEST("S0005", "Bad Request"),
    OLD_PASSWORD_IS_WRONG("C0006", "Old password is incorrect"),
    INVALID_USERNAME_OR_PASSWORD("S0007", "Sai tài khoản hoặc mật khẩu"),
    USER_IS_LOCK("S0008", "Tài khoản đã bị khóa, vui lòng liên hệ admin để được giải quyết"),
    EXIST_EMAIL("S0009", "Email này đã tồn tại!"),
    WRONG_EMAIL_PASSWORD("S0010", "Sai tài khoản hoặc mật khẩu");



    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    Error(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

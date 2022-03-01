package hotel.booking.constant;

public enum Role {
    USER("USER", "User"),
    ADMIN("ADMIN", "Admin"),
    SUPER_ADMIN("SUPER_ADMIN", "Super Admin");

    private final String code;
    private final String name;

    Role(String code, String name) {
        this.code =code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

package hotel.booking.utils;

public class StringUtils {

    public static final String PATTERN_LOG = "[line: %s] %s: %s";

    public static String convertObjectToString(Object input) {
        return input == null ? null : input.toString();

    }

    public static String convertObjectToStringOrEmpty(Object input) {
        return input == null ? "" : input.toString();
    }

    public static String buildLog(Error err, int line) {
        return String.format(PATTERN_LOG, line, err.getCode(), err.getMessage());
    }

    public static String buildLog(String err, int line) {
        return String.format(PATTERN_LOG, line, null , err);
    }
}

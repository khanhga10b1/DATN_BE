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

    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static String replaceSpecialCharacter(String value) {
        if(value != null) {
            value = value.replaceAll("\\\\", "\\\\\\\\");
            value = value.replaceAll("%","\\\\%");
            value = value.replaceAll("_","\\\\_");
        } else {
            value = "";
        }
        return value;
    }
}


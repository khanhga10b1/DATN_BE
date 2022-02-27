package hotel.booking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static boolean isBetween(Long date, Long min, Long max) {
        return date >= min && date <= max;
    }

    public static Date getDateWithoutTimeUsingFormat(Date date)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
    }

    public static Date convertStringToDate(String date, String pattern) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(date);
    }
}

package PizzaHut_be.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtil {
    public final String GOOGLE_DATE_PATTERN = "YYYY-MM-DD";
    public final String FACEBOOK_DATE_PATTERN = "MM/DD/YYYY";
    public final String LOCK_LOGIN_DATE_PATTERN = "dd-MM-yyyy HH:mm:ss";
    public final String DATE_FORMAT = "dd/MM/yyyy";
    public final String RESPONSE_DATE_FORMAT = "yyyy-MM-dd";

    public LocalDate convertDateResponseToLocalDate(String dateStr, String pattern) {
        if (!Util.isNullOrEmpty(dateStr)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            return LocalDate.parse(dateStr, formatter);
        }

        return null;
    }
}

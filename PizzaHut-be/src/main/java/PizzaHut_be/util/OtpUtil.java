package PizzaHut_be.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasLength;

@UtilityClass
public class OtpUtil {
    private static final Pattern OTP_PATTERN = Pattern.compile("^\\d{4}$");

    /***
     * Check if the otp is valid
     * @param otp
     * @return
     */
    public boolean valid(String otp) {
        return hasLength(otp) && OTP_PATTERN.matcher(otp).matches();
    }
}

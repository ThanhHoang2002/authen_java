package PizzaHut_be.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasText;

@UtilityClass
public class PasswordUtil {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[^\\w\\s]).{8,36}$");

    /***
     * Check if the password is valid
     * @param password
     * @return
     */
    public boolean valid(String password) {
        return hasText(password) && PASSWORD_PATTERN.matcher(password).matches();
    }
}

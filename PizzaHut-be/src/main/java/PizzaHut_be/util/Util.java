package PizzaHut_be.util;

import PizzaHut_be.model.constant.FileConstant;
import PizzaHut_be.model.constant.RedisConstant;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {
    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public String generateRedisKey(String... arg) {
        return String.join(RedisConstant.SEPARATOR, arg);
    }

    public String generateFileDirectory(String... arg) {
        return String.join(FileConstant.DIRECTORY_DIVIDE, arg);
    }
}

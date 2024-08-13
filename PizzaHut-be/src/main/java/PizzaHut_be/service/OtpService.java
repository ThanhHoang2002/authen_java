package PizzaHut_be.service;

import PizzaHut_be.model.constant.RedisPrefixKeyConstant;
import PizzaHut_be.model.enums.LanguageEnum;
import PizzaHut_be.model.enums.OtpDestinationEnum;
import PizzaHut_be.model.enums.OtpRequestTypeEnum;
import PizzaHut_be.util.OtpUtil;
import PizzaHut_be.util.Util;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.security.SecureRandom;
import java.util.Locale;

@Service
@CustomLog
@RequiredArgsConstructor
@Validated
public class OtpService {
    private final JedisPool jedisPool;
    private final MailOtpService mailOtpService;

    private String otpDestinationDefault = "EMAIL";
    @Value("${app.otp.expire-time.default}")
    private int expireOtpDefault;

    /***
     * Send OTP to client via email or phone
     * @param mailOrPhone
     * @param language
     * @param otpDestinationEnum
     * @param otpRequestTypeEnum
     * @param expiredTime
     * @return
     */
    public boolean sendOtp(String language, Integer expiredTime, Locale locale, @NotBlank String mailOrPhone, @NotNull OtpDestinationEnum otpDestinationEnum, @NotNull OtpRequestTypeEnum otpRequestTypeEnum) {
        locale = locale != null ? locale : getLocaleByUserLanguage(language);

        if (expiredTime == null) expiredTime = expireOtpDefault;

        String otp = generateOtp(mailOrPhone, otpRequestTypeEnum, expiredTime);

        switch (otpDestinationEnum) {
            case DEFAULT:
                return sendOtp(language, expiredTime, locale, mailOrPhone, OtpDestinationEnum.valueOf(otpDestinationDefault), otpRequestTypeEnum);
            case EMAIL:
                return mailOtpService.sendOtpMail(mailOrPhone, otp, locale);
        }
        return false;
    }

    /***
     * Get user locale by user language
     * @param language
     * @return
     */
    private Locale getLocaleByUserLanguage(String language) {
        if (Util.isNullOrEmpty(language)) return Locale.forLanguageTag(LanguageEnum.VI.toString());

        return Locale.forLanguageTag(language);
    }

    /***
     * Verify otp by find in redis
     * @param mailOrPhone
     * @param otpRequestTypeEnum
     * @return
     */
    public boolean verifyOtp(String mailOrPhone, OtpRequestTypeEnum otpRequestTypeEnum, String otp) {
        String otpLoginKey = Util.generateRedisKey(RedisPrefixKeyConstant.OTP, otpRequestTypeEnum.toString(), mailOrPhone, otp);

        if (!OtpUtil.valid(otp)) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String otpRedisValue = jedis.get(otpLoginKey);

            if (!Util.isNullOrEmpty(otpRedisValue) && otp.equals(otpRedisValue)) {

                new Thread(() -> {
                    Jedis jedisThread = null;

                    try {
                        jedisThread = jedisPool.getResource();
                        jedisThread.del(otpLoginKey);
                    } catch (Exception e) {
                        log.error("Error while get OTP in redis: " + e.getMessage(), e);
                    } finally {
                        if (jedisThread != null) {
                            jedisThread.close();
                        }
                    }

                }).start();

                return true;
            }
        } catch (Exception e) {
            log.error("Error while get OTP in redis: " + e.getMessage(), e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return false;
    }

    /***
     * Generate OTP and save to Redis
     * @param mailOrPhone
     * @param otpRequestTypeEnum
     * @param expiredTime
     * @return
     */
    private String generateOtp(String mailOrPhone, OtpRequestTypeEnum otpRequestTypeEnum, int expiredTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // Generate OTP until not duplicate
            String otp = generateOtp();

            while (jedis.exists(Util.generateRedisKey(RedisPrefixKeyConstant.OTP, otpRequestTypeEnum.toString(), mailOrPhone, otp))) {
                otp = generateOtp();
            }

            log.info("OTP is: " + otp);

            String key = Util.generateRedisKey(RedisPrefixKeyConstant.OTP, otpRequestTypeEnum.toString(), mailOrPhone, otp);
            jedis.set(key, otp);
            jedis.expire(key, expiredTime);
            return otp;
        } catch (Exception e) {
            log.error("Error while generating OTP: ", e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /***
     * Generate OTP
     * @return
     */
    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        return String.format("%04d", random.nextInt(9999));
    }
}

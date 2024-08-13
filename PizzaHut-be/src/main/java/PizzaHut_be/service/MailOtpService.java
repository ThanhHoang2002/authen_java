package PizzaHut_be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Locale;

@CustomLog
@Service
@RequiredArgsConstructor
public class MailOtpService {
    private static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=\"utf-8\"";
    private final LanguageService languageService;
    private final JavaMailSender mailSender;
    private final EmailContentService emailContentService;
    /***
     * Send OTP to client via email
     * @param email
     * @param otp
     * @param locale
     * @return
     */
    public boolean sendOtpMail(String email, String otp, Locale locale) {
        log.info("Start send OTP {} for email : {}", otp, email);
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            String emailName = email.substring(0, email.lastIndexOf("@"));
            helper.setTo(email);
            helper.setSubject(languageService.getMessage("otp.accuracy.subject"));
            mimeMessage.setContent(emailContentService.getContent(emailName, otp, locale), CONTENT_TYPE_TEXT_HTML);
            mailSender.send(mimeMessage);
            log.info("End send OTP {} for email: {}", otp, email);
            return true;
        } catch (MessagingException e) {
            log.error("Error sending OTP mail: ", e);
        }
        log.info("End send OTP {} for email: {}", otp, email);
        return false;
    }
}

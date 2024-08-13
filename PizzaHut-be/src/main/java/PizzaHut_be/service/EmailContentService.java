package PizzaHut_be.service;

import PizzaHut_be.model.enums.LanguageEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Locale;

@Service
public class EmailContentService {
    private static final String MAIL_TEMPLATE_PREFIX = "/templates/";
    private static final String MAIL_TEMPLATE_SUFFIX = ".html";
    private static final String UTF_8 = "UTF-8";

    private static final String TEMPLATE_NAME_EN = "mail-template-en";
    private static final String TEMPLATE_NAME_LO = "mail-template-lo";
    private static final String TEMPLATE_NAME_VI = "mail-template-vi";
    private static final String TEMPLATE_NAME_ZH = "mail-template-zh";

    private final TemplateEngine templateEngine = emailTemplateEngine();

    private TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    private ResourceBundleMessageSource emailMessageSource() {
        return new ResourceBundleMessageSource();
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(MAIL_TEMPLATE_PREFIX);
        templateResolver.setSuffix(MAIL_TEMPLATE_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public String getContent(String userName, String otp, @NotNull Locale locale) {
        Context context = new Context();
        context.setVariable("username", userName);
        context.setVariable("otp", otp);
        switch (LanguageEnum.valueOf(locale.toString().toUpperCase())) {
            case EN:
                return templateEngine.process(TEMPLATE_NAME_EN, context);
            case ZH:
                return templateEngine.process(TEMPLATE_NAME_ZH, context);
            case VI:
                return templateEngine.process(TEMPLATE_NAME_VI, context);
            default:
                return templateEngine.process(TEMPLATE_NAME_LO, context);
        }
    }
}

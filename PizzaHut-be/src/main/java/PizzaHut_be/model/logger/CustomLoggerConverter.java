package PizzaHut_be.model.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class CustomLoggerConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String lineNumber = event.getCallerData()[0].getLineNumber() + "";
        String customLineNumber = event.getMDCPropertyMap().get("line");

        if (customLineNumber == null || customLineNumber.trim().isEmpty()) {
            return lineNumber;
        } else {
            return customLineNumber;
        }
    }
}

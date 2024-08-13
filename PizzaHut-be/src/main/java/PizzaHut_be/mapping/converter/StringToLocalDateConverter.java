package PizzaHut_be.mapping.converter;

import PizzaHut_be.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;

@RequiredArgsConstructor
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private final String formatDatePattern;

    @Override
    public LocalDate convert(MappingContext<String, LocalDate> context) {
        return DateUtil.convertDateResponseToLocalDate(context.getSource(), formatDatePattern);
    }
}

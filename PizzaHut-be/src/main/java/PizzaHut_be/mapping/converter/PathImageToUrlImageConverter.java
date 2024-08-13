package PizzaHut_be.mapping.converter;

import PizzaHut_be.util.Util;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class PathImageToUrlImageConverter implements Converter<String, String> {
    private final String minioUrl;

    public PathImageToUrlImageConverter(String minioUrl) {
        this.minioUrl = minioUrl;
    }

    @Override
    public String convert(MappingContext<String, String> context) {
        String fileDirectory = context.getSource();

        if (Util.isNullOrEmpty(fileDirectory)) {
            return null;
        }

        return Util.generateFileDirectory(minioUrl, fileDirectory);
    }
}

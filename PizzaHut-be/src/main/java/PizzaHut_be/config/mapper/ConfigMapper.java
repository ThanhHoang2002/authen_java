package PizzaHut_be.config.mapper;

import PizzaHut_be.mapping.GoogleUserDtoToUserModelMapping;
import PizzaHut_be.mapping.UserModelToUserResponseMapping;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigMapper {
    @Value("${minio.url.public}")
    private String minioUrl;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new GoogleUserDtoToUserModelMapping());
        modelMapper.addMappings(new UserModelToUserResponseMapping(minioUrl));
        return modelMapper;
    }
}

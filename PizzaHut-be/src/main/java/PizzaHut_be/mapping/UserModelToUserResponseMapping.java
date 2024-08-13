package PizzaHut_be.mapping;

import PizzaHut_be.mapping.converter.PathImageToUrlImageConverter;
import PizzaHut_be.model.dto.response.ResponseUser;
import PizzaHut_be.model.entity.Client;
import org.modelmapper.PropertyMap;

public class UserModelToUserResponseMapping extends PropertyMap<Client, ResponseUser> {
    private String minioUrl;

    public UserModelToUserResponseMapping(String minioUrl) {
        this.minioUrl = minioUrl;
    }

    @Override
    protected void configure() {
        using(new PathImageToUrlImageConverter(minioUrl)).map(source.getAvatar()).setAvatar(null);
    }
}

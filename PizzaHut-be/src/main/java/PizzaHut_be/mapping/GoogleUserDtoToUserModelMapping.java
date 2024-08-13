package PizzaHut_be.mapping;

import PizzaHut_be.mapping.converter.StringToLocalDateConverter;
import PizzaHut_be.model.dto.GoogleUserDto;
import PizzaHut_be.model.entity.Client;
import PizzaHut_be.util.DateUtil;
import org.modelmapper.PropertyMap;

public class GoogleUserDtoToUserModelMapping extends PropertyMap<GoogleUserDto, Client> {
    @Override
    protected void configure() {
        using(new StringToLocalDateConverter(DateUtil.GOOGLE_DATE_PATTERN)).map(source.getBirthday()).setBirthday(null);
        map().setEmail(source.getEmail());
        map().setPhone(source.getPhoneNumber());
        map().setAvatar(source.getPicture());
    }
}

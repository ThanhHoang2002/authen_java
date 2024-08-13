package PizzaHut_be.model.enums;

public enum OtpDestinationEnum {
    DEFAULT(0), PHONE(1), EMAIL(2) ;

    public final int value;

    OtpDestinationEnum(int i) {
        value = i;
    }
}

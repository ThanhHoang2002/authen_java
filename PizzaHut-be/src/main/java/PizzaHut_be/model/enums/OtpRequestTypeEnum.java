package PizzaHut_be.model.enums;

public enum OtpRequestTypeEnum {
    LOGIN(0), RESET_PASSWORD(1), LINK(2) , DELETE(3) , SET_PRIMARY_VALUE(4);

    public final int value;

    OtpRequestTypeEnum(int i) {
        value = i;
    }
}

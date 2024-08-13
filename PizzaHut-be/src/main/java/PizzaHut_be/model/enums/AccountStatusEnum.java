package PizzaHut_be.model.enums;

public enum AccountStatusEnum {
    DEACTIVE(0), ACTIVE(1), DELETE(2), LOCK(3), NEED_LINK(4), WAIT_ACTIVE(5);

    public final int value;

    AccountStatusEnum(int i) {
        value = i;
    }
}

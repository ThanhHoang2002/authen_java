package PizzaHut_be.model.enums;

public enum Role {
    MANAGER(0), STAFF(1), USER(2);

    public final int value;

    Role(int i) {
        value = i;
    }
}

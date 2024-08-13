package PizzaHut_be.model.logger;

public class CustomLoggerFactory {
    public static CustomLogger getLogger(String name) {
        return new CustomLogger(name);
    }
}

package courseworks.sockshop.model;

public enum Color {
    RED("Красный"),
    BLUE("Синий"),
    GREEN("Зелёный"),
    WHITE("Белый"),
    YELLOW("Желтый");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

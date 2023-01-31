package courseworks.sockshop.model;

public enum Size {
    XS("25"), S("27"), M("28"), L("29"), XL("31");

    private final String value;

    Size(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

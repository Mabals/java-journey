package Lesson15;



public enum Category {
    INCOME("Income"),
    HOUSING("Housing"),
    GROCERIES("Groceries"),
    TRANSPORT("Transport"),
    AIRTIME("Airtime"),
    ENTERTAINMENT("Entertainment"),
    UTILITIES("Utilities"),
    OTHER("Other");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Category fromText(String text) {
        for (Category category : values()) {
            if (category.label.equalsIgnoreCase(text.trim())) {
                return category;
            }
        }
        return OTHER;
    }
}

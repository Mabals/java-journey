package Lesson14;



public class EnumDemo {
    public static void main(String[] args) {
        Category c = Category.GROCERIES;
        System.out.println(c);

        if (c == Category.GROCERIES) {
            System.out.println("It's groceries");
        }

        String advice = switch (c) {
            case GROCERIES -> "Compare prices at different shops";
            case TRANSPORT -> "Consider a monthly taxi or bus pass";
            case ENTERTAINMENT -> "Set a monthly limit";
            default -> "No tip for this category";
        };
        System.out.println(advice);

        for (Category category : Category.values()) {
            System.out.print(category + " ");
        }
        System.out.println();

        Category parsed = Category.valueOf("TRANSPORT");
        System.out.println(parsed);

        System.out.println(Category.fromText(" groceries "));
        System.out.println(Category.fromText("Crypto"));
        System.out.println(Category.HOUSING.getLabel());
    }
}

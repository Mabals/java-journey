package Lesson14;

public class RecordDemo {
    public static void main(String[] args) {
        Transaction rent = new Transaction("2026-09-02", "Rent", -7500.00, Category.HOUSING);
        System.out.println(rent);
        System.out.println(rent.description() + " | " + rent.category().getLabel());

        Transaction sameRent = new Transaction("2026-09-02", "Rent", -7500.00, Category.HOUSING);
        System.out.println(rent.equals(sameRent));
        System.out.println(rent == sameRent);

        Transaction moved = rent.withCategory(Category.OTHER);
        System.out.println(rent.category() + " → " + moved.category());

        Transaction broken = new Transaction("2026-09-03", "  ", -50.00, Category.OTHER);
    }
}

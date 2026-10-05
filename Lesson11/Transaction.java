package Lesson11;

public class Transaction {
    private final String date;
    private final String description;
    private final double amount;
    private String category;

    /*1. 🐛 The constructor skips the category validation

setCategory rejects a blank category, but the constructor sets this.category = category; without checking. So this works, when it shouldn't:

java
new Transaction("2026-10-05", "Salary", 25000, "");   // ✅ allowed. Blank category!

Validation must happen on every route into the object. Here, there are two routes: the constructor and the setter. The neatest fix is to have the constructor call setCategory(category) instead of assigning the field directly. Then the rule lives in one place, and both routes use it. */

    public Transaction(String date, String description, double amount, String category) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (amount == 0) {
            throw new IllegalArgumentException("Amount cannot be zero");
        }
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category is required");
        }
        this.category = category;
    }

    public boolean isExpense() {
        return amount < 0;
    }

    public boolean isIncome() {
        return amount > 0;
    }

    public double getAbsoluteAmount() {
        return Math.abs(amount);
    }

    @Override
    public String toString() {
        return description + " | " + String.format("%.2f", amount) + " | " + category;
    }
}

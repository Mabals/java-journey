package Lesson12;

public class Transaction {
    private final String date;
    private final String description;
    private final double amount;
    private String category;


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

package Lesson15;

public record Transaction(String date, String description, double amount, Category category) {

    public Transaction {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (amount == 0) {
            throw new IllegalArgumentException("Amount cannot be zero");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category is required");
        }
    }

    public boolean isExpense() {
        return amount < 0;
    }

    public boolean isIncome() {
        return amount > 0;
    }

    public double absoluteAmount() {
        return Math.abs(amount);
    }

    public Transaction withCategory(Category newCategory) {
        return new Transaction(date, description, amount, newCategory);
    }
}

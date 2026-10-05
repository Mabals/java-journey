package Lesson10;

public class Transaction {
    String date;
    String description;
    double amount;
    String category;

    public Transaction(String date, String description, double amount, String category) {
        this.date = date;
        this.description = description;
        this.amount = amount;
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

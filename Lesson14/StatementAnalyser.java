package Lesson14;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;



public class StatementAnalyser {

    

    public static void main(String[] args) {
        Path path = Path.of("Lesson14", "transactions.csv");
        List<Transaction> transactions = new ArrayList<>();
        int skippedCount = 0;

        try {
            List<String> lines = Files.readAllLines(path);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                try {
                    Transaction transaction = parseTransaction(line);
                    transactions.add(transaction);
                } catch (IllegalArgumentException e) {
                    System.out.printf("Skipping line %d: %s%n", i + 1, e.getMessage());
                    skippedCount++;
                }
            }
        } catch (NoSuchFileException e) {
            System.out.println("File not found: " + path);
            return;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        String report = buildReport(transactions, skippedCount);
        System.out.println(report);
        saveReport(report, "Lesson14/report.txt");
        
    }

    public static Transaction parseTransaction(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) {
            throw new IllegalArgumentException("expected 4 columns, got " + parts.length);
        }

        String date = parts[0].trim();
        String description = parts[1].trim();
        double amount;
        try {
            amount = Double.parseDouble(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid amount '" + parts[2].trim() + "'");
        }
        Category category = Category.fromText(parts[3]);

        return new Transaction(date, description, amount, category);
    }

    

    public static double calculateIncome(List<Transaction> transactions) {
        double totalIncome = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isIncome()) {
                totalIncome += transaction.amount();
            }
        }
        return totalIncome;    
    }

    public static double calculateExpenses(List<Transaction> transactions) {
        double totalExpenses = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isExpense()) {
                totalExpenses += transaction.absoluteAmount();
            }
        }
        return totalExpenses;
    }

    public static Map<Category, Double> spendingByCategory(List<Transaction> transactions) {
        Map<Category, Double> categoryTotals = new EnumMap<>(Category.class);
        for (Transaction transaction : transactions) {
            if (transaction.isExpense()) {
                Category category = transaction.category();
                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + transaction.absoluteAmount());
            }
        }
        return categoryTotals;
    }

    public static Transaction findLargestExpense(List<Transaction> transactions) {
        //findLargestExpense	Transaction (or null)	The method from section 9 of the lesson
        Transaction largestExpense = null;
        double maxAmount = 0; // Start at 0 to find the largest expense
        for (Transaction transaction : transactions) {
            if (transaction.isExpense()) {
                double amount = transaction.absoluteAmount();
                if (amount > maxAmount) {
                    maxAmount = amount;
                    largestExpense = transaction;
                }
            }
        }
        return largestExpense;
    }


    public static String buildReport(List<Transaction> transactions, int skippedCount) {
        // 1. Calculate everything first
        double totalIncome = calculateIncome(transactions);
        double totalExpenses = calculateExpenses(transactions);
        double net = totalIncome - totalExpenses;
        Map<Category, Double> categoryTotals = spendingByCategory(transactions);
        Transaction largestExpense = findLargestExpense(transactions);

        // 2. Then build the text
        StringBuilder report = new StringBuilder();
        report.append("===== Statement Summary =====\n");
        report.append(String.format("Transactions processed: %d (%d skipped)%n%n", transactions.size(), skippedCount));
        report.append(String.format("Total income:      R%,.2f%n", totalIncome));
        report.append(String.format("Total expenses:    R%,.2f%n", totalExpenses));
        report.append(String.format("Net:               R%,.2f%n%n", net));

        report.append("Spending by category:\n");
        for (Map.Entry<Category, Double> entry : categoryTotals.entrySet()) {
            report.append(String.format("  %-15s R%,10.2f%n", entry.getKey().getLabel(), entry.getValue()));
        }

        if (largestExpense != null) {
            report.append(String.format("%nLargest expense: %s (R%,.2f)%n",
                    largestExpense.description(), largestExpense.absoluteAmount()));
        } else {
            report.append("\nLargest expense: none\n");
        }

        return report.toString();
    }


    public static void saveReport(String report, String filename) {
        Path path = Path.of(filename);
        try {
            Files.writeString(path, report);
            System.out.println("Report saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving report: " + e.getMessage());
        }


    }
}

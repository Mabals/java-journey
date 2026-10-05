package Lesson10;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatementAnalyser {

    

    public static void main(String[] args) {
        Path path = Path.of("Lesson10", "transactions.csv");
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
        saveReport(report, "Lesson10/report.txt");
        
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
        String category = parts[3].trim();

        return new Transaction(date, description, amount, category);
    }

    

    public static double calculateIncome(List<Transaction> transactions) {
        double totalIncome = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isIncome()) {
                totalIncome += transaction.amount;
            }
        }
        return totalIncome;    
    }

    public static double calculateExpenses(List<Transaction> transactions) {
        double totalExpenses = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isExpense()) {
                totalExpenses += transaction.getAbsoluteAmount();
            }
        }
        return totalExpenses;
    }

    public static Map<String, Double> spendingByCategory(List<Transaction> transactions) {
        Map<String, Double> categoryTotals = new TreeMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.isExpense()) {
                String category = transaction.category;
                double amount = transaction.getAbsoluteAmount();
                categoryTotals.put(category, categoryTotals.getOrDefault(category, 0.0) + amount);
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
                double amount = transaction.getAbsoluteAmount();
                if (amount > maxAmount) {
                    maxAmount = amount;
                    largestExpense = transaction;
                }
            }
        }
        return largestExpense;
    }


    public static String buildReport(List<Transaction> transactions, int skippedCount) {
        double totalIncome = calculateIncome(transactions);
        double totalExpenses = calculateExpenses(transactions);
        double net = totalIncome - totalExpenses;
        Map<String, Double> categoryTotals = spendingByCategory(transactions);
        Transaction largestExpense = findLargestExpense(transactions);

        StringBuilder report = new StringBuilder();
        report.append(String.format("Transactions processed: %d (%d skipped)%n", transactions.size(), skippedCount));
        report.append(String.format("Income R%,.2f, expenses R%,.2f, net R%,.2f%n", totalIncome, totalExpenses, net));
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            report.append(String.format("%s R%,.2f%n", entry.getKey(), entry.getValue()));
        }
        if (largestExpense != null) {
            report.append(String.format("Largest expense: %s (R%,.2f)%n", largestExpense.description, largestExpense.getAbsoluteAmount()));
        } else {
            report.append("Largest expense: none\n");
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

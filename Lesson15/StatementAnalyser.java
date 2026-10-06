package Lesson15;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;



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
        return transactions.stream()
                .filter(Transaction::isIncome)
                .mapToDouble(Transaction::amount)
                .sum();
    }

    public static double calculateExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::absoluteAmount)
                .sum();
    }

    public static Map<Category, Double> spendingByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        Transaction::category,
                        () -> new EnumMap<>(Category.class),
                        Collectors.summingDouble(Transaction::absoluteAmount)));
    }

    public static Optional<Transaction> findLargestExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(Transaction::isExpense)
                .max(Comparator.comparingDouble(Transaction::absoluteAmount));
    }


    public static String buildReport(List<Transaction> transactions, int skippedCount) {
        // 1. Calculate everything first
        double totalIncome = calculateIncome(transactions);
        double totalExpenses = calculateExpenses(transactions);
        double net = totalIncome - totalExpenses;
        Map<Category, Double> categoryTotals = spendingByCategory(transactions);
        Optional<Transaction> largestExpense = findLargestExpense(transactions);

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

        String largestText = largestExpense
                .map(t -> String.format("%s (R%,.2f)", t.description(), t.absoluteAmount()))
                .orElse("none");
        report.append(String.format("%nLargest expense: %s%n", largestText));

        List<Transaction> top3 = transactions.stream()
                .filter(Transaction::isExpense)
                .sorted(Comparator.comparingDouble(Transaction::absoluteAmount).reversed())
                .limit(3)
                .toList();

        report.append("\nTop 3 expenses:\n");
        for (int i = 0; i < top3.size(); i++) {
            Transaction t = top3.get(i);
            report.append(String.format("  %d. %-22s R%,10.2f%n", i + 1, t.description(), t.absoluteAmount()));
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

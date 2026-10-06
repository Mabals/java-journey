package Lesson15;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamDemo {
    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction("2026-09-01", "Salary", 25000.00, Category.INCOME),
                new Transaction("2026-09-02", "Rent", -7500.00, Category.HOUSING),
                new Transaction("2026-09-03", "Checkers", -1250.50, Category.GROCERIES),
                new Transaction("2026-09-05", "Uber", -185.00, Category.TRANSPORT),
                new Transaction("2026-09-10", "Pick n Pay", -980.25, Category.GROCERIES),
                new Transaction("2026-09-15", "Freelance", 3500.00, Category.INCOME)
        );

        long expenseCount = transactions.stream()
                .filter(Transaction::isExpense)
                .count();
        System.out.println("Expenses: " + expenseCount);

        List<String> expenseNames = transactions.stream()
                .filter(Transaction::isExpense)
                .map(Transaction::description)
                .toList();
        System.out.println(expenseNames);

        double totalExpenses = transactions.stream()
                .filter(Transaction::isExpense)
                .mapToDouble(Transaction::absoluteAmount)
                .sum();
        System.out.printf("Total expenses: R%,.2f%n", totalExpenses);

        boolean hasBigExpense = transactions.stream().anyMatch(t -> t.amount() < -5000);
        boolean allValid = transactions.stream().allMatch(t -> t.amount() != 0);
        System.out.println("Any expense over R5,000? " + hasBigExpense);
        System.out.println("All amounts non-zero? " + allValid);

        List<String> top3 = transactions.stream()
                .filter(Transaction::isExpense)
                .sorted(Comparator.comparingDouble(Transaction::absoluteAmount).reversed())
                .limit(3)
                .map(Transaction::description)
                .toList();
        System.out.println("Top 3 expenses: " + top3);

        Map<Category, Double> byCategory = transactions.stream()
                .filter(Transaction::isExpense)
                .collect(Collectors.groupingBy(
                        Transaction::category,
                        () -> new EnumMap<>(Category.class),
                        Collectors.summingDouble(Transaction::absoluteAmount)));
        System.out.println(byCategory);

        String summary = transactions.stream()
                .filter(Transaction::isExpense)
                .map(Transaction::description)
                .collect(Collectors.joining(", "));
        System.out.println("You spent on: " + summary);

        Optional<Transaction> biggest = transactions.stream()
                .filter(Transaction::isExpense)
                .max(Comparator.comparingDouble(Transaction::absoluteAmount));
        System.out.println("Biggest: " + biggest.map(Transaction::description).orElse("none"));

        Optional<Transaction> huge = transactions.stream()
                .filter(t -> t.amount() < -100000)
                .findFirst();
        System.out.println("Over R100,000: " + huge.map(Transaction::description).orElse("none"));
    }
}

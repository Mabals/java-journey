package Lesson9;

/*Exercise 9: Bank statement analyser

This is a mini version of PocketRand's statement import. Your program reads a bank statement CSV, analyses the spending, and saves a report.

The structure
lesson09/
├── transactions.csv         ← the sample bank statement (you create this, copy below)
├── StatementAnalyser.java   ← your program
└── report.txt               ← created BY your program
Step 0: Create the CSV file

Create transactions.csv in lesson09 with exactly this content. Amounts are negative for money going out and positive for money coming in, the way banks export them. The last line is deliberately broken, to test your error handling.

Date,Description,Amount,Category
2026-09-01,Salary,25000.00,Income
2026-09-02,Rent,-7500.00,Housing
2026-09-03,Checkers groceries,-1250.50,Groceries
2026-09-05,Uber,-185.00,Transport
2026-09-07,Vodacom airtime,-99.00,Airtime
2026-09-10,Pick n Pay groceries,-980.25,Groceries
2026-09-12,Taxi fare,-240.00,Transport
2026-09-15,Freelance website,3500.00,Income
2026-09-18,Netflix,-199.00,Entertainment
2026-09-20,Woolworths groceries,-645.80,Groceries
2026-09-22,Petrol,-900.00,Transport
2026-09-25,Electricity,-650.00,Utilities
2026-09-28,Movie night,abc,Entertainment
The target output (printed AND saved to report.txt)
===== Statement Summary =====
Transactions processed: 12 (1 skipped)

Total income:      R28,500.00
Total expenses:    R12,649.55
Net:               R15,850.45

Spending by category:
  Airtime               R99.00
  Entertainment        R199.00
  Groceries          R2,876.55
  Housing            R7,500.00
  Transport          R1,325.00
  Utilities            R650.00

Largest expense: Rent (R7,500.00)

Use these numbers to check your program.

Step 1: Read the file and print every line

File: StatementAnalyser.java, in main
Do: create a Path for transactions.csv, read all lines with readAllLines inside a try/catch (IOException), and print each line.
Why: before any analysis, prove you can find and read the file. This is where the folder gotcha shows up. If you get "file not found", print the absolute path to see where Java is looking.
Test: you see all 14 lines, including the header.

Step 2: Skip the header and split each line

Do: loop from index 1. For each line, split(",") it into parts. If the line doesn't have exactly 4 parts, print a skip message and continue. Then pull out the description, the amount (parsed with Double.parseDouble) and the category.
Wrap the parsing in its own try/catch (NumberFormatException). On failure, print something like "Skipping line 14: invalid amount 'abc'" and count it as skipped.
Why: this is the two-level error handling from section 6. A bad row is skipped, and the rest of the statement is still processed.
Test: print each parsed transaction temporarily. You should see 12 valid ones, plus the skip message for line 14.

Step 3: Total income and expenses

Do: before the loop, create two running totals. In the loop: if the amount is positive, add it to income. If it's negative, add its positive value to expenses. Look up Math.abs, which turns −7500 into 7500. Keep a count of valid transactions too.
Why: it's the sum pattern from Lesson 5, split by a condition. Storing expenses as positive numbers makes the report easier to read.
Test: income R28,500.00, expenses R12,649.55.

Step 4: Spending by category (a map)

Do: create a Map<String, Double> before the loop. Use a TreeMap, so categories come out alphabetically. For each expense, add its amount to its category's total.
Hint: this is the counting pattern from Lesson 7, but instead of adding 1, you add the amount. getOrDefault(category, 0.0) gives the current total, then add the expense and put it back.
Why: "how much did I spend on groceries this month?" is PocketRand's core feature.
Test: Groceries R2,876.55, Transport R1,325.00.

Step 5: The largest expense

Do: track the largest expense amount and its description in two variables before the loop. Update them whenever you find a bigger expense.
Why: it's the max algorithm from Lesson 5. You need the description as well as the amount, so both variables are updated together.
Test: Rent, R7,500.00.

Step 6: Build the report with StringBuilder

Do: after the loop, create a StringBuilder and append every line of the target report using String.format(...). Loop over the category map's entries for the category section. At the end, convert it to a String.
Why: you'll print the report and save it to a file. Building it once means both outputs are guaranteed to match, with no duplicated printf lines. It's also StringBuilder's main job (Lesson 6): building text in a loop.
A clean option is to put this in its own method that takes the values it needs and returns the report String.

Step 7: Print it and save it to report.txt

Do: print the report String. Then write it to report.txt with Files.writeString, inside a try/catch (IOException), and print where it was saved (using toAbsolutePath()).
Why: the program now produces a file someone can keep, which is the whole point of file output.
Test: open report.txt in VS Code. It should match the console output exactly.

Step 8: Test the error handling
Rename transactions.csv to something else and run the program. It should print a friendly message, for example "Statement file not found: transactions.csv", not a stack trace. (Catch NoSuchFileException before IOException.) Then rename it back.
Add a broken line with only 3 columns, like 2026-09-29,Missing category,-50.00. It should be skipped with a message, and the skipped count should go up to 2.
Bonus
Show each category's percentage of total expenses: Housing is 59.3%.
Let the user type the file name (with readString/nextLine) instead of hard-coding it. */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatementAnalyser {
    public static void main(String[] args) {
        Path path = Path.of("Lesson9", "transactions.csv");

        try {
            List<String> lines = Files.readAllLines(path);
            double income = 0;
            double expenses = 0;
            int validTransactions = 0;
            int skippedTransactions = 0;
            double largestExpense = Double.NEGATIVE_INFINITY;
            String largestExpenseDescription = "";
            Map<String, Double> categoryTotals = new TreeMap<>();
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.out.printf("Skipping line %d: expected 4 columns, got %d%n", i + 1, parts.length);
                    skippedTransactions++;
                    continue;
                }
                String description = parts[1];
                double amount;
                try {
                    amount = Double.parseDouble(parts[2]);
                } catch (NumberFormatException e) {
                    System.out.printf("Skipping line %d: invalid amount '%s'%n", i + 1, parts[2]);
                    skippedTransactions++;
                    continue;
                }
                String category = parts[3];
                // For testing, print the parsed transaction
                // System.out.printf("Line %d: Description='%s', Amount=%.2f, Category='%s'%n", i + 1, description, amount,
                //         category);
                if (amount > 0) {
                    income += amount;
                } else {
                    expenses += Math.abs(amount);
                    // Update category total
                    double currentTotal = categoryTotals.getOrDefault(category, 0.0);
                    categoryTotals.put(category, currentTotal + Math.abs(amount));
                    // Update largest expense
                    if (Math.abs(amount) > largestExpense) {
                        largestExpense = Math.abs(amount);
                        largestExpenseDescription = description;
                    }
                }
                validTransactions++;
            }

            String report = buildReport(validTransactions, skippedTransactions, income, expenses, categoryTotals,
                    largestExpenseDescription, largestExpense);
            System.out.println(report);
            saveReport(report, Path.of("Lesson9", "report.txt"));
        } catch (NoSuchFileException e) {
            System.out.println("Statement file not found: " + path);
        } catch (IOException e) {
            System.out.println("Error reading statement file: " + e.getMessage());
        }



    }

    public static String buildReport(int validTransactions, int skippedTransactions, double income, double expenses,
            java.util.Map<String, Double> categoryTotals, String largestExpenseDescription, double largestExpense) {
        StringBuilder report = new StringBuilder();
        report.append("===== Statement Summary =====\n");
        report.append(
                String.format("Transactions processed: %d (%d skipped)\n\n", validTransactions, skippedTransactions));
        report.append(String.format("Total income:      R%,.2f%n", income));
        report.append(String.format("Total expenses:    R%,.2f%n", expenses));
        report.append(String.format("Net:               R%,.2f%n%n", income - expenses));
        report.append("Spending by category:\n");
        for (java.util.Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            report.append(String.format("  %-20s R%,.2f%n", entry.getKey(), entry.getValue()));
        }
        report.append(String.format("%nLargest expense: %s (R%,.2f)%n", largestExpenseDescription, largestExpense));
        return report.toString();
    }

    public static void saveReport(String report, Path reportPath) {
        try {
            Files.writeString(reportPath, report);
            System.out.println("Report saved to: " + reportPath.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error saving report: " + e.getMessage());
        }
    }

    

}

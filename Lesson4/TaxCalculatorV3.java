package Lesson4;

import java.util.Scanner;

public class TaxCalculatorV3 {

    private static final double PRIMARY_REBATE = 17820;
    private static final double SECONDARY_REBATE = 9765; // 65+
    private static final double TERTIARY_REBATE = 3249; // 75+
    private static final double UIF_RATE = 0.01;
    private static final double UIF_CAP = 177.12;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ===== 1. SETUP: runs once, BEFORE the loop =====
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        while (name.isBlank()) { // reject an empty name
            System.out.print("Name can't be empty. Enter your name: ");
            name = sc.nextLine();
        }

        int calculationCount = 0;
        int choice;

        // ===== 2. MAIN LOOP =====
        do {
            printMenu();
            choice = readInt(sc, "Enter your choice: ", 1, 3);

            switch (choice) {
                case 1 -> {
                    runCalculation(sc, name); // 
                    calculationCount++; // count only after a successful calculation
                }
                case 2 -> {
                    printTaxBrackets();
                }
                case 3 -> System.out.printf("You ran %d calculation(s). Goodbye, %s!%n", calculationCount, name);

            }
        } while (choice != 3);

        // ===== 3. CLEANUP =====
        sc.close();
    }

    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                if (value >= min && value <= max) {
                    return value; // valid: return ends the loop AND the method
                }
                System.out.printf("Please enter a number from %d to %d.%n", min, max);
            } else {
                System.out.println("That's not a number. Try again.");
                sc.next();
            }
        }
    }

    public static double readDouble(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                double value = sc.nextDouble();
                if (value >= min && value <= max) {
                    return value; // valid: return ends the loop AND the method
                }
                System.out.printf("Please enter a number from %.2f to %.2f.%n", min, max);
            } else {
                System.out.println("That's not a number. Try again.");
                sc.next();
            }
        }
    }

    public static double calculateTax(double annualGross) {
        double tax;
        if (annualGross <= 245100) {
            tax = annualGross * 0.18;
        } else if (annualGross <= 383100) {
            tax = 44118 + (annualGross - 245100) * 0.26;
        } else if (annualGross <= 530200) {
            tax = 79998 + (annualGross - 383100) * 0.31;
        } else if (annualGross <= 695800) {
            tax = 125599 + (annualGross - 530200) * 0.36;
        } else if (annualGross <= 887000) {
            tax = 185215 + (annualGross - 695800) * 0.39;
        } else if (annualGross <= 1878600) {
            tax = 259783 + (annualGross - 887000) * 0.41;
        } else {
            tax = 666339 + (annualGross - 1878600) * 0.45;
        }
        return tax;

    }

    public static double applyRebates(double tax, int age) {
        double annualTax = Math.max(0, tax - PRIMARY_REBATE);
        if (age >= 65) {
            annualTax = Math.max(0, annualTax - SECONDARY_REBATE);
        }
        if (age >= 75) {
            annualTax = Math.max(0, annualTax - TERTIARY_REBATE);
        }
        return annualTax;

    }

    public static double calculateUif(double monthlyGross) {
        return Math.min(monthlyGross * UIF_RATE, UIF_CAP);
    }

    public static void printMenu() {
        System.out.println("\n===== SARS Tax Calculator 2026/27 =====");
        System.out.println("1. Calculate tax");
        System.out.println("2. Show tax brackets");
        System.out.println("3. Exit");
    }

    public static void printTaxBrackets() {
        System.out.println("\n===== SARS tax brackets 2026/27 =====");
        System.out.printf("%-25s %s%n", "Taxable income (R)", "Tax");
        System.out.printf("%-25s %s%n", "1 – 245 100", "18% of income");
        System.out.printf("%-25s %s%n", "245 101 – 383 100", "44 118 + 26% above 245 100");
        System.out.printf("%-25s %s%n", "383 101 – 530 200", "79 998 + 31% above 383 100");
        System.out.printf("%-25s %s%n", "530 201 – 695 800", "125 599 + 36% above 530 200");
        System.out.printf("%-25s %s%n", "695 801 – 887 000", "185 215 + 39% above 695 800");
        System.out.printf("%-25s %s%n", "887 001 – 1 878 600", "259 783 + 41% above 887 000");
        System.out.printf("%-25s %s%n", "1 878 601 and above", "666 339 + 45% above 1 878 600");
    }

    public static void printSummary(String name, double monthlyGross, double annualGross,
            double annualTax, double monthlyUif) {
        double annualUif = monthlyUif * 12;
        double monthlyTax = annualTax / 12;
        double monthlyTakeHome = monthlyGross - monthlyTax - monthlyUif;
        double annualTakeHome = annualGross - annualTax - annualUif;
        System.out.println("\n===== Summary =====");
        System.out.printf("Name: %s%n", name);
        System.out.printf("Monthly Gross: R%.2f%n", monthlyGross);
        System.out.printf("Annual Gross: R%.2f%n", annualGross);
        System.out.printf("Annual Tax: R%.2f%n", annualTax);
        System.out.printf("Monthly Tax: R%.2f%n", monthlyTax);
        System.out.printf("Monthly UIF: R%.2f%n", monthlyUif);
        System.out.printf("Annual UIF: R%.2f%n", annualUif);
        System.out.printf("Monthly Take-Home: R%.2f%n", monthlyTakeHome);
        System.out.printf("Annual Take-Home: R%.2f%n", annualTakeHome);
    }

    public static void runCalculation(Scanner sc, String name) {

        int salaryOption = readInt(sc, "Enter 1 for monthly salary or 2 for annual salary: ", 1, 2);
        double salary = readDouble(sc, "Enter your salary: R", 1, 100_000_000);
        int age = readInt(sc, "Enter your age: ", 15, 120);

        double monthlyGross;
        double annualGross;
        if (salaryOption == 1) {
            monthlyGross = salary;
            annualGross = salary * 12;
        } else {
            annualGross = salary;
            monthlyGross = salary / 12;
        }

        double tax = calculateTax(annualGross);
        double annualTax = applyRebates(tax, age);
        double monthlyUif = calculateUif(monthlyGross);

        printSummary(name, monthlyGross, annualGross, annualTax, monthlyUif);

    }
}
package Lesson5;

import java.util.Scanner;

public class TaxCalculatorV4 {

    private static final double PRIMARY_REBATE = 17820;
    private static final double SECONDARY_REBATE = 9765; // 65+
    private static final double TERTIARY_REBATE = 3249; // 75+
    private static final double UIF_RATE = 0.01;
    private static final double UIF_CAP = 177.12;

    // SARS 2027 tax year (1 Mar 2026 – 28 Feb 2027)
    private static final double[] BRACKET_START = {0, 245100, 383100, 530200, 695800, 887000, 1878600};
    private static final double[] BRACKET_END   = {245100, 383100, 530200, 695800, 887000, 1878600, Double.MAX_VALUE};
    private static final double[] BASE_TAX      = {0, 44118, 79998, 125599, 185215, 259783, 666339};
    private static final double[] RATE          = {0.18, 0.26, 0.31, 0.36, 0.39, 0.41, 0.45};

    public static void main(String[] args) {

        System.out.println(calculateTax(100000));    // expect 18000.0
        System.out.println(calculateTax(360000));    // expect 73992.0
        System.out.println(calculateTax(2000000));   // expect 720969.0

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
        // 1. loop i through every bracket (which loop type? you need the index)
        double tax = 0;
        for (int i = 0; i < BRACKET_END.length; i++) {
            if (annualGross <= BRACKET_END[i]) { // 2. if annualGross is within this bracket's end...
                tax = BASE_TAX[i] + (annualGross - BRACKET_START[i]) * RATE[i];// 3. ...return the formula above for bracket i
                break; // exit the loop once the correct bracket is found
            }
        }
        // 4. after the loop: return 0 (it will never actually be reached, but Java needs a return on every path)
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

    /*Once that works, make these changes in both printf lines:

Rate: change %-14.2f%% → %.0f%% to get 18% instead of 18.00 %.
Commas, no decimals, right-aligned: change R%-14.2f → %,15.0f. Drop the R, since the header will say "(R)", and drop the -, so numbers right-align.
"and above": change %-15s → %15s so it's right-aligned like the numbers.
Headers: make them match the column widths, for example:
java
   System.out.printf("%15s %15s %15s %5s%n", "From (R)", "To (R)", "Base tax (R)", "Rate");

And the rate column: %4.0f%% (4 wide + the % sign = 5, matching the header's %5s). */
    public static void printTaxBrackets() {
        System.out.println("\n===== SARS Tax Brackets 2026/27 =====");
        System.out.printf("%15s %15s %15s %5s%n", "From (R)", "To (R)", "Base tax (R)", "Rate");
        for (int i = 0; i < BRACKET_START.length; i++) {
            if (i == BRACKET_START.length - 1) {
                System.out.printf("%,15.0f %15s %,15.0f %.0f%%%n",
                        BRACKET_START[i], "and above", BASE_TAX[i], RATE[i] * 100);
            } else {
                System.out.printf("%,15.0f %,15.0f %,15.0f %.0f%%%n",
                        BRACKET_START[i], BRACKET_END[i], BASE_TAX[i], RATE[i] * 100);
            }
        }
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

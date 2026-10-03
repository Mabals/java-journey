import java.util.Scanner;

public class TaxCalculatorV2 {

    private static final double PRIMARY_REBATE = 17820;
    private static final double SECONDARY_REBATE = 9765;   // 65+
    private static final double TERTIARY_REBATE = 3249;    // 75+
    private static final double UIF_RATE = 0.01;
    private static final double UIF_CAP = 177.12;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // ===== 1. SETUP: runs once, BEFORE the loop =====
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        while (name.isBlank()) {                       // reject an empty name
            System.out.print("Name can't be empty. Enter your name: ");
            name = sc.nextLine();
        }

        int calculationCount = 0;
        int choice;

        // ===== 2. MAIN LOOP =====
        do {
            System.out.println("\n===== SARS Tax Calculator 2026/27 =====");
            System.out.println("1. Calculate tax");
            System.out.println("2. Show tax brackets");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            // Validate the menu choice
            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number from 1 to 3.");
                sc.next();          // throw away the bad input
                choice = 0;         // not 3, so the loop continues
                continue;           // jump straight to while (choice != 3)
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    // --- a) Age ---
                    int age;
                    while (true) {
                        System.out.print("Enter your age: ");
                        if (sc.hasNextInt()) {
                            age = sc.nextInt();
                            if (age >= 15 && age <= 120) break;
                            System.out.println("Age must be between 15 and 120.");
                        } else {
                            System.out.println("That's not a number. Try again.");
                            sc.next();
                        }
                    }

                    // --- b) Monthly or annual ---
                    int salaryOption;
                    while (true) {
                        System.out.print("Is your salary monthly or annual? (1 = monthly, 2 = annual): ");
                        if (sc.hasNextInt()) {
                            salaryOption = sc.nextInt();
                            if (salaryOption == 1 || salaryOption == 2) break;
                            System.out.println("Please enter 1 or 2.");
                        } else {
                            System.out.println("That's not a number. Try again.");
                            sc.next();
                        }
                    }

                    // --- c) Salary ---
                    double salary;
                    while (true) {
                        System.out.print("Enter your gross salary: R");
                        if (sc.hasNextDouble()) {
                            salary = sc.nextDouble();
                            if (salary > 0) break;
                            System.out.println("Salary must be more than R0.");
                        } else {
                            System.out.println("That's not a valid amount. Try again.");
                            sc.next();
                        }
                    }

                    // --- d) Convert to monthly + annual ---
                    double monthlyGross;
                    double annualGross;
                    if (salaryOption == 1) {
                        monthlyGross = salary;
                        annualGross = salary * 12;
                    } else {
                        annualGross = salary;
                        monthlyGross = salary / 12;
                    }

                    // --- e) Tax brackets: 2027 tax year, source: SARS ---
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

                    // --- f) Rebates ---
                    double annualTax = Math.max(0, tax - PRIMARY_REBATE);
                    if (age >= 65) annualTax = Math.max(0, annualTax - SECONDARY_REBATE);
                    if (age >= 75) annualTax = Math.max(0, annualTax - TERTIARY_REBATE);
                    double monthlyTax = annualTax / 12;

                    // --- g) UIF + take-home ---
                    double monthlyUif = Math.min(monthlyGross * UIF_RATE, UIF_CAP);
                    double annualUif = monthlyUif * 12;
                    double monthlyTakeHome = monthlyGross - monthlyTax - monthlyUif;
                    double annualTakeHome = annualGross - annualTax - annualUif;

                    // --- h) Summary ---
                    System.out.printf("%n===== Salary summary for %s =====%n", name);
                    System.out.printf("%-12s %15s %15s%n", "", "Monthly", "Annual");
                    System.out.printf("%-12s R%,14.2f R%,14.2f%n", "Gross", monthlyGross, annualGross);
                    System.out.printf("%-12s R%,14.2f R%,14.2f%n", "Tax (PAYE)", monthlyTax, annualTax);
                    System.out.printf("%-12s R%,14.2f R%,14.2f%n", "UIF", monthlyUif, annualUif);
                    System.out.printf("%-12s R%,14.2f R%,14.2f%n", "Take-home", monthlyTakeHome, annualTakeHome);

                    calculationCount++;   // count only after a successful calculation
                }
                case 2 -> {
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
                case 3 -> System.out.printf("You ran %d calculation(s). Goodbye, %s!%n", calculationCount, name);
                default -> System.out.println("Invalid choice. Please choose 1, 2 or 3.");
            }
        } while (choice != 3);

        // ===== 3. CLEANUP =====
        sc.close();
    }
}
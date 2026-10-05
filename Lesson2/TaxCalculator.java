package Lesson2;

import java.util.Scanner;

public class TaxCalculator {

    private static final double PRIMARY_REBATE = 17820;
    private static final double SECONDARY_REBATE = 9765;   // 65+
    private static final double TERTIARY_REBATE = 3249;    // 75+
    private static final double UIF_RATE = 0.01;
    private static final double UIF_CAP = 177.12;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your age: ");
        int age = sc.nextInt();

        // Modern arrow switch; both values are always set correctly
        System.out.print("Is your salary monthly or annual? (1 = monthly, 2 = annual): ");
        int salaryOption = sc.nextInt();

        System.out.print("Enter your gross salary: R");
        double salary = sc.nextDouble();

        double monthlyGross;
        double annualGross;

        // Modern arrow switch; both values are always set correctly
        switch (salaryOption) {
            case 1 -> {
                monthlyGross = salary;
                annualGross = salary * 12;
            }
            case 2 -> {
                annualGross = salary;
                monthlyGross = salary / 12;
            }
            default -> {
                System.out.println("Invalid option. Please run the program again.");
                sc.close();
                return;
            }
        }

        // Tax brackets: 2027 tax year (1 Mar 2026 – 28 Feb 2027), source: SARS
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
        } else if (annualGross <= 1878600) {                       // real SARS threshold
            tax = 259783 + (annualGross - 887000) * 0.41;
        } else {
            tax = 666339 + (annualGross - 1878600) * 0.45;          // fixed amount added
        }

        // Rebates reduce tax, never below zero
        double annualTax = Math.max(0, tax - PRIMARY_REBATE);
        if (age >= 65) {
            annualTax = Math.max(0, annualTax - SECONDARY_REBATE);
        }
        if (age >= 75) {                                            //tertiary rebate now used
            annualTax = Math.max(0, annualTax - TERTIARY_REBATE);
        }
        double monthlyTax = annualTax / 12;

        //UIF uses the real monthly figure, whichever option was chosen
        double monthlyUif = Math.min(monthlyGross * UIF_RATE, UIF_CAP);
        double annualUif = monthlyUif * 12;

        double monthlyTakeHome = monthlyGross - monthlyTax - monthlyUif;
        double annualTakeHome = annualGross - annualTax - annualUif;

        
        System.out.printf("%n===== Salary summary for %s =====%n", name);
        System.out.printf("%-12s %15s %15s%n", "", "Monthly", "Annual");
        System.out.printf("%-12s R%,14.2f R%,14.2f%n", "Gross", monthlyGross, annualGross);
        System.out.printf("%-12s R%,14.2f R%,14.2f%n", "Tax (PAYE)", monthlyTax, annualTax);
        System.out.printf("%-12s R%,14.2f R%,14.2f%n", "UIF", monthlyUif, annualUif);
        System.out.printf("%-12s R%,14.2f R%,14.2f%n", "Take-home", monthlyTakeHome, annualTakeHome);

        sc.close();
    }
}
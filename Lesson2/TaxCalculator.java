package Lesson2;

/*Exercise 2: Real SARS tax calculator

In lesson02/TaxCalculator.java, upgrade your salary calculator to use the real 2026/27 
tax brackets (1 March 2026 – 28 February 2027). According to SARS's official table, they are:

Annual taxable income (R)	Tax
1 – 245 100	18% of income
245 101 – 383 100	44 118 + 26% of the amount above 245 100
383 101 – 530 200	79 998 + 31% of the amount above 383 100
530 201 – 695 800	125 599 + 36% of the amount above 530 200
695 801 – 887 000	185 215 + 39% of the amount above 695 800
887 001 – ?	259 783 + 41% of the amount above 887 000
above ?	45% top bracket

For the 41% and 45% brackets, get the missing figures from the SARS page yourself. 
Reading the official spec instead of trusting someone's summary is a real developer skill.

Requirements:

Ask for name, age and monthly gross salary.
Use a switch to ask: 1 = monthly salary entered, 2 = annual salary entered. 
Convert to an annual amount if needed.
Calculate the annual tax with if / else if.
Subtract the primary rebate, which is R17,820 for everyone under 65. If the result is negative, the tax is R0, 
because you can't pay negative tax.
UIF is 1% of the monthly salary, capped at R177.12. Use an if or a ternary.
Print a neat summary: gross, tax, UIF, and take-home pay, both monthly and annual.

Test values:

R7,000/month → tax should be R0. The rebate covers it, since you only pay tax above 
about R99,000 a year.
R30,000/month → R360,000/year, which falls in the 26% bracket. 
Work out the expected tax on paper first, then check your program matches. 
Testing against hand-calculated results is how real developers verify their logic.

Bonus: if age is 65 or over, add the secondary rebate as well. 
Find its value on the same SARS page. */

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
package Lesson1;
/*Exercise 1: Salary calculator

Write SalaryCalculator.java that:

Asks for the user's name and monthly gross salary (a double).
Calculates the annual salary.
Deducts a flat 18% tax (store it as a final constant) and R177.12 UIF per month.
Prints the monthly take-home pay and the annual take-home pay, neatly formatted.

Bonus: look up System.out.printf("%.2f", value) to print exactly 2 decimal places.*/

import java.util.Scanner;
public class SalaryCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter your name: ");
        String name = sc.nextLine();

        System.out.println("Enter your monthly gross salary: ");
        double monthlyGrossSalary = sc.nextDouble();

        final double TAX_RATE = 0.18;
        final double UIF_DEDUCTION = 177.12;

        double monthlyTakeHomePay = monthlyGrossSalary - (monthlyGrossSalary * TAX_RATE) - UIF_DEDUCTION;
        double annualTakeHomePay = monthlyTakeHomePay * 12;

        System.out.printf(name + "'s monthly take-home pay is R%.2f%n", monthlyTakeHomePay);
        System.out.printf("And your annual take-home pay is R%.2f%n", annualTakeHomePay);
        sc.close();
    }
}
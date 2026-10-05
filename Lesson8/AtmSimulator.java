package Lesson8;


import java.util.Scanner;

public class AtmSimulator {
    public static void main(String[] args) {
        double balance = 2500.00;
        double withdrawnToday = 0;
        final double DAILY_LIMIT = 3000.00;

        Scanner sc = new Scanner(System.in);

        
        try {
            while (true) {
                printMenu();
                int choice = readInt(sc, "Enter your choice: ", 1, 4);
                switch (choice) {
                    case 1:
                        System.out.printf("Balance: R%,.2f%n", balance);
                        break;
                    case 2:
                        double depositAmount = readDouble(sc, "Enter deposit amount: R", -1000000, 1000000);
                        try {
                            balance = deposit(balance, depositAmount);
                            System.out.printf("Balance: R%,.2f%n", balance);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Deposit failed: " + e.getMessage());
                        }
                        break;
                    case 3:
                        double withdrawAmount = readDouble(sc, "Enter withdrawal amount: R", -1000000, 1000000);
                        try {
                            balance = withdraw(balance, withdrawAmount, withdrawnToday, DAILY_LIMIT);
                            withdrawnToday += withdrawAmount;
                            System.out.printf("Balance: R%,.2f%n", balance);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        } catch (InsufficientFundsException e) {
                            System.out.println(e.getMessage());
                        } 
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        return;
                }
            }

        } finally {
            System.out.println("Please take your card. Goodbye!");
            sc.close();
        }

    }

    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number from %d to %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("That's not a number. Try again.");
            }
        }
    }

    public static double readDouble(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number from %.2f to %.2f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("That's not a number. Try again.");
            }
        }
    }

    public static void printMenu() {
        System.out.println("===== ATM =====");
        System.out.println("1. Check balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Exit");
    }

    public static double deposit(double balance, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit must be more than R0");
        }
        if (amount > 50000) {
            throw new IllegalArgumentException("Deposits over R50,000 must be made at a branch");
        }
        return balance + amount;
    }

    public static double withdraw(double balance, double amount, double withdrawnToday, double dailyLimit)
            throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal must be more than R0");
        }
        if (amount % 50 != 0) {
            throw new IllegalArgumentException("This ATM only dispenses multiples of R50");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Balance: R%,.2f, requested: R%,.2f", balance, amount));
        }
        if (withdrawnToday + amount > dailyLimit) {
            double remaining = dailyLimit - withdrawnToday;
            throw new IllegalArgumentException(
                    String.format("Daily limit exceeded. You can still withdraw R%,.2f today", remaining));
        }
        return balance - amount;
    }

}

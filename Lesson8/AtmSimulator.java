package Lesson8;

/*Exercise 8: ATM simulator

Build an ATM that enforces bank rules using exceptions.

Files (in lesson08/)
InsufficientFundsException.java: the custom checked exception from section 8
AtmSimulator.java: the main program
Setup

In main:

java
double balance = 2500.00;
double withdrawnToday = 0;
final double DAILY_LIMIT = 3000.00;
===== ATM =====
1. Check balance
2. Deposit
3. Withdraw
4. Exit
Part A: input with exceptions
Use the new readInt from section 10 (exception-based) for the menu.
Write readDouble the same way, using Double.parseDouble and catching NumberFormatException.
Part B: deposit(double balance, double amount) → double

Return the new balance. Throw an IllegalArgumentException if:

amount <= 0 → "Deposit must be more than R0"
amount > 50000 → "Deposits over R50,000 must be made at a branch"

In main, call it inside try/catch and print "Deposit failed: " + e.getMessage() on failure. The balance must not change if the deposit fails.

Part C: withdraw(double balance, double amount, double withdrawnToday, double dailyLimit) → double

Return the new balance. Check, in this order:

amount <= 0 → IllegalArgumentException: "Withdrawal must be more than R0"
amount % 50 != 0 → IllegalArgumentException: "This ATM only dispenses multiples of R50"
amount > balance → InsufficientFundsException, with the message showing the balance and the requested amount (like section 8)
withdrawnToday + amount > dailyLimit → IllegalArgumentException: "Daily limit exceeded. You can still withdraw R1,000.00 today", with the remaining amount calculated

Because InsufficientFundsException is checked, the method needs throws InsufficientFundsException in its signature. In main, catch both exception types with separate catch blocks and different messages. After a successful withdrawal, update withdrawnToday in main.

Part D: finally

Wrap the whole menu loop in a try { ... } finally { ... }. The finally block prints "Please take your card. Goodbye!". Real ATMs always return your card, even when something goes wrong.

Test round
Action	Expected
Menu: abc	"That's not a number. Try again."
Check balance	R2,500.00
Deposit -100	Deposit failed: Deposit must be more than R0
Deposit 60000	Deposit failed: Deposits over R50,000 must be made at a branch
Deposit 500	Balance R3,000.00
Withdraw 75	This ATM only dispenses multiples of R50
Withdraw 5000	Insufficient funds. Balance: R3,000.00, requested: R5,000.00
Withdraw 2000	Balance R1,000.00
Deposit 2000	Balance R3,000.00
Withdraw 1500	Daily limit exceeded. You can still withdraw R1,000.00 today
Withdraw 1000	✅ Balance R2,000.00 (exactly reaches the limit, which is allowed)
Exit	Final balance, then "Please take your card. Goodbye!"
Bonus

Create a second custom exception, DailyLimitExceededException, and use it for rule 4 instead of IllegalArgumentException. Then your catch blocks can respond to each business rule separately.

Same process as before: A → B → C → D, testing as you go. Start with the exception class and Part A, and send me your code whenever you finish a part or get stuck. */
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

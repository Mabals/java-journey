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

    // File: InsufficientFundsException.java
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }

    
}
    


package Lesson11;

import java.io.File;

/*Here's what to improve, most important first.

1. 🐛 The constructor skips the category validation

setCategory rejects a blank category, but the constructor sets this.category = category; without checking. So this works, when it shouldn't:

java
new Transaction("2026-10-05", "Salary", 25000, "");   // ✅ allowed. Blank category!

Validation must happen on every route into the object. Here, there are two routes: the constructor and the setter. The neatest fix is to have the constructor call setCategory(category) instead of assigning the field directly. Then the rule lives in one place, and both routes use it.

2. 🧹 Who decides the category?

You added a category parameter to deposit and withdraw. It works, but think about what it allows:

java
thato.withdraw(500, "Groceries", "Income");   // a withdrawal labelled "Income"

The caller can now label a transaction inconsistently with what actually happened. The account can't stop it. The exercise had the account set the category itself ("Deposit" / "Withdrawal"), because the account knows which operation is happening. That's encapsulation: the object decides what it's responsible for.

Your version isn't wrong. Letting users categorise their spending is a real PocketRand feature. But that's what setCategory is for, after the transaction is recorded. My suggestion: remove the category parameter from deposit/withdraw, have the account set "Deposit"/"Withdrawal", and keep setCategory for re-categorising.

3. 🐛 The R50,000 deposit rule is missing

deposit only checks for ≤ 0. Add the second guard: over 50,000 → "Deposits over R50,000 must be made at a branch".

4. 🧹 Make the insufficient-funds message useful

"Insufficient funds for withdrawal" doesn't tell the user how much they have. Use String.format to include the balance and the requested amount, as in your Lesson 8 ATM:
"Insufficient funds. Balance: R16,000.00, requested: R50,000.00"

5. 🧹 Statement layout
Transaction.toString() doesn't include the date (Step 1 asked for it). And getStatement() prints a header row starting with Date, which doesn't match the lines below it. Add the date, and use widths (%-20s for the description, %,10.2f for the amount) so the columns line up under the header.
Use %,.2f for money everywhere, including the balance, so you get R16,000.00.
6. 🧹 Small tidy-ups
java.time.LocalDate is written out in full twice. Add import java.time.LocalDate; and just write LocalDate.now().
Did you try step 8 (temporarily adding thato.balance = 1_000_000;)? 
Do it once to see the "balance has private access" error yourself, then delete the line.*/

import java.util.ArrayList;
import java.util.List;


public class BankAccount {
    private static int accountsOpened = 0; // shared counter for generating account numbers
    private final int accountNumber; // final: never changes after creation
    private final String holderName; // final: never changes after creation
    private double balance; // NOT final: changes as deposits/withdrawals occur
    private final List<Transaction> history; // final: the list itself never gets replaced


    public BankAccount(String holderName) {
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("Holder name is required");
        }
        BankAccount.accountsOpened++;
        this.accountNumber = 1000 + BankAccount.accountsOpened;
        this.holderName = holderName;
        this.balance = 0.0;
        this.history = new ArrayList<>();
        

    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getHistory() {
        return List.copyOf(history); // return a read-only copy of the history
    }

    public static int getAccountsOpened() {
        return accountsOpened;
    }


    public void deposit(double amount, String description, String category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be more than R0");
        }
        balance += amount;
        history.add(new Transaction(java.time.LocalDate.now().toString(), description, amount, category));
    }

    public void withdraw(double amount, String description, String category) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be more than R0");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        balance -= amount;
        history.add(new Transaction(java.time.LocalDate.now().toString(), description, -amount, category));
    }

    public void transferTo(BankAccount other, double amount) throws InsufficientFundsException {
        if (other == null) {
            throw new IllegalArgumentException("Target account is required");
        }
        if (other == this) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be more than R0");
        }
        this.withdraw(amount, "Transfer to account " + other.getAccountNumber(), "Transfer");
        other.deposit(amount, "Transfer from account " + this.getAccountNumber(), "Transfer");
    }

    public String getStatement() {
        StringBuilder statement = new StringBuilder();
        statement.append(String.format("Account %d (%s): Balance R%.2f%n", accountNumber, holderName, balance));
        statement.append("Date       | Description | Amount | Category\n");
        statement.append("---------------------------------------------\n");
        for (Transaction transaction : history) {
            statement.append(transaction.toString()).append("\n");
        }
        return statement.toString();
    }

    

}
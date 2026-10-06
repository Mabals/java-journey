package Lesson13;


import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount {
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
    /*
     * Below the getters Add a protected method getAvailableFunds() that returns the
     * balance
     * Below that Add a public method getAccountType() that returns "Account"
     */

    protected double getAvailableFunds() {
        return balance;
    }

    public abstract String getAccountType();

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
        if (amount > getAvailableFunds()) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Available: R" + String.format("%.2f", getAvailableFunds())
                            + ", requested: R" + String.format("%.2f", amount));

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
        statement.append(String.format("===== %s Account %d: %s =====%n", getAccountType(), accountNumber, holderName));
        statement.append("Date       | Description | Amount | Category\n");
        statement.append("---------------------------------------------\n");
        for (Transaction transaction : history) {
            statement.append(transaction.toString()).append("\n");
        }
        return statement.toString();
    }

}
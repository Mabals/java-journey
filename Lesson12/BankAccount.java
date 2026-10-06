package Lesson12;
/*Exercise 12: Savings and cheque accounts

You'll extend your Lesson 11 BankAccount with two account types, each with different withdrawal rules.

The bank's rules
	SavingsAccount	ChequeAccount
Special feature	Earns interest	Has an overdraft facility
Withdrawal rule	Must always keep R100 minimum in the account	Can go below zero, up to the overdraft limit (e.g. R2,000)
Money available to withdraw	balance − R100	balance + overdraft limit
Extra action	addInterest(): adds one month's interest	chargeMonthlyFee(): deducts the R65 monthly fee
The design (the clever part from section 8)

The only real difference between the accounts is how much money is available. So:

BankAccount gets a new protected method getAvailableFunds(), which returns the balance by default.
BankAccount.withdraw() checks the amount against getAvailableFunds() instead of the balance directly.
Each child overrides getAvailableFunds() with its own rule.

Then withdraw() is written once, in the parent, and still behaves correctly for every account type. The balance stays private. Children never touch it directly. They only change the answer to "how much is available?"

The structure
lesson12/
├── InsufficientFundsException.java   ← copy from Lesson 11 (unchanged)
├── Transaction.java                  ← copy from Lesson 11 (unchanged)
├── BankAccount.java                  ← copy from Lesson 11, small changes (Step 1)
├── SavingsAccount.java               ← NEW: Step 2
├── ChequeAccount.java                ← NEW: Step 3
└── AccountsDemo.java                 ← NEW: Step 4

Check that the package lines match across all six files (or remove them from all).

Step 1: Prepare the parent

📄 File: BankAccount.java

Where in the file	Change
Below the getters	Add a protected method getAvailableFunds() that returns the balance
Below that	Add a public method getAccountType() that returns "Account"
Inside withdraw, the insufficient-funds check	Compare the amount with getAvailableFunds() instead of balance. Change the message to show "Available: R..." instead of the balance.
Inside getStatement, the heading line	Include getAccountType(), e.g. ===== Savings Account 1001: Thato Khonkhe =====

Why: these two methods are the hooks the children will override. Because withdraw and getStatement call them, the children's versions run automatically (section 8).
Test: your Lesson 11 BankDemo should still give the same results. Nothing about a plain account has changed.

Step 2: SavingsAccount

📄 File: SavingsAccount.java

Where in the file	What to write
Class line	SavingsAccount extends BankAccount
Fields	A private static final minimum balance of 100. A private final annual interest rate (e.g. 0.06 for 6%).
Constructor	Takes the holder name and the annual rate. First line: super(...) with the holder name. Then validate the rate (not negative) and store it.
Getter	getInterestRate()
Override	getAvailableFunds() → the balance minus the minimum balance (use the parent's getBalance(), since the balance is private)
Override	getAccountType() → "Savings"
New method	addInterest(): calculate balance × annual rate ÷ 12, and if it's more than 0, call the inherited deposit with the description "Interest"

Why addInterest calls deposit: it reuses the parent's validation and its history recording. Interest then appears on the statement like any other deposit, with no duplicated code.

Step 3: ChequeAccount

📄 File: ChequeAccount.java

Where in the file	What to write
Class line	ChequeAccount extends BankAccount
Fields	A private static final monthly fee of 65. A private final overdraft limit.
Constructor	Takes the holder name and the overdraft limit. super(...) first, then validate the limit (not negative) and store it.
Getter	getOverdraftLimit()
Override	getAvailableFunds() → the balance plus the overdraft limit
Override	getAccountType() → "Cheque"
New method	chargeMonthlyFee(): call the inherited withdraw with the fee and the description "Monthly fee". It needs throws InsufficientFundsException, because withdraw can throw.
Step 4: Test everything

📄 File: AccountsDemo.java, in main

Put steps 1–7 inside one try block, catching InsufficientFundsException, except steps 3 and 6, which are expected to fail. Give each of those its own try/catch that prints "Declined: " + the message.

#	Action	Expected
1	Create a SavingsAccount ("Thato Khonkhe", 0.06) and a ChequeAccount ("Thato Khonkhe", 2000)	Account numbers 1001 and 1002 (the static counter in the parent is shared by both children)
2	Savings: deposit 10,000	Balance R10,000.00
3	Savings: withdraw 9,950	Declined: Insufficient funds. Available: R9,900.00, requested: R9,950.00
4	Savings: withdraw 2,000	Balance R8,000.00
5	Savings: addInterest()	8,000 × 0.06 ÷ 12 = R40.00 → balance R8,040.00
6	Cheque: deposit 1,000, then withdraw 2,500	✅ Allowed (available: 1,000 + 2,000 = 3,000) → balance −R1,500.00
7	Cheque: withdraw 1,000	Declined: Available: R500.00, requested: R1,000.00
8	Cheque: chargeMonthlyFee()	Balance −R1,565.00

Then polymorphism:

#	Action	Expected
9	Put both accounts in a List<BankAccount>, loop over it, and print each getStatement()	Each heading shows its own type ("Savings" / "Cheque")
10	In the same loop, add up getBalance()	Total: R6,475.00 (8,040 − 1,565)
11	In the loop, use instanceof: if it's a SavingsAccount, print its interest rate	"Savings interest rate: 6.0%"

Finally, check what's blocked: temporarily try calling addInterest() on a variable declared as BankAccount (not SavingsAccount). Read the compile error and explain it to yourself using section 7's table. Then delete the line.

Bonus
A maximum of 2 free withdrawals a month on savings: override withdraw in SavingsAccount. Count the withdrawals, then call super.withdraw(...) so the parent still does the real work and validation.
Make deposit final in BankAccount, so no future account type can skip its validation (section 9). Try to override it in ChequeAccount and read the error.*/

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
    /*
     * Below the getters Add a protected method getAvailableFunds() that returns the
     * balance
     * Below that Add a public method getAccountType() that returns "Account"
     */

    protected double getAvailableFunds() {
        return balance;
    }

    public String getAccountType() {
        return "Account";
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
package Lesson12;

import java.util.ArrayList;
import java.util.List;

public class AccountsDemo {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("Thato Khonkhe", 0.06);
        ChequeAccount cheque = new ChequeAccount("Thato Khonkhe", 2000);

        System.out.printf("Savings account number: %d%n", savings.getAccountNumber());
        System.out.printf("Cheque account number: %d%n", cheque.getAccountNumber());

        try {
            savings.deposit(10000, "Initial deposit", "Deposit");

            // This withdrawal should be declined: savings must keep R100.
            try {
                savings.withdraw(9950, "Withdrawal", "Withdrawal");
            } catch (InsufficientFundsException e) {
                System.out.println("Declined: " + e.getMessage());
            }

            savings.withdraw(2000, "Withdrawal", "Withdrawal");
            savings.addInterest();
            System.out.printf("Savings balance: R%.2f%n", savings.getBalance());

            cheque.deposit(1000, "Initial deposit", "Deposit");
            // This is allowed because the overdraft makes R3,000 available.
            cheque.withdraw(2500, "Withdrawal", "Withdrawal");

            // This withdrawal should be declined: only R500 remains available.
            try {
                cheque.withdraw(1000, "Withdrawal", "Withdrawal");
            } catch (InsufficientFundsException e) {
                System.out.println("Declined: " + e.getMessage());
            }

            cheque.chargeMonthlyFee();
            System.out.printf("Cheque balance: R%.2f%n", cheque.getBalance());

            List<BankAccount> accounts = new ArrayList<>();
            accounts.add(savings);
            accounts.add(cheque);

            double totalBalance = 0;
            for (BankAccount account : accounts) {
                System.out.println(account.getStatement());
                totalBalance += account.getBalance();

                if (account instanceof SavingsAccount) {
                    SavingsAccount savingsAccount = (SavingsAccount) account;
                    System.out.printf(
                            "Savings interest rate: %.1f%%%n",
                            savingsAccount.getInterestRate() * 100);
                }
            }

            System.out.printf("Total balance: R%.2f%n", totalBalance);
        } catch (InsufficientFundsException e) {
            System.out.println("Unexpected withdrawal failure: " + e.getMessage());
        }
    }
}
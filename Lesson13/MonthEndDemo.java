package Lesson13;

import java.util.List;

public class MonthEndDemo {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("Thato Khonkhe", 0.06);
        ChequeAccount cheque = new ChequeAccount("Thato Khonkhe", 2000);

        savings.deposit(12000, "Salary", "Deposit");
        cheque.deposit(5000, "Salary", "Deposit");

        List<BankAccount> accounts = List.of(savings, cheque);
        runMonthEnd(accounts);

        for (BankAccount account : accounts) {
            System.out.printf("%s %d balance: R%,.2f%n", 
                account.getAccountType(), account.getAccountNumber(), account.getBalance());
        }

        StatementExporter exporter = new TextStatementExporter();
        showExport(savings, exporter);
    }

    public static void runMonthEnd(List<BankAccount> accounts) {
        for (BankAccount account : accounts) {
            if (account instanceof InterestBearing interestAccount) {
                interestAccount.addInterest();
                System.out.printf("Interest added to %d at %.1f%%%n",
                        account.getAccountNumber(), interestAccount.getInterestRate() * 100);
            }
            if (account instanceof FeeCharging feeAccount) {
                try {
                    feeAccount.chargeMonthlyFee();
                    System.out.printf("Fee of R%.2f charged to %d%n",
                            feeAccount.getMonthlyFee(), account.getAccountNumber());
                } catch (InsufficientFundsException e) {
                    System.out.println("Could not charge fee: " + e.getMessage());
                }
            }
        }
    }

    public static void showExport(BankAccount account, StatementExporter exporter) {
        String fileName = "statement-" + account.getAccountNumber() + "." + exporter.getFileExtension();
        System.out.println("Exporting " + fileName);
        System.out.println(exporter.export(account));
    }
}

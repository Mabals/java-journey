package Lesson11;

public class BankDemo {
    public static void main(String[] args) {
        BankAccount thato = new BankAccount("Thato Khonkhe");
        BankAccount lerato = new BankAccount("Lerato Mokoena");

        try {
            thato.deposit(25000, "Salary", "Income");
            thato.withdraw(7500, "Rent", "Expense");
            lerato.deposit(3000, "Salary", "Income");
            thato.transferTo(lerato, 1500);
        } catch (InsufficientFundsException e) {
            System.out.println("Declined: " + e.getMessage());
        }

        try {
            thato.withdraw(50000, "New car", "Expense");
        } catch (InsufficientFundsException e) {
            System.out.println("Declined: " + e.getMessage());
        }

        try {
            thato.deposit(-100, "Invalid deposit", "Income");
        } catch (IllegalArgumentException e) {
            System.out.println("Declined: " + e.getMessage());
        }

        System.out.println(thato.getStatement());
        System.out.println(lerato.getStatement());
        System.out.println("Accounts opened: " + BankAccount.getAccountsOpened());

        try {
            thato.getHistory().clear();
        } catch (UnsupportedOperationException e) {
            System.out.println("History is read-only");
        }
    }
}

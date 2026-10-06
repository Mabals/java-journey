package Lesson12;

public class ChequeAccount extends BankAccount {
    private static final double MONTHLY_FEE = 65.0;
    private final double overdraftLimit;

    public ChequeAccount(String holderName, double overdraftLimit) {
        super(holderName);

        if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }

        this.overdraftLimit = overdraftLimit;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    protected double getAvailableFunds() {
        return getBalance() + overdraftLimit;
    }

    @Override
    public String getAccountType() {
        return "Cheque";
    }

    public void chargeMonthlyFee() throws InsufficientFundsException {
        withdraw(MONTHLY_FEE, "Monthly fee", "Fee");
    }
}

package Lesson13;

public class SavingsAccount extends BankAccount implements InterestBearing {
    private static final double MINIMUM_BALANCE = 100.0;
    private final double annualInterestRate;

    public SavingsAccount(String holderName, double annualInterestRate) {
        super(holderName);
        if (annualInterestRate < 0) {
            throw new IllegalArgumentException("Annual interest rate cannot be negative");
        }
        this.annualInterestRate = annualInterestRate;
    }

    @Override 
    public double getInterestRate() {
        return annualInterestRate;
    }

    @Override
    protected double getAvailableFunds() {
        return getBalance() - MINIMUM_BALANCE;
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

    @Override 
    public void addInterest() {
        double interest = getBalance() * annualInterestRate / 12;
        if (interest > 0) {
            deposit(interest, "Interest", "Interest");
        }
    }
}

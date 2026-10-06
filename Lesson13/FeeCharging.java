package Lesson13;

public interface FeeCharging {
    double getMonthlyFee();
    void chargeMonthlyFee() throws InsufficientFundsException;
}

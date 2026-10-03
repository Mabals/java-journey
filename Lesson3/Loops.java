public class Loops {
    public static void main(String[] args) {
        double balance = 0;
        double monthlyDeposit = 1000;

        for (int month = 1; month <= 12; month++) {
            balance += monthlyDeposit;
            System.out.printf("Month %2d: R%,.2f%n", month, balance);
        }
    }
}

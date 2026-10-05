package Lesson11;

public class LoyaltyDemo {
    public static void main(String[] args) {
        LoyaltyCard thato = new LoyaltyCard("Thato");
        LoyaltyCard lerato = new LoyaltyCard("Lerato");

        thato.earn(455.50);
        thato.earn(120.00);
        lerato.earn(89.99);
        thato.redeem(30);

        System.out.println(thato);
        System.out.println(lerato);
        System.out.println(thato.getActivity());
        System.out.println("Cards issued: " + LoyaltyCard.getCardsIssued());
        System.out.println("Rands per point: " + LoyaltyCard.RANDS_PER_POINT);
    }
}

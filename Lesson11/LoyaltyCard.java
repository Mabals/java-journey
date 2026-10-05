package Lesson11;

import java.util.ArrayList;
import java.util.List;

public class LoyaltyCard {

    // ═══════════ 1. STATIC: shared by ALL cards (one copy for the whole class) ═══════════
    public static final int RANDS_PER_POINT = 10;    // a constant: 1 point per R10 spent
    private static int cardsIssued = 0;              // a counter shared by every card

    // ═══════════ 2. INSTANCE FIELDS: each card's OWN data (all private) ═══════════
    private final int cardNumber;                    // final: can never change after creation
    private final String owner;                      // final: can never change after creation
    private int points;                              // NOT final: changes as points are earned/used
    private final List<String> activity = new ArrayList<>();

    // ═══════════ 3. CONSTRUCTOR: validate first, then set up ═══════════
    public LoyaltyCard(String owner) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner name is required");
        }
        cardsIssued++;                               // update the SHARED counter
        this.cardNumber = 5000 + cardsIssued;        // each card gets the next number
        this.owner = owner;
        this.points = 0;
    }

    // ═══════════ 4. GETTERS: read-only access to private data ═══════════
    public int getCardNumber() {
        return cardNumber;
    }

    public String getOwner() {
        return owner;
    }

    public int getPoints() {
        return points;
    }

    public List<String> getActivity() {
        return List.copyOf(activity);                // a read-only COPY, not the real list
    }

    // ═══════════ 5. BUSINESS METHODS: the ONLY ways to change points ═══════════
    public void earn(double amountSpent) {
        if (amountSpent <= 0) {
            throw new IllegalArgumentException("Amount spent must be more than R0");
        }
        int earned = (int) (amountSpent / RANDS_PER_POINT);
        points += earned;
        activity.add(String.format("Earned %d points (spent R%.2f)", earned, amountSpent));
    }

    public void redeem(int pointsToUse) {
        if (pointsToUse <= 0) {
            throw new IllegalArgumentException("Points must be more than 0");
        }
        if (pointsToUse > points) {
            throw new IllegalArgumentException("Only " + points + " points available");
        }
        points -= pointsToUse;
        activity.add("Redeemed " + pointsToUse + " points");
    }

    // ═══════════ 6. STATIC METHOD: about ALL cards, not one card ═══════════
    public static int getCardsIssued() {
        return cardsIssued;
    }

    // ═══════════ 7. toString() ═══════════
    @Override
    public String toString() {
        return String.format("Card %d (%s): %d points", cardNumber, owner, points);
    }
}
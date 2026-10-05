package Lesson7;


import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class SpazaShop {
    public static void main(String[] args) {
        Map<String, Double> prices = new LinkedHashMap<>();
        prices.put("bread", 18.99);
        prices.put("milk", 24.50);
        prices.put("eggs", 45.00);
        prices.put("maize meal", 89.99);
        prices.put("sugar", 52.50);

        Map<String, Integer> stock = new LinkedHashMap<>();
        stock.put("bread", 10);
        stock.put("milk", 8);
        stock.put("eggs", 5);
        stock.put("maize meal", 3);
        stock.put("sugar", 6);

        List<String> cart = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = readInt(sc, "Enter your choice: ", 1, 6);
            switch (choice) {
                case 1:
                    printProducts(prices, stock);
                    break;
                case 2:
                    addToCart(sc, prices, stock, cart);
                    break;
                case 3:
                    printCart(prices, cart);
                    break;
                case 4:
                    removeFromCart(sc, stock, cart);
                    break;
                case 5:
                    checkout(prices, cart);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
            }

        }

        
        
    }

    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                if (value >= min && value <= max) {
                    sc.nextLine(); // consume the leftover newline
                    return value; // valid: return ends the loop AND the method
                }
                System.out.printf("Please enter a number from %d to %d.%n", min, max);
            } else {
                System.out.println("That's not a number. Try again.");
                sc.next();
            }
        }
    }

    public static void printProducts(Map<String, Double> prices, Map<String, Integer> stock) {
        System.out.printf("%-15s %-10s %-10s%n", "Product", "Price", "Stock");
        for (String product : prices.keySet()) {
            double price = prices.get(product);
            int quantity = stock.get(product);
            System.out.printf("%-15s %-10.2f %-10d%n", product, price, quantity);
        }


    }

    public static void addToCart(Scanner sc, Map<String, Double> prices, Map<String, Integer> stock, List<String> cart) {
        System.out.print("Enter product name to add to cart: ");
     
        String product = sc.nextLine().trim().toLowerCase();

        if (!prices.containsKey(product)) {
            System.out.println("We don't sell that.");
            return;
        }

        int quantity = stock.get(product);
        if (quantity <= 0) {
            System.out.println("Sorry, out of stock.");
            return;
        }

        cart.add(product);
        stock.put(product, quantity - 1);
        System.out.println(product + " added to cart.");
    }

    public static double printCart(Map<String, Double> prices, List<String> cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return 0.0;
        }

        Map<String, Integer> itemCount = new LinkedHashMap<>();
        for (String item : cart) {
            itemCount.put(item, itemCount.getOrDefault(item, 0) + 1);
        }

        double total = 0.0;
        System.out.printf("%-15s %-10s %-10s%n", "Item", "Qty", "Total");
        for (String item : itemCount.keySet()) {
            int qty = itemCount.get(item);
            double price = prices.get(item);
            double itemTotal = price * qty;
            total += itemTotal;
            System.out.printf("%-15s %-10d %-10.2f%n", item, qty, itemTotal);
        }
        System.out.printf("%-15s %-10s %-10.2f%n", "", "Total:", total);
        return total;
    }

    public static void removeFromCart(Scanner sc, Map<String, Integer> stock, List<String> cart) {
        System.out.print("Enter product name to remove from cart: ");
        
        String product = sc.nextLine().trim().toLowerCase();

        if (!cart.contains(product)) {
            System.out.println("That's not in your cart.");
            return;
        }

        cart.remove(product);
        stock.put(product, stock.get(product) + 1);
        System.out.println(product + " removed from cart.");
    }

    public static void checkout(Map<String, Double> prices, List<String> cart) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout.");
            return;
        }
        double total = printCart(prices, cart);
        double vat = total * 15 / 115;
        System.out.printf("VAT included (15%%): R%.2f%n", vat);
        System.out.printf("Total amount to pay: R%.2f%n", total);
        System.out.println("Thank you for shopping with us!");
        cart.clear();
    }

    public static void printMenu() {
        System.out.println("\n===== Spaza Shop Till =====");
        System.out.println("1. View products");
        System.out.println("2. Add item to cart");
        System.out.println("3. View cart");
        System.out.println("4. Remove item from cart");
        System.out.println("5. Checkout");
        System.out.println("6. Exit");
    }

    


}

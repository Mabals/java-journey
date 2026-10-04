package Lesson7;

/*Exercise 7: Spaza shop till system

Build a small till for a spaza shop. It practises both collections, plus everything 
from earlier lessons.

Setup

Create lesson07/SpazaShop.java. In main, create the shop's data. Use LinkedHashMap so 
products display in the order you add them:

java
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

The cart is a list of product names, and an item appears once for each unit bought. 
For example, [bread, milk, bread] means 2 bread and 1 milk.

Copy your readInt method in too. Remember that you need import java.util.*; 
(the * imports everything in java.util, which is fine for exercises).

The menu
===== Spaza Shop Till =====
1. View products
2. Add item to cart
3. View cart
4. Remove item from cart
5. Checkout
6. Exit
Part A: printProducts(prices, stock)

Loop over the products and print a neat table. Use entrySet() or keySet():

Product          Price   Stock
bread           R18.99      10
milk            R24.50       8
eggs            R45.00       5
maize meal      R89.99       3
sugar           R52.50       6
Part B: addToCart(sc, prices, stock, cart)
Ask for the product name. Read it with nextLine(), then trim() and toLowerCase() it so "Bread " 
still works.
If the product doesn't exist (containsKey), print "We don't sell that."
If its stock is 0, print "Sorry, out of stock."
Otherwise, add it to the cart and reduce the stock by 1.

⚠️ The Scanner trap is back. Your menu uses readInt (which uses nextInt), and here you read 
text with nextLine(). The leftover Enter will make the product name come out empty. 
You've seen the fix before, in Lesson 3.

💡 Maps hold references (Lesson 5, section 8). When addToCart changes stock or cart, it 
changes the same map and list that main has. So the method doesn't need to return anything.

Part C: printCart(prices, cart)

Use the counting pattern (section 13) to group the cart, so [bread, milk, bread] becomes 
bread → 2, milk → 1. Then print:

Item            Qty      Total
bread             2     R37.98
milk              1     R24.50
                  Total: R62.48

If the cart is empty, print "Your cart is empty." instead.

Tip: make printCart return the total as a double. You'll reuse it at checkout.

Part D: removeFromCart(sc, stock, cart)

Ask for a product name. If it's in the cart, remove one of it and add 1 back to stock. 
Otherwise, print "That's not in your cart." (Which remove do you need: by index, or by value?)

Part E: checkout(prices, cart)

Print the cart (reuse printCart), then the VAT. In South Africa, shop prices already include 
15% VAT, so the VAT portion is:

VAT = total × 15 / 115

For a R62.48 total, that's R8.15. Print it as "VAT included (15%): R8.15", then thank the 
customer and empty the cart with clear().

Test round
View products
Add Bread, milk, bread, and rice (→ "We don't sell that.")
View the cart → bread ×2, milk ×1, total R62.48
View products → bread stock is now 8, milk 7
Remove bread → cart total R43.49, bread stock back to 9
Add maize meal 4 times → the 4th time says "Sorry, out of stock."
Checkout → the receipt shows VAT, and the cart is empty afterwards
Bonus
Let the user choose a quantity when adding (e.g. 3 bread at once), and don't allow more than the stock.
At exit, print the day's total sales across all checkouts.

Same process as before: A → B → C → D → E, testing after each part. Start with the menu loop and Part A,
 and send me your code whenever you finish a part or get stuck.
 */
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

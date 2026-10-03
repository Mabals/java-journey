package Lesson1;
import java.util.Scanner;

public class Greeting {
    public static void main(String [] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("Enter your age: ");
        int age = sc.nextInt();

        System.out.print("Are you employed? (true/false): ");
        boolean isEmployed = sc.nextBoolean();

        System.out.println("Hello, " + name + "! You are " + age 
        + " years old and it is " + isEmployed + " that you are employed.");
        sc.close();




    }
}
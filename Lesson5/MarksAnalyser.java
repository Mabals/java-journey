package Lesson5;

import java.util.Scanner;

public class MarksAnalyser {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int numStudents = readInt(sc, "Enter the number of students: ", 1, 1000);
        int[] marks = new int[numStudents];

        for (int i = 0; i < numStudents; i++) {
            marks[i] = readInt(sc, "Mark for student " + (i + 1) + ": ", 0, 100);
        }

        // Calculate average, highest, lowest, and pass count
        int sum = 0;
        int highest = marks[0];
        int lowest = marks[0];
        int passCount = 0;

        for (int mark : marks) {
            sum += mark;
            if (mark > highest) highest = mark;
            if (mark < lowest) lowest = mark;
            if (mark >= 50) passCount++;
        }

        double average = (double) sum / numStudents;

        // Print results
        System.out.println("\nMarks: ");
        for (int i = 0; i < numStudents; i++) {
            System.out.println("Student " + (i + 1) + ": " + marks[i]);
        }
        System.out.printf("Average: %.2f%n", average);
        System.out.println("Highest: " + highest);
        System.out.println("Lowest: " + lowest);
        System.out.println("Pass count: " + passCount);

        sc.close();

    }

    

    public static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                if (value >= min && value <= max) {
                    return value; // valid: return ends the loop AND the method
                }
                System.out.printf("Please enter a number from %d to %d.%n", min, max);
            } else {
                System.out.println("That's not a number. Try again.");
                sc.next();
            }
        }
    }

    public static double readDouble(Scanner sc, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextDouble()) {
                double value = sc.nextDouble();
                if (value >= min && value <= max) {
                    return value; // valid: return ends the loop AND the method
                }
                System.out.printf("Please enter a number from %.2f to %.2f.%n", min, max);
            } else {
                System.out.println("That's not a number. Try again.");
                sc.next();
            }
        }
    }
}

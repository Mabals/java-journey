package Lesson4;

public class WarmUps {
    public static void main(String[] args) {
        System.out.println("isEven(4) = " + isEven(4)); // Expected: true
        System.out.println("isEven(7) = " + isEven(7)); // Expected: false
        System.out.println("max(3.5, 9.1) = " + max(3.5, 9.1)); // Expected: 9.1
        System.out.println("monthlyToAnnual(30000) = " + monthlyToAnnual(30000)); // Expected: 360000.0
        System.out.print("printLine(10) = ");
        printline(10); // Expected: ==========
        System.out.println("grade(80) = " + grade(80)); // Expected: Distinction
        System.out.println("grade(55) = " + grade(55)); // Expected: Pass
        System.out.println("grade(30) = " + grade(30)); // Expected: Fail
    }

    public static boolean isEven(int n) {
        if (n % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static double max(double a, double b) {
        if (a > b) {
            return a;
        }else {
            return b;
        }
    }

    public static double monthlyToAnnual(double monthly) {
        return monthly * 12;
    }

    public static void printline(int Length) {
        for (int i = 0; i < Length; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    public static String grade(int mark) {
        if (mark >= 75) {
            return "Distinction";
        } else if (mark >= 50) {
            return "Pass";
        } else {
            return "Fail";
        }
    }


}
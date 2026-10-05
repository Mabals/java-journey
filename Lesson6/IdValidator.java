package Lesson6;
import java.util.Scanner;

public class IdValidator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String again;

        do {
            String id = readValidId(sc);
            printIdDetails(id);
            again = readYesNo(sc, "\nCheck another? (y/n): ");
        } while (again.equalsIgnoreCase("y"));

        System.out.println("Goodbye!");

        sc.close();

    }

    public static String readValidId(Scanner sc) {
        String id;
        while (true) {
            System.out.print("Enter an SA ID number: ");
            id = sc.nextLine().trim();
            if (id.length() != 13) {
                System.out.println("Must be exactly 13 digits");
                continue;
            }
            if (!isAllDigits(id)) {
                System.out.println("Must contain only digits");
                continue;
            }
            if (!isValidChecksum(id)) {
                System.out.println("Invalid checksum");
                continue;
            }
            return id; // valid ID, return it
        }
    }

    public static void printIdDetails(String id) {
        System.out.println("\n===== ID number details =====");
        System.out.printf("ID number:     %s%n", maskId(id));
        System.out.printf("Date of birth: %s%n", getDateOfBirth(id));
        System.out.printf("Gender:        %s%n", getGender(id));
        System.out.printf("Citizenship:   %s%n", getCitizenship(id));
        System.out.println("Checksum:      Valid");

    }

    public static String readYesNo(Scanner sc, String prompt) {
        String choice;
        while (true) {
            System.out.print(prompt);
            choice = sc.nextLine().trim();
            if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n")) {
                return choice;
            } else {
                System.out.println("Please enter 'y' or 'n'.");
            }   

        }

    }

    public static boolean isAllDigits(String id) {
        for (int i = 0; i < id.length(); i++) {
            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getDateOfBirth(String id) {
        String year = id.substring(0, 2);
        String month = id.substring(2, 4);
        String day = id.substring(4, 6);
        int yearInt = Integer.parseInt(year);
        if (yearInt > 26) {
            year = "19" + year;
        } else {
            year = "20" + year; 
        }
        return year + "-" + month + "-" + day;
    }

    public static String getGender(String id) {
        int genderCode = Integer.parseInt(id.substring(6, 10));
        if (genderCode >= 5000) {
            return "Male";
        }else {
            return "Female";
        }
    }

    public static String getCitizenship(String id) {
        char citizenshipCode = id.charAt(10);
        if (citizenshipCode == '0') {
            return "SA citizen";
        } else {
            return "Permanent resident";
        }
    }

    public static boolean isValidChecksum(String id) {
        int sum = 0;
        for (int i = 0; i < id.length(); i++) {
            int digit = id.charAt(i) - '0'; // convert char to int
            if (i % 2 == 1) { // odd index
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        return sum % 10 == 0;
    }

    public static String maskId(String id) {
        StringBuilder masked = new StringBuilder();
        masked.append(id.substring(0, 6));
        for (int i = 6; i < id.length(); i++) {
            masked.append('*');
        }
        return masked.toString();
    }

    
}

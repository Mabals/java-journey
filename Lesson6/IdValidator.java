package Lesson6;
import java.util.Scanner;
/*Exercise 6: South African ID number validator

This is a real-world problem. Banks, insurers and government systems validate SA ID numbers every day.

How a 13-digit SA ID number is structured
8 0 0 1 0 1   5 0 0 9   0   8   7
└─ YYMMDD ─┘  └─SSSS─┘  C   A   Z
Part	Index	Meaning
YYMMDD	0–5	Date of birth (80-01-01 = 1 January 1980)
SSSS	6–9	Gender: 0000–4999 = female, 5000–9999 = male
C	10	Citizenship: 0 = SA citizen, 1 = permanent resident
A	11	Usually 8 (historical, so ignore it)
Z	12	Check digit, calculated from the other 12 (Part C)

⚠️ Use only the test numbers below. Never put a real person's ID number in your code or on GitHub. 
It's personal information protected by POPIA.

Target output
Enter an SA ID number: 8001015009087

===== ID number details =====
ID number:     800101*******
Date of birth: 1980-01-01
Gender:        Male
Citizenship:   SA citizen
Checksum:      Valid

Check another? (y/n): n
Goodbye!
Part A: Basic validation

Create lesson06/IdValidator.java. Write these methods:

isAllDigits(String id) → boolean: loop through every character and use Character.isDigit. 
Return false as soon as you find a non-digit, and true after the loop.
In main: read the ID with sc.nextLine() and trim() it. Then check that the 
length is 13 and that it's all digits. If not, print why it's invalid and ask again.
Test input	Expected
80010150090	"Must be exactly 13 digits"
80O1015009087 (letter O)	"Must contain digits only"
8001015009087	passes ✅
Part B: Extracting the details
getDateOfBirth(String id) → String, like "1980-01-01".
Use substring for YY, MM and DD.
The century: if YY (as an int) is greater than 26, it's the 1900s, otherwise the 2000s. So 80 → 1980 and 05 → 2005.
getGender(String id) → "Male" or "Female". Parse the SSSS digits
 with Integer.parseInt(id.substring(...)) and compare with 5000.
getCitizenship(String id) → "SA citizen" or "Permanent resident". Use charAt(10).
Part C: The checksum (the challenging part)

SA ID numbers use the Luhn algorithm, the same one used for bank card numbers. It catches typing mistakes. 
Here's how it works for a 13-digit ID:

Go through all 13 digits (index 0 to 12).
For digits at odd indexes (1, 3, 5, 7, 9, 11): double the digit. If the result is greater than 9, subtract 9.
Digits at even indexes (0, 2, 4, …, 12) are used as they are.
Add everything up.
The ID is valid if the total is divisible by 10 (total % 10 == 0).

Worked example for 8001015009087:

Index	0	1	2	3	4	5	6	7	8	9	10	11	12
Digit	8	0	0	1	0	1	5	0	0	9	0	8	7
Odd index?		✓		✓		✓		✓		✓		✓	
Doubled		0		2		2		0		18→9		16→7	
Value used	8	0	0	2	0	2	5	0	0	9	0	7	7

Total = 8+0+0+2+0+2+5+0+0+9+0+7+7 = 40. 40 % 10 == 0, so it's valid ✅

Write isValidChecksum(String id) → boolean. You'll need:

a loop with the index (which loop type?)
id.charAt(i) - '0' to get each digit as an int
i % 2 == 1 to detect odd indexes
a running sum
Test ID	Expected
8001015009087	Valid (male, born 1980-01-01, SA citizen)
9506150123088	Valid (female, born 1995-06-15, SA citizen)
8001015009088	Invalid: last digit changed, so the total is 41
Part D: Masking with StringBuilder

maskId(String id) → String: keep the first 6 characters and replace the other 7 with *, giving 800101*******.
Use a StringBuilder: append the first 6 characters, then use a loop to append * for each remaining 
character. (Real systems mask ID numbers like this so they're not fully visible on screen.)

Part E: Putting it together

In main: read and validate an ID, print the details table (from the target output), 
then ask "Check another? (y/n)" and loop until the user enters n. Use equalsIgnoreCase 
so Y/y and N/n both work.

Bonus: also validate the date. The month must be 1–12 and the day 1–31. 9913325009087 should be 
rejected as an invalid date. */



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

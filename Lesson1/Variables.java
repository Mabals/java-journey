package Lesson1;
public class Variables {
    public static void main(String[] args) {
        int age = 23; //Whole numbers
        double balance = 1000.50; //Decimal numbers
        String name = "Thato"; //Text (double quotes)
        boolean isEmployed = true; //true or false()
        char Grade = 'A'; //Single character(single quotes)
        final double VAT_Rate = 0.15; //Constant value
        double total = balance + (balance * VAT_Rate); //Calculating total with VAT
        System.out.println(name + " is " + age + " years old and is he employed? " + isEmployed);
        System.out.println("Total balance including VAT is: " + total);

    }
}
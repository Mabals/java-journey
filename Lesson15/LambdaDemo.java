package Lesson15;

import java.util.ArrayList;
import java.util.List;

public class LambdaDemo {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("Sipho", "Thato", "Lerato", "Naledi"));

        names.sort((a, b) -> a.compareTo(b));
        System.out.println(names);

        names.sort((a, b) -> Integer.compare(a.length(), b.length()));
        System.out.println(names);

        names.removeIf(name -> name.startsWith("S"));
        System.out.println(names);

        names.forEach(name -> System.out.println("Hi " + name));
    }
}

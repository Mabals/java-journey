package Lesson13;

public interface StatementExporter {
    String export(BankAccount account);
    String getFileExtension();
}

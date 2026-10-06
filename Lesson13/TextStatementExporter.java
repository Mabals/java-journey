package Lesson13;

public class TextStatementExporter implements StatementExporter {

    @Override
    public String export(BankAccount account) {
        return account.getStatement();
    }

    @Override
    public String getFileExtension() {
        return "txt";
    }
}
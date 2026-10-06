package Lesson13;

public class CsvStatementExporter implements StatementExporter {

    @Override
    public String export(BankAccount account) {
        StringBuilder csv = new StringBuilder("Date,Description,Amount,Category\n");
        for (Transaction t : account.getHistory()) {
            csv.append(t.getDate()).append(",")
               .append(t.getDescription()).append(",")
               .append(t.getAmount()).append(",")
               .append(t.getCategory()).append("\n");
        }
        return csv.toString();
    }

    @Override
    public String getFileExtension() {
        return "csv";
    }
}
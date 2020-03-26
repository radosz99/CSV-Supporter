
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws IOException {
        File csvFile = new File("C:\\Users\\Radek\\Desktop\\6semestr\\DPP\\lab_3\\lab3\\file.csv");
        ArrayList<ArrayList<String>> data = CSVHandler.parseCSV(csvFile,",");
        ArrayList<Integer> columns = new ArrayList<>();
        ArrayList<Integer> rows = new ArrayList<>();
        columns.add(0);
        columns.add(2);

        ArrayList<ArrayList<String>> data2 = CSVHandler.getByMultipleColumn(columns,data);
        CSVHandler.showCSV(data2);

    }
}

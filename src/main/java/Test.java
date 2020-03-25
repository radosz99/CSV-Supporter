
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws IOException {
        File csvFile = new File("C:\\Users\\Radek\\Desktop\\6semestr\\DPPTes\\file.csv");
        ArrayList<ArrayList<String>> data = CSVHandler.parseCSV(csvFile);
        ArrayList<Integer> columns = new ArrayList<>();
        columns.add(0);
        columns.add(2);
        ArrayList<ArrayList<String>> data2 = CSVHandler.getByMultipleRow(columns,data);
        ArrayList<String> tescik = CSVHandler.getByRow(3,data);
        for(String s :tescik)
            System.out.println(s);
        CSVHandler csvHandler = new CSVHandler();
        csvHandler.saveToCSV(data2, ";", "test.csv");
        //CSVHandler.showCSV(data2);
    }
}

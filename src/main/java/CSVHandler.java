import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVHandler {

    public static ArrayList<String> getByRow(int rowId, ArrayList<ArrayList<String>> data){
        if(rowId>=data.size())
            return null;
        return data.get(rowId);
    }

    public static ArrayList<String> getByColumn(int columnId, ArrayList<ArrayList<String>> data){
        ArrayList<String> result = new ArrayList<>();
        for(ArrayList<String> row : data) {
            if (columnId >= row.size()) {
                return null;
            }
            else {
                result.add(row.get(columnId));
            }
        }
        return result;
    }

    public static  ArrayList<ArrayList<String>> getByMultipleRow(ArrayList<Integer> rowsId, ArrayList<ArrayList<String>> data){
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for(Integer rowId : rowsId) {
            if (rowId >= data.size())
                return null;
            result.add(data.get(rowId));
        }
        return result;
    }

    public static  ArrayList<ArrayList<String>> getByMultipleColumn (ArrayList<Integer> columnsId, ArrayList<ArrayList<String>> data){
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for(Integer columnId : columnsId) {
            result.add(new ArrayList<>());
            for(ArrayList<String> row : data) {
                if (columnId >= row.size()) {
                    return null;
                }
                else {
                    result.get(result.size() - 1).add(row.get(columnId));
                }
            }
        }
        return result;
    }

    public static ArrayList<ArrayList<String>> getByMultipleRowColumn (ArrayList<Integer> columnsId, ArrayList<Integer> rowsId, ArrayList<ArrayList<String>> data){
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        int counter=0;
        for(Integer rowId : rowsId){
            result.add(new ArrayList<>());
            for(Integer columnId : columnsId){
                result.get(counter).add(data.get(rowId).get(columnId));
            }
            counter++;
        }
        return result;
    }



    public static ArrayList<ArrayList<String>> parseCSV(File csvFile, String separator) throws IOException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        String[] headers = null;
        String line;
        BufferedReader br = new BufferedReader(new FileReader(csvFile));
        Scanner scanner = new Scanner(csvFile);
        int rowCounter = 0;
        while (scanner.hasNextLine()){
            data.add(new ArrayList());
            headers = scanner.nextLine().split(separator);
            for(String s : headers) {
                data.get(rowCounter).add(s);
            }
            rowCounter++;
        }
        return data;
    }

    public static ArrayList<ArrayList<String>> concatenateCSV(ArrayList<ArrayList<String>> main, ArrayList<ArrayList<String>> dataToAdd){
        for(ArrayList<String> row : dataToAdd){
            main.add(row);
        }
        return main;
    }

    public static void showCSV(ArrayList<ArrayList<String>> data){
        for(ArrayList<String> columns : data){
            for(String row : columns){
                System.out.print(row + " ");
            }
            System.out.print("\n");
        }
}

    public String arrayListToString(ArrayList<String> data, String separator) {
        String[]result = new String[data.size()];
        int counter = 0;
        for(String s : data){
            result[counter]=s;
            counter++;
        }
        return Stream.of(result)
                .collect(Collectors.joining(separator));
    }

        public void saveToCSV(ArrayList<ArrayList<String>> data, String separator, String pathname) throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new File(pathname))) {
            data.stream()
                    .map(n->arrayListToString(n, separator))
                    .forEach(pw::println);
        }
    }

}

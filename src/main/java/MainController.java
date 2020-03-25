import classes.View;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private Button loadDataBtn = new Button();
    @FXML private Button showDataBtn = new Button();
    @FXML private Button hideDataBtn = new Button();
    @FXML private Button filterBtn = new Button();
    ArrayList<ArrayList<String>> filterData = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    @FXML private TextArea allData = new TextArea();
    @FXML private TextField textFieldSeparator = new TextField();
    @FXML private TextField columnId = new TextField();
    @FXML private TextField rowId = new TextField();
    @FXML private Label rowsAmountLbl = new Label();
    @FXML private Label columnsAmountLbl = new Label();
    @FXML private Label rowsAmountInfoLbl = new Label();
    @FXML private Label columnsAmountInfoLbl = new Label();
    @FXML private Label separatorInfoLbl = new Label();
    @FXML private Label idRowLbl = new Label();
    @FXML private Label idColumnLbl = new Label();
    @FXML private Label fileNameInfoLbl = new Label();
    @FXML private Label fileNameLbl = new Label();
    @FXML private Label filtrLbl = new Label();
    @FXML private Label filtrInfoLbl = new Label();
    @FXML private Label loadLbl = new Label();
    @FXML private Label loadInfoLbl = new Label();
    private String fileName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showDataBtn.setOnAction(e->{
            showDataInTextArea(data);
        });

        filterBtn.setOnAction(e->{
            filterData();
            System.out.println(filterData.size() + " " + filterData.get(0).size());
            showDataInTextArea(filterData);
        });
        setLabels();
    }

    public void loadData() throws IOException {
        FileChooser choose = new FileChooser();
        File selectedFile = choose.showOpenDialog(null);
        if(selectedFile==null) {return;}
        if(checkIfCSV(selectedFile)){
            if(textFieldSeparator.getText().equals("")){
                badAlert("Wpisz separator!");
                return;
            }
            else {
                data = CSVHandler.parseCSV(selectedFile, textFieldSeparator.getText());
                goodAlert("Wczytanie pliku powiodło się!");
            }
        }
        else{
            badAlert("Wybierz plik CSV!");
            return;
        }

        if(data.get(0).size()==1){
            warningAlert("Jedna kolumna danych czy zły separator? (sprawdź klikając na Pokaż dane)");
        }
        columnsAmountLbl.setText(Integer.toString(data.get(0).size()));
        rowsAmountLbl.setText(Integer.toString(data.size()));
    }

    public void showDataInTextArea(ArrayList<ArrayList<String>> dataToShow){
        String allDataString = "";
        for(ArrayList<String> row : dataToShow){
            for(String column : row){
                allDataString=allDataString+column + "\t";
            }
            allDataString=allDataString+"\n";
        }
        allData.setText(allDataString);
    }

    public void hideDataInTextArea(){
        allData.setText("");
    }

    public void filterData(){
        String[]rows;
        String[]columns;
        ArrayList<Integer> rowsToFind = new ArrayList<>();
        ArrayList<Integer> columnsToFind = new ArrayList<>();

        try {
            rows = rowId.getText().split(";");
            columns = columnId.getText().split(";");
            for(String s : rows){
                rowsToFind.add(Integer.parseInt(s));
            }
            for(String s : columns){
                columnsToFind.add(Integer.parseInt(s));
            }
        } catch (NumberFormatException e) {
            badAlert("Wpisz w poprawnym formacie!");
            return;
        }
        try {
            filterData = CSVHandler.getByMultipleRowColumn(columnsToFind, rowsToFind, data);
        }catch(IndexOutOfBoundsException e){
            badAlert("Wpisz właściwy zakres!");
            return;
        }
    }


    public boolean checkIfCSV(File file){
        String[] parts;
        String name;
        name = file.getName();
        name = name.replace('.',';');
        parts = name.split(";");
        if(!parts[parts.length-1].contentEquals("csv")) {
            return false;
        }
        fileNameLbl.setText(file.getName());
        return true;
    }

    private void setLabels(){
        loadDataBtn.setText("Wczytaj plik CSV");
        showDataBtn.setText("Pokaż wczytany plik");
        hideDataBtn.setText("Ukryj dane");
        filterBtn.setText("Filtruj!");
        rowsAmountInfoLbl.setText("Liczba wierszy: ");
        columnsAmountInfoLbl.setText("Liczba kolumn: ");
        separatorInfoLbl.setText("Separator: ");
        fileNameInfoLbl.setText("Nazwa pliku: ");
        idRowLbl.setText("Id wierszy: ");
        idColumnLbl.setText("Id kolumn: ");
        filtrLbl.setText("Filtrowanie kolumn i wierszy");
        filtrInfoLbl.setText("Wpisz id separując znakiem ;");
        loadLbl.setText("Wczytywanie pliku CSV");
        loadInfoLbl.setText("Wybierz plik wpisując uprzednio separator");
    }

    /**
     * Metoda obslugujaca powiadomienia o udanej operacji.
     * @param message tekst do pokazania uzytkownikowi.
     */
    private static void goodAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SUKCES");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Metoda obslugujaca powiadomienia o nieudanej operacji.
     * @param message tekst do pokazania uzytkownikowi.
     */
    private static void badAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("BŁĄD");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Metoda obslugujaca pokazynie ostrzeżeń.
     * @param message tekst do pokazania uzytkownikowi.
     */
    private static void warningAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("OSTRZEŻENIE");
        alert.setHeaderText(message);
        alert.showAndWait();
    }


}

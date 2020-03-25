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
    @FXML private TableView tableView = new TableView();
    @FXML private Button loadDataBtn = new Button();
    @FXML private Button showDataBtn = new Button();
    @FXML private Button hideDataBtn = new Button();
    @FXML private Button getRowBtn = new Button();
    @FXML private Button getColumnBtn = new Button();
    ObservableList<View> observableView = FXCollections.observableArrayList();
    ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
    ArrayList<String> view = new ArrayList<>();
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
    @FXML private TableColumn<String, View> column1 = new TableColumn<>("Wartości");
    private String fileName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        column1.setCellValueFactory(new PropertyValueFactory<>("item"));
        tableView.getColumns().add(column1);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

    public void showDataInTextArea(){
        String allDataString = "";
        for(ArrayList<String> row : data){
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

    public void showByColumn(){
        view = null;

        int parseColumn = 0;
        try {
            parseColumn = Integer.parseInt(columnId.getText());
        } catch (NumberFormatException e) {
            badAlert("Wpisz poprawną wartość!");
            return;
        }
        view = CSVHandler.getByColumn(parseColumn,data);
        try {
            if (view.isEmpty()) {
                badAlert("Brak danych dla kolumny " + parseColumn);
                return;
            }
        }  catch (NullPointerException e) {
            badAlert("Brak danych dla kolumny " + parseColumn);
            return;
        }
        tableView.getItems().clear();
        for(String s : view) {
            observableView.add(new View(s));
        }

        tableView.setItems(observableView);
        column1.setText("Kolumna " + parseColumn);
    }

    public void showByRow(){
        view =  null;

        int parseRow = 0;
        try {
            parseRow = Integer.parseInt(columnId.getText());
        } catch (NumberFormatException e) {
            badAlert("Wpisz poprawną wartość!");
            return;
         }
        view = CSVHandler.getByRow(parseRow,data);
        try {
            if (view.isEmpty()) {
                badAlert("Brak danych dla wiersza " + parseRow);
                return;
            }
        } catch (NullPointerException e){
            badAlert("Brak danych dla wiersza " + parseRow);
            return;
        }
        tableView.getItems().clear();
        for(String s : view) {
            observableView.add(new View(s));
        }
        tableView.setItems(observableView);
        column1.setText("Wiersz " + parseRow);
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
        showDataBtn.setText("Pokaż dane");
        hideDataBtn.setText("Ukryj dane");
        getColumnBtn.setText("Pokaż kolumnę");
        getRowBtn.setText("Pokaż wiersz");
        rowsAmountInfoLbl.setText("Liczba wierszy: ");
        columnsAmountInfoLbl.setText("Liczba kolumn: ");
        separatorInfoLbl.setText("Separator: ");
        fileNameInfoLbl.setText("Nazwa pliku: ");
        idRowLbl.setText("Id: ");
        idColumnLbl.setText("Id: ");
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

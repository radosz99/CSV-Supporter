import classes.LocalizedBinding;
import classes.View;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ChoiceFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private Button loadDataBtn = new Button();
    @FXML private Button showDataBtn = new Button();
    @FXML private Label saveFilterDataInfo = new Label();
    @FXML private Button filterBtn = new Button();
    @FXML private Button saveFilterDataBtn = new Button();
    @FXML private Button saveFilesBtn = new Button();
    @FXML private Button addFileBtn = new Button();
    @FXML private Button resetBtn = new Button();
    @FXML private ImageView imageviewFlag;
    ArrayList<ArrayList<String>> dataFromFileConcatenation = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> dataForConcatenation = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> filterData = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> dataFromCSVFile = new ArrayList<ArrayList<String>>();
    @FXML private TextArea allData = new TextArea();
    @FXML private TextField textFieldSeparator = new TextField();
    @FXML private TextField textFieldSeparator2 = new TextField();
    @FXML private TextField textFieldSeparator3 = new TextField();
    @FXML private TextField columnId = new TextField();
    @FXML private TextField rowId = new TextField();
    @FXML private ComboBox <Locale> lang;
    @FXML private Label rowsAmountLbl = new Label();
    @FXML private Label columnsAmountLbl = new Label();
    @FXML private Label separatorInfoLbl = new Label();
    @FXML private Label separatorInfoLbl2 = new Label();
    @FXML private Label separatorInfoLbl3 = new Label();
    @FXML private Label idRowLbl = new Label();
    @FXML private Label idColumnLbl = new Label();
    @FXML private Label fileNameInfoLbl = new Label();
    @FXML private Label fileNameLbl = new Label();
    @FXML private Label countryLbl = new Label();
    @FXML private Label filtrLbl = new Label();
    @FXML private Label filtrInfoLbl = new Label();
    @FXML private Label loadLbl = new Label();
    @FXML private Label loadInfoLbl = new Label();
    @FXML private Label concatLbl = new Label();
    @FXML private Label concatInfoLbl = new Label();
    @FXML private Label allLbl = new Label();
    @FXML private Label timeLbl = new Label();
    @FXML private CheckBox columnCheckbox = new CheckBox();
    @FXML private CheckBox rowCheckbox = new CheckBox();
    public LocalizedBinding localizedBinding;
    public ResourceBundle sources;
    Timeline clock = new Timeline();
    private int filesToConcat;
    private String fileName="";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lang.getItems().addAll(new Locale("pl","PL"),
                new Locale("en","US"),
                new Locale("en","GB"));

        lang.setCellFactory(lv -> {
            return createListCell();
        });
        lang.setButtonCell(createListCell());

        localizedBinding = new LocalizedBinding("bundles", Locale.getDefault());
        localizedBinding.localeProperty().bind(lang.valueProperty());

        lang.getSelectionModel().select(Locale.getDefault());
        lang.setOnAction(lv -> {
            updateDateAndFlag();
        });
        setCellFactory();
        updateDateAndFlag();
        sources = resourceBundle;
        allData.setEditable(false);
        Main.getPrimaryStage().titleProperty().bind(localizedBinding.createStringBinding("title"));
        showDataBtn.setOnAction(e->{
            if(fileName!="") {
                showDataInTextArea(dataFromCSVFile);
            }
            else{
                warningAlert("Nie został wczytany żaden plik");
            }
        });
        loadDataBtn.setOnAction(e->{
            try {
                loadData();
            } catch (NullPointerException | IOException ex) {
                return;
            }
        });

        filterBtn.setOnAction(e->{
            filterData();
        });

        addFileBtn.setOnAction(e->{
            try {
                concateFiles();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        setLabels();
    }

    private ListCell<Locale> createListCell() {
        return new ListCell<Locale>() {
            @Override
            public void updateItem(Locale locale, boolean empty) {
                super.updateItem(locale, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(locale.getDisplayCountry(locale));
                }
            }
        };
    }

    public void setCellFactory(){
        lang.setCellFactory(new Callback<ListView<Locale>, ListCell<Locale>>() {
            @Override
            public ListCell<Locale> call(ListView<Locale> p) {
                return new ListCell<Locale>() {
                    @Override
                    protected void updateItem(Locale item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getDisplayCountry(item));
                        }
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            try {
                                icon = new Image(new File("C:\\Users\\Radek\\Desktop\\6semestr\\DPP\\lab_3\\lab3\\src\\main\\resources\\icons\\"+item.getCountry().toString()+".png").toURI().toString());
                            } catch(NullPointerException ex) {
                                System.out.println("brak ikony");
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(10);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        });
    }

    public ArrayList<ArrayList<String>> getDataFromFile(ArrayList<ArrayList<String>> dataFromFile, String separator) throws IOException {
        FileChooser choose = new FileChooser();
        File selectedFile = choose.showOpenDialog(null);
        if(selectedFile==null) {
            return dataFromFile;
        }
        if(checkIfCSV(selectedFile)){
            dataFromFile = CSVHandler.parseCSV(selectedFile, separator);
            goodAlert("Wczytanie pliku powiodło się!");

        }
        else{
            badAlert("Wybierz plik CSV!");
            return null;
        }
        return dataFromFile;

    }

    public void resetConcatenation(){
        warningAlert("Zresetowano " + filesToConcat + " plików.");
        dataFromFileConcatenation.clear();
        filesToConcat=0;
    }

    public void choiceFormatter(int rows, int columns) {
        System.out.println("Wiersz " + rows);
        System.out.println("Kolumna " + columns);
        String help = String.valueOf(rows);
        int rowsAmount = Integer.parseInt(getTwoLastDigits(help));
        System.out.println("Przek wiersze " + rowsAmount);
        String s = localizedBinding.getString("rows");
        ChoiceFormat fmt = new ChoiceFormat(s);
        rowsAmountLbl.setText(rows + " "+ fmt.format(rowsAmount));

        help = String.valueOf(columns);
        int columnsAmount = Integer.parseInt(getTwoLastDigits(help));
        System.out.println("Przek kolumny} " + columnsAmount);
        s = localizedBinding.getString("columns");
        fmt = new ChoiceFormat(s);
        columnsAmountLbl.setText(columns + " "+ fmt.format(columnsAmount));
    }

    public void loadData() throws IOException {
        if(textFieldSeparator.getText().equals("")){
            badAlert("Wpisz separator!");
            return;
        }
        dataFromCSVFile = getDataFromFile(dataFromCSVFile,textFieldSeparator.getText());
        if(!dataFromCSVFile.isEmpty()) {
            fileNameLbl.setText(fileName);
        }
        else{
            return;
        }
        if(dataFromCSVFile.get(0).size()==1){
            warningAlert("Jedna kolumna danych czy zły separator? (sprawdź naciskając na Pokaż wczytany plik)");
        }
        choiceFormatter(dataFromCSVFile.size(), dataFromCSVFile.get(0).size());
    }

    private String getTwoLastDigits(String help){
        char[] ch = new char[help.length()];
        for (int i = 0; i < help.length(); i++) {
            ch[i] = help.charAt(i);
        }

        while(help.length()>2) {
            char[] helpCh = new char[help.length()-1];
            for(int i=0; i<help.length()-1;i++) {
                ch[i]=ch[i+1];
                helpCh[i]=ch[i+1];
            }
            help = new String(helpCh);
        }
        return help;
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

    public void saveFilterData() throws FileNotFoundException {
        if(textFieldSeparator3.getText().equals("")){
            badAlert("Wpisz separator!");
            return;
        }
        if(!filterData.isEmpty()) {
            CSVHandler csvHandler = new CSVHandler();
            FileChooser fx = new FileChooser();
            File file = fx.showSaveDialog(Main.getPrimaryStage());
            if(file!=null) {
                csvHandler.saveToCSV(filterData, textFieldSeparator3.getText(), file.getPath());
                goodAlert("Zapisano do pliku " + file.getName());
            }
        }
        else{
            warningAlert("Nie ma nic do zapisania!");
        }
    }

    public void filterData(){
        String[]rows;
        String[]columns;
        ArrayList<Integer> rowsToFind = new ArrayList<>();
        ArrayList<Integer> columnsToFind = new ArrayList<>();

        if(!columnCheckbox.isSelected()) {
            try {
                columns = columnId.getText().split(";");
                for (String s : columns) {
                    columnsToFind.add(Integer.parseInt(s));
                }
            } catch (NumberFormatException e) {
                badAlert("Wpisz w poprawnym formacie!");
                return;
            }
        }

        if(!rowCheckbox.isSelected()) {
            try {
                rows = rowId.getText().split(";");
                for (String s : rows) {
                    rowsToFind.add(Integer.parseInt(s));
                }
            } catch (NumberFormatException e) {
                badAlert("Wpisz w poprawnym formacie!");
                return;
            }
        }

        if(!columnCheckbox.isSelected() && !rowCheckbox.isSelected())
            filterForMultiple(columnsToFind,rowsToFind);
        else if (columnCheckbox.isSelected()&& rowCheckbox.isSelected()){
            filterData = dataFromCSVFile;
            showDataInTextArea(filterData);
        }
        else if(columnCheckbox.isSelected()){
            try {
                filterData = CSVHandler.getByMultipleRow(rowsToFind,dataFromCSVFile);
                showDataInTextArea(filterData);
            }catch(IndexOutOfBoundsException e){
                badAlert("Wpisz właściwy zakres!");
                return;
            }
        }
        else if(rowCheckbox.isSelected()){
            try {
                filterData = CSVHandler.getByMultipleColumn(columnsToFind,dataFromCSVFile);
                showDataInTextArea(filterData);
            }catch(IndexOutOfBoundsException e){
                badAlert("Wpisz właściwy zakres!");
                return;
            }
        }
    }

    public void filterForMultiple(ArrayList<Integer> columnsToFind,  ArrayList<Integer> rowsToFind){

        try {
            filterData = CSVHandler.getByMultipleRowColumn(columnsToFind, rowsToFind, dataFromCSVFile);
            showDataInTextArea(filterData);
        }catch(IndexOutOfBoundsException e){
            badAlert("Wpisz właściwy zakres!");
            return;
        }
    }
    public void saveFiles() throws FileNotFoundException {
        if(filesToConcat!=0) {
            CSVHandler csvHandler = new CSVHandler();
            FileChooser fx = new FileChooser();
            File file = fx.showSaveDialog(Main.getPrimaryStage());
            if(file!=null) {
                csvHandler.saveToCSV(dataFromFileConcatenation, textFieldSeparator2.getText(), file.getPath());
                goodAlert("Zapisano " + filesToConcat + " plików do pliku " + file.getName());
                dataFromFileConcatenation.clear();
                filesToConcat = 0;
            }
        }
        else{
            warningAlert("Dodaj najpierw jakieś pliki!");
        }
    }

    public void concateFiles() throws IOException {
        if(textFieldSeparator2.getText().equals("")){
            badAlert("Wpisz separator!");
            return;
        }
        dataForConcatenation=null;
        dataForConcatenation = getDataFromFile(dataForConcatenation,textFieldSeparator2.getText());
        if(dataForConcatenation!=null) {
            filesToConcat++;
            goodAlert("To już " + filesToConcat + " plik");
            CSVHandler.concatenateCSV(dataFromFileConcatenation, dataForConcatenation);

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
        fileNameLbl.textProperty().unbind();
        fileName = file.getName();
        return true;
    }

    private void setLabels(){
        loadDataBtn.textProperty().bind(localizedBinding.createStringBinding("loadDataBtn"));
        showDataBtn.textProperty().bind(localizedBinding.createStringBinding("showDataBtn"));
        saveFilterDataInfo.textProperty().bind(localizedBinding.createStringBinding("saveFilterDataInfo"));
        filterBtn.textProperty().bind(localizedBinding.createStringBinding("filterBtn"));
        separatorInfoLbl.setText("Separator: ");
        separatorInfoLbl2.setText("Separator: ");
        separatorInfoLbl3.setText("Separator: ");
        fileNameInfoLbl.textProperty().bind(localizedBinding.createStringBinding("fileNameInfoLbl"));
        idRowLbl.textProperty().bind(localizedBinding.createStringBinding("idRowLbl"));
        idColumnLbl.textProperty().bind(localizedBinding.createStringBinding("idColumnLbl"));
        fileNameLbl.textProperty().bind(localizedBinding.createStringBinding("fileNameLbl"));
        filtrLbl.textProperty().bind(localizedBinding.createStringBinding("filtrLbl"));
        filtrInfoLbl.textProperty().bind(localizedBinding.createStringBinding("filtrInfoLbl"));
        loadLbl.textProperty().bind(localizedBinding.createStringBinding("loadLbl"));
        loadInfoLbl.textProperty().bind(localizedBinding.createStringBinding("loadInfoLbl"));
        saveFilterDataBtn.textProperty().bind(localizedBinding.createStringBinding("saveFilterDataBtn"));
        saveFilesBtn.textProperty().bind(localizedBinding.createStringBinding("saveFilterDataBtn"));
        concatLbl.textProperty().bind(localizedBinding.createStringBinding("concatLbl"));
        concatInfoLbl.textProperty().bind(localizedBinding.createStringBinding("concatInfoLbl"));
        addFileBtn.textProperty().bind(localizedBinding.createStringBinding("addFileBtn"));
        resetBtn.textProperty().bind(localizedBinding.createStringBinding("resetBtn"));
        allLbl.textProperty().bind(localizedBinding.createStringBinding("allLbl"));
        countryLbl.textProperty().bind(localizedBinding.createStringBinding("countryLbl"));
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

    public void updateDateAndFlag() {
        Locale.setDefault(lang.getSelectionModel().getSelectedItem());
        DateTimeFormatter localeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        clock.stop();
        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            timeLbl.setText(LocalDateTime.now().format(localeFormat));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        Image i = new Image(new File("C:\\Users\\Radek\\Desktop\\6semestr\\DPP\\lab_3\\lab3\\src\\main\\resources\\icons\\"+Locale.getDefault().getCountry().toString()+ ".png").toURI().toString());
        imageviewFlag.setImage(i);
        try {
            choiceFormatter(dataFromCSVFile.size(), dataFromCSVFile.get(0).size());
        } catch (IndexOutOfBoundsException e){

        }


    }

}

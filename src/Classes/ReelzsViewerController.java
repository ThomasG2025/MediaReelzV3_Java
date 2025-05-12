package Classes;

import Database.ReelOperations.*;
import Objects.User;
import Objects.Reel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.function.Predicate;

import static javafx.application.Platform.exit;


public class ReelzsViewerController implements Initializable {

    // files
    public File file;

    // Observable Lists
    public ObservableList<Reel> reelzsList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsCdList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsDvdList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsArchivedList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsNotArchivedList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsIsOriginalList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsNotOriginalList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsCdExportList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsDvdExportList = FXCollections.observableArrayList();

    // ArrayLists
    public ArrayList<Reel> bulkReelList = new ArrayList<>();
    public ArrayList<Reel> bulkReelList2 = new ArrayList<>();
    public ArrayList<Reel> approvedBulkReelzsList = new ArrayList<>();
    public ArrayList<Reel> duplicateBulkReelListForUserReelDuplicateCheck = new ArrayList<>();
    public ArrayList<Reel> duplicateUserReelIdsToInsert = new ArrayList<>();

    // arrays
    public String[] bulkArray;

    @FXML
    private String[] searchFilterNames = {"Standard" , "Cd" , "Dvd" , "Artist" , "Year" , "Is Original Reel" , "Is Not Original Reel" , "Type" , "Genre" , "Owned Reelzs" , "Wanted Reelzs"  , "Digitally Archived Reelzs" , "Not Digitally Archived"};

    // int's
    public int index = -1;

    // Alerts




    // errors //
    public Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
    public Alert reelModificationError = new Alert(Alert.AlertType.ERROR);
    public Alert databaseQueryExecutionError = new Alert(Alert.AlertType.ERROR);
    public Alert databaseModificationError = new Alert(Alert.AlertType.ERROR);
    public Alert fileLoadingError = new Alert(Alert.AlertType.ERROR);
    public Alert fileReadingError = new Alert(Alert.AlertType.ERROR);
    public Alert fileWritingError = new Alert(Alert.AlertType.ERROR);
    public Alert windowCreationError = new Alert(Alert.AlertType.ERROR);
    public Alert databaseRetrievalError = new Alert(Alert.AlertType.ERROR);
    public Alert singleInsertDuplicateLocationError = new Alert(Alert.AlertType.ERROR);

    // information //
    public Alert reelzsRefreshedNotification = new Alert(Alert.AlertType.INFORMATION);
    public Alert bulkSuccessfulInsertNotification = new Alert(Alert.AlertType.INFORMATION);
    public Alert successfulInsertNotification = new Alert(Alert.AlertType.INFORMATION);
    public Alert successfulUpdateNotification = new Alert(Alert.AlertType.INFORMATION);
    public Alert successfulDeleteNotification = new Alert(Alert.AlertType.INFORMATION);
    public Alert successfulReelzsExport = new Alert(Alert.AlertType.INFORMATION);


    // pane
    public Pane reelzsViewerPanel;

    // menuBar
    public MenuBar menuBar;
    public Label menuBarFileLabel;
    public Menu fileMenu;
    public MenuItem batchAddReelzsMenuItem;
    public MenuItem standardExportReelzsMenuItem;
    public MenuItem cdExportReelzsMenuItem;
    public MenuItem dvdExportReelzsMenuItem;
    public Label menuBarEditLabel;
    public Menu editMenu;
    public MenuItem modifyUserMenuItem;
    public Label menuBarHelpLabel;
    public Menu helpMenu;
    public MenuItem aboutMenuItem;

    // ChoiceBoxes
    @FXML
    private ChoiceBox<String> advancedSearchFiltersChoiceBox = new ChoiceBox<>();

    // labels(excluding menuBar labels
    public Label searchReelzsLabel;
    public Label reelIdLabel;
    public Label reelNameLabel;
    public Label reelArtistLabel;
    public Label reelYearLabel;
    public Label reelTypeLabel;
    public Label reelGenreLabel;
    public Label reelNotesLabel;
    public Label reelRatingLabel;

    // textfields
    public TextField searchReelzsTextField;
    public TextField reelIdTextfield;
    public TextField reelNameTextfield;
    public TextField reelArtistTextfield;
    public TextField reelYearTextfield;
    public TextField reelTypeTextfield;
    public TextField reelGenreTextField;
    public TextField reelNotesTextfield;
    public TextField reelRatingTextfield;

    // checkBoxes
    public CheckBox reelDigitallyArchivedStatusCheckBox;
    public CheckBox reelIsOriginalCheckBox;

    // buttons
    public Button addReelButton;
    public Button updateReelButton;
    public Button deleteReelButton;
    public Button clearEntryFieldsButton;

// tableview and columns

    @FXML
    private TableView<Reel> reelzsTable;
    @FXML
    private TableColumn<Reel, Integer> reelIdColumn;
    @FXML
    private TableColumn<Reel, String> reelNameColumn;
    @FXML
    private TableColumn<Reel, String> reelArtistColumn;
    @FXML
    private TableColumn<Reel, Integer> reelYearColumn;
    @FXML
    private TableColumn<Reel, String> reelTypeColumn;
    @FXML
    private TableColumn<Reel, String> reelGenreColumn;
    @FXML
    private TableColumn<Reel, Integer> reelIsOriginalColumn;
    @FXML
    private TableColumn<Reel, String> reelRatingColumn;
    @FXML
    private TableColumn<Reel, String> reelNotesColumn;

    @FXML
    private TableColumn<Reel, Integer> reelDigitallyArchivedColumn;

    public void getReelzs() {

        reelzsList.removeAll(reelzsList);



        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT * FROM reelView Where userId = ? ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            rs = ps.executeQuery();
            while (rs.next()) {


                reelzsList.addAll(new Reel(
                        rs.getInt("reelId") ,
                        rs.getString("reelName") ,
                        rs.getString("reelArtist") ,
                        rs.getInt("reelYear") ,
                        rs.getString("reelType") ,
                        rs.getString("reelGenre") ,
                        rs.getString("reelRating") ,
                        rs.getInt("reelIsOriginalStatus") ,
                        rs.getString("note") ,
                        rs.getInt("reelDigitallyArchivedStatus")));

                reelzsTable.setItems(reelzsList);

            }
            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException throwables) {
            databaseRetrievalError.setContentText(" there was a problem retrieving your reelzs from the database");
            databaseRetrievalError.showAndWait();
            throwables.printStackTrace();
        }

    }

    //    single insert method
    public void verifyRequiredFieldsAreNotNull() {
        ReelSingleInsert reelSI = new ReelSingleInsert(reelzsTable);
        if (reelNameTextfield.getText().equals("") || reelArtistTextfield.getText().equals("") || reelYearTextfield.getText().equals("") || reelTypeTextfield.getText().equals("")
                || reelGenreTextField.getText().equals("")) {

            reelModificationError.setContentText("one or more of the required fields are empty");
            reelModificationError.showAndWait();


        } else {




                reelSI.verifyReelIsNotDuplicate(
                        reelNameTextfield.getText(),
                        reelArtistTextfield.getText(),
                        Integer.parseInt(reelYearTextfield.getText())
                        ,reelTypeTextfield.getText(),
                        reelGenreTextField.getText(),
                        reelRatingTextfield.getText(),
                        ConvertCheckboxResultToInt(reelIsOriginalCheckBox.isSelected()),
                        reelNotesTextfield.getText(),
                        ConvertCheckboxResultToInt(reelDigitallyArchivedStatusCheckBox.isSelected())

                );



        }

    }

    public int ConvertCheckboxResultToInt(Boolean checkboxState) {

        int convertedState = 0;

        if (checkboxState) {
            convertedState = 1;

        }

        return convertedState;

    }

//   update method( will be using checkbox boolean to int method from single add reel)

    public void updateReel() {
        ReelUpdate reelU = new ReelUpdate(reelzsTable);
        int reelId = Integer.parseInt(reelIdTextfield.getText());
        String reelName = reelNameTextfield.getText();
        String reelArtist = reelArtistTextfield.getText();
        int reelYear = Integer.parseInt(reelYearTextfield.getText());
        String reelType = reelTypeTextfield.getText();
        String reelGenre = reelGenreTextField.getText();
        String reelRating = reelRatingTextfield.getText();
        int reelOriginalStatus = ConvertCheckboxResultToInt(reelIsOriginalCheckBox.isSelected());
        int reelDigitallyArchivedStatus = ConvertCheckboxResultToInt(reelDigitallyArchivedStatusCheckBox.isSelected());
        String reelNotes = reelNotesTextfield.getText();

        if (reelId == 0 || reelName.equals("") || reelArtist.equals("") || reelYear == 0 || reelType.equals("")
                || reelGenre.equals("") ) {

            reelModificationError.setContentText("one or more of the required fields are empty");
            reelModificationError.showAndWait();
        } else {



                reelU.updateReelTable(reelId , reelName , reelArtist , reelYear , reelType , reelGenre , reelRating , reelOriginalStatus , reelDigitallyArchivedStatus , reelNotes);







        }

    }

// delete method //

    public void verifyRequiredDeleteFieldsAreNotNull() {

        deleteWarning.setContentText("Are you sure you want to delete this reel ?");
        Optional<ButtonType> result = deleteWarning.showAndWait();
        ReelDelete reelD = new ReelDelete(reelzsTable);
        int reelId = Integer.parseInt(reelIdTextfield.getText());

        if (result.isPresent() && result.get() == ButtonType.OK && reelId == 0 ) {

            reelModificationError.setContentText("one or more of the required fields are empty");
            reelModificationError.showAndWait();
        } else {

            reelD.checkNumberOfUserReelIdsWithReelId(reelId);

        }
    }

//    bulk import code will go here   //

    public void advEnter() throws IOException {
        ArrayList<Reel> bulkReelList = new ArrayList<>();
        ReelBulkImport reelB = new ReelBulkImport(reelzsTable);
        FileChooser csvImport = new FileChooser();
        csvImport.setInitialDirectory(new File("."));
        csvImport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        file = csvImport.showOpenDialog(null);

        Scanner fileIn = new Scanner(file);
        if (file.isFile()) {

            bulkReelList =  reelB.bulkImport(file);

        } else {

            fileLoadingError.setContentText("That was not a valid file please make sure the file ends in .csv and try again");
            fileLoadingError.showAndWait();
        }
        fileIn.close();


        restartMediaReelzs();


    }

    public boolean ConvertIntToBoolean(int status) {
        boolean returnedStatus = false;
        if (status == 1) {
            returnedStatus = true;

        }

        return returnedStatus;
    }

    public  void restartMediaReelzs(){

        exit();


    }

// export reelzs  //

    public void standardExport() {
        ReelExport reelE = new ReelExport();
        reelE.writeStandardCsv(reelzsList);

    }

    public void cdExport(){
        ReelExport reelE = new ReelExport();
        reelE.writeCdCsv(reelzsList);

    }

    public void dvdExport(){
        ReelExport reelE = new ReelExport();
        reelE.writeDvdCsv(reelzsList);

    }

    // search methods //

    public void searchTypeChooserFunction() {

        ReelSearch reelS = new ReelSearch(reelzsTable);
        FilteredList<Reel> filteredData = new FilteredList<>(reelzsList , e -> true);
        FilteredList<Reel> filteredCdReelzs = new FilteredList<>(reelzsCdList , e -> true);
        FilteredList<Reel> filteredDvdReelzs = new FilteredList<>(reelzsDvdList , e -> true);
        FilteredList<Reel> filteredArtists = new FilteredList<>(reelzsList , e -> true);
        FilteredList<Reel> filteredYears = new FilteredList<>(reelzsList , e -> true);
        FilteredList<Reel> filteredOriginalReelzs = new FilteredList<>(reelzsIsOriginalList , e -> true);
        FilteredList<Reel> filteredNotOriginalReelzs = new FilteredList<>(reelzsNotOriginalList , e -> true);
        FilteredList<Reel> filteredTypes = new FilteredList<>(reelzsList , e -> true);
        FilteredList<Reel> filteredGenres = new FilteredList<>(reelzsList , e -> true);
        FilteredList<Reel> filteredArchivedReelzs = new FilteredList<>(reelzsArchivedList , e -> true);
        FilteredList<Reel> filteredNotArchivedReelzs = new FilteredList<>(reelzsNotArchivedList , e -> true);


        if (advancedSearchFiltersChoiceBox.getValue().equals("Standard")) {
            reelS.standardReelzsSearchMethod(searchReelzsTextField,filteredData,reelzsList);

        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Cd")){

            reelS.cdSearchMethod(searchReelzsTextField,filteredCdReelzs,reelzsList);
        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Dvd")){

            reelS.dvdSearchMethod(searchReelzsTextField,filteredDvdReelzs,reelzsList);
        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Artist")) {
            reelS.artistSearchMethod(searchReelzsTextField,filteredArtists);

        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Year")) {

            reelS.yearSearchMethod(searchReelzsTextField,filteredYears);

        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Is Original Reel")){

            reelS.isOriginalMethod(searchReelzsTextField,filteredOriginalReelzs,reelzsList);

        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Is Not Original Reel")){

            reelS.isNotOriginalMethod(searchReelzsTextField,filteredNotOriginalReelzs,reelzsList);
        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Type")) {
            reelS.typeSearchMethod(searchReelzsTextField,filteredTypes);
        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Genre")) {
            reelS.genreSearchMethod(searchReelzsTextField,filteredGenres);
        }

        if(advancedSearchFiltersChoiceBox.getValue().equals("Digitally Archived Reelzs")){
            reelS.digitallyArchivedSearchMethod(searchReelzsTextField,filteredArchivedReelzs,reelzsList);
        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Not Digitally Archived")){
            reelS.notDigitallyArchivedSearchMethod(searchReelzsTextField,filteredNotArchivedReelzs,reelzsList);
        }


    }

    // viewer functions and button events //

    public void getSelectedReel(MouseEvent event) {
        index = reelzsTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;

        }
        reelIdTextfield.setText(reelIdColumn.getCellData(index).toString());
        reelNameTextfield.setText(reelNameColumn.getCellData(index));
        reelArtistTextfield.setText(reelArtistColumn.getCellData(index));
        reelYearTextfield.setText(reelYearColumn.getCellData(index).toString());
        reelTypeTextfield.setText(reelTypeColumn.getCellData(index));
        reelGenreTextField.setText(reelGenreColumn.getCellData(index));
        reelRatingTextfield.setText(reelRatingColumn.getCellData(index));
        reelIsOriginalCheckBox.setSelected(reelzsTable.getSelectionModel().getSelectedItem().getReelIsOriginal().isSelected());
        reelNotesTextfield.setText(reelNotesColumn.getCellData(index));
        reelDigitallyArchivedStatusCheckBox.setSelected(reelzsTable.getSelectionModel().getSelectedItem().getReelDigitallyArchived().isSelected());

    }

    public void clearEntryFields() {

        reelIdTextfield.setText(" ");
        reelNameTextfield.setText("");
        reelArtistTextfield.setText("");
        reelYearTextfield.setText("");
        reelTypeTextfield.setText("");
        reelGenreTextField.setText("");
        reelIsOriginalCheckBox.setSelected(false);
        reelRatingTextfield.setText("");
        reelNotesTextfield.setText("");
        reelDigitallyArchivedStatusCheckBox.setSelected(false);

    }

    public void createAboutWindow() {

        try {

            FXMLLoader createAboutWindowLoader = new FXMLLoader(getClass().getResource("/MediaReelzV2/FXML/AboutWindow.fxml"));
            Parent root8 = createAboutWindowLoader.load();
            Stage createAboutStage = new Stage();
            createAboutStage.initModality(Modality.APPLICATION_MODAL);
            createAboutStage.setTitle("About Media Reelzs");
            createAboutStage.setScene(new Scene(root8));
            createAboutStage.show();

        } catch (IOException e) {
            windowCreationError.setContentText(" there was a problem creating the about window");
            windowCreationError.showAndWait();
            e.printStackTrace();
        }

    }

    public void createModifyUserWindow() {

        try {

            FXMLLoader createModifyUserWindow = new FXMLLoader(getClass().getResource("/MediaReelzV2/FXML/ModifyUsersWindow.fxml"));
            Parent root9 = createModifyUserWindow.load();
            Stage createAboutStage = new Stage();
            createAboutStage.initModality(Modality.APPLICATION_MODAL);
            createAboutStage.setTitle("Modify User");
            createAboutStage.setScene(new Scene(root9));
            createAboutStage.show();

        } catch (IOException e) {
            windowCreationError.setContentText(" there was a problem creating the modify user window");
            windowCreationError.showAndWait();
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url , ResourceBundle resourceBundle) {
        advancedSearchFiltersChoiceBox.getItems().addAll(searchFilterNames);
        advancedSearchFiltersChoiceBox.setValue("Standard");
        advancedSearchFiltersChoiceBox.setOnAction(e -> searchTypeChooserFunction());
        reelIdColumn.setCellValueFactory(new PropertyValueFactory<>("reelId"));
        reelNameColumn.setCellValueFactory(new PropertyValueFactory<>("reelName"));
        reelArtistColumn.setCellValueFactory(new PropertyValueFactory<>("reelArtist"));
        reelYearColumn.setCellValueFactory(new PropertyValueFactory<>("reelYear"));
        reelTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reelType"));
        reelGenreColumn.setCellValueFactory(new PropertyValueFactory<>("reelGenre"));
        reelRatingColumn.setCellValueFactory(new PropertyValueFactory<>("reelRating"));
        reelIsOriginalColumn.setCellValueFactory(new PropertyValueFactory<>("reelIsOriginal"));
        reelNotesColumn.setCellValueFactory(new PropertyValueFactory<>("reelNotes"));
        reelDigitallyArchivedColumn.setCellValueFactory(new PropertyValueFactory<>("reelDigitallyArchived"));
        getReelzs();

    }

}





package Classes;

import Objects.User;
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

public class ReelzsViewerController implements Initializable {

    // files
    public File file;
    public File file2;
    public File file3;
    public File file4;
    public File file5;
    public File file6;
    public File file7;


    // Observable Lists
    public ObservableList<Reel> reelzsList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsCdList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsDvdList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsOwnList = FXCollections.observableArrayList();
    public ObservableList<Reel> reelzsWantList = FXCollections.observableArrayList();
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
    public ArrayList<Integer> bulkierReliesForUniqueReelAttributeRetrieval = new ArrayList<>();
    public ArrayList<Integer> bulkUserReelIdsForUniqueReelAttributeRetrieval2 = new ArrayList<>();
    public ArrayList<Integer> bulkierReliesForReeLocationIdRetrieval = new ArrayList<>();
    public ArrayList<Integer> bulkierReliesForReeLocationIdRetrieval2 = new ArrayList<>();
    public ArrayList<Integer> retrievedReelIdsBasedOnUserId = new ArrayList<>();

    // arrays
    public String[] bulkArray;





    @FXML
    private String[] searchFilterNames = {"Standard" , "Cd" , "Dvd" , "Artist" , "Year" , "Is Original Reel" , "Is Not Original Reel" , "Type" , "Genre" , "Owned Reelzs" , "Wanted Reelzs" , "Location" , "Digitally Archived Reelzs" , "Not Digitally Archived"};

    // int's
    public int index = -1;

    // Alerts
    public Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
    public Alert reelModificationError = new Alert(Alert.AlertType.ERROR);
    public Alert databaseQueryExecutionError = new Alert(Alert.AlertType.ERROR);
    public Alert databaseModificationError = new Alert(Alert.AlertType.ERROR);
    public Alert fileLoadingError = new Alert(Alert.AlertType.ERROR);
    public Alert fileReadingError = new Alert(Alert.AlertType.ERROR);
    public Alert fileWritingError = new Alert(Alert.AlertType.ERROR);
    public Alert windowCreationError = new Alert(Alert.AlertType.ERROR);
    public Alert databaseRetrievalError = new Alert(Alert.AlertType.ERROR);
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
    public Label reelLocationNameLabel;
    public Label reelLocationPageNumberLabel;
    public Label reelLocationPageSizeLabel;
    public Label reelLocationSlotNumberLabel;
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
    public TextField reelLocationNameTextField;
    public TextField reelLocationPageNumberTextField;
    public TextField reelLocationPageSizeTextField;
    public TextField reelLocationSlotNumberTextField;
    public TextField reelRatingTextfield;

    // checkBoxes
    public CheckBox reelOwnStatusCheckbox;
    public CheckBox reelWantStatusCheckbox;
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
    private TableColumn<Reel, Integer> reelOwnColumn;
    @FXML
    private TableColumn<Reel, Integer> reelWantColumn;
    @FXML
    private TableColumn<Reel, String> reelLocationNameColumn;
    @FXML
    private TableColumn<Reel, Integer> reelLocationPageNumberColumn;
    @FXML
    private TableColumn<Reel, Integer> reelLocationPageSizeColumn;
    @FXML
    private TableColumn<Reel, Integer> reelLocationSlotNumberColumn;
    @FXML
    private TableColumn<Reel, Integer> reelDigitallyArchivedColumn;


    public void populateReelsList() {
        int foundUserReel;
        retrievedReelIdsBasedOnUserId.removeAll(retrievedReelIdsBasedOnUserId);

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT * FROM User_Reels  Where userId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            rs = ps.executeQuery();
            while (rs.next()) {
                foundUserReel = rs.getInt("reelId");
                retrievedReelIdsBasedOnUserId.add(foundUserReel);

            }
            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            databaseRetrievalError.setContentText(" there was an issue selecting reelzs based on the given user credentials please try again");
            databaseRetrievalError.showAndWait();
            e.printStackTrace();
        }

        getReelzs(retrievedReelIdsBasedOnUserId);

    }

    public void getReelzs(ArrayList<Integer> reelIdsList) {
        int userReelId;
        reelzsList.removeAll(reelzsList);
        Collections.sort(reelIdsList);
        for (Integer integer : reelIdsList) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT * FROM reelzs Where reelId = ? ";
                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , integer);
                rs = ps.executeQuery();
                while (rs.next()) {

                    userReelId = getUserReelId2(integer);
                    reelzsList.addAll(new Reel(
                            rs.getInt("reelId") ,
                            rs.getString("reelName") ,
                            rs.getString("reelArtist") ,
                            rs.getInt("reelYear") ,
                            rs.getString("reelType") ,
                            rs.getString("reelGenre") ,
                            rs.getString("reelRating") ,
                            rs.getInt("reelIsOriginalStatus") ,
                            setReelNotes(userReelId) ,
                            setReelOwnStatus(userReelId) ,
                            setReelWantStatus(userReelId) ,
                            setReelLocationName(userReelId) ,
                            setReelLocationPageNumber(userReelId) ,
                            setReelLocationPageSize(userReelId) ,
                            setReelLocationSlotNumber(userReelId) ,
                            setReelDigitallyArchivedStatus(userReelId)));

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


    }


    public String setReelNotes(int userReelId) {

        String foundNote = "";

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT note  FROM uniqueReelAttributes  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundNote = rs.getString("note");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundNote;
    }


    public int setReelOwnStatus(int userReelId) {

        int foundReelOwnStatus = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelOwnStatus  FROM uniqueReelAttributes  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelOwnStatus = rs.getInt("reelOwnStatus");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


        return foundReelOwnStatus;
    }


    public int setReelWantStatus(int userReelId) {

        int foundReelWantStatus = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelWantStatus  FROM uniqueReelAttributes  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelWantStatus = rs.getInt("reelWantStatus");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundReelWantStatus;
    }


    public String setReelLocationName(int userReelId) {

        String foundReelLocationName = "";

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelLocationName  FROM reelLocation  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelLocationName = rs.getString("reelLocationName");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundReelLocationName;
    }


    public int setReelLocationPageNumber(int userReelId) {

        int foundReelLocationPageNumber = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelLocationPageNumber  FROM reelLocation  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelLocationPageNumber = rs.getInt("reelLocationPageNumber");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundReelLocationPageNumber;
    }


    public int setReelLocationPageSize(int userReelId) {

        int foundReelLocationPageSize = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelLocationPageSize  FROM reelLocation  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelLocationPageSize = rs.getInt("reelLocationPageSize");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundReelLocationPageSize;
    }


    public int setReelLocationSlotNumber(int userReelId) {

        int foundReelLocationSlotNumber = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelLocationSlotNumber  FROM reelLocation  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelLocationSlotNumber = rs.getInt("reelLocationSlotNumber");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundReelLocationSlotNumber;


    }


    public int setReelDigitallyArchivedStatus(int userReelId) {

        int foundReelDigitallyArchivedStatus = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelDigitallyArchivedStatus  FROM uniqueReelAttributes  Where userReelId = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelDigitallyArchivedStatus = rs.getInt("reelDigitallyArchivedStatus");

                rs.close();
                ps.close();
                conn.close();

            }
            conn.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundReelDigitallyArchivedStatus;


    }


    public int getUserReelId2(int reelId) {

        int userReelId = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT userReelId FROM User_Reels  Where userId = ? AND  reelId = ? ";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelId);

            rs = ps.executeQuery();
            if (rs.next()) {
                userReelId = rs.getInt("userReelId");
            } else {
                databaseRetrievalError.setContentText(" there was a problem retrieving the userReelId based on the provided credentials");
                databaseRetrievalError.showAndWait();
            }
            ps.close();
            conn.close();

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

        return userReelId;
    }


    //    single insert method
    public void verifyRequiredFieldsAreNotNull() {

        if (reelNameTextfield.getText().equals("") || reelArtistTextfield.getText().equals("") || reelYearTextfield.getText().equals("") || reelTypeTextfield.getText().equals("")
                || reelGenreTextField.getText().equals("") || reelLocationNameTextField.getText().equals("") || reelLocationPageNumberTextField.getText().equals("")
                || reelLocationPageSizeTextField.getText().equals("") || reelLocationSlotNumberTextField.getText().equals("")) {

            reelModificationError.setContentText("one or more of the required fields are empty");
            reelModificationError.showAndWait();


        } else {

            verifyReelIsNotDuplicate();

        }

    }

    public void verifyReelIsNotDuplicate() {

        int foundReelId;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelId FROM reelzs  Where reelName = ?  AND reelArtist = ? AND reelYear = ? AND reelType = ?";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelNameTextfield.getText());
            ps.setString(2 , reelArtistTextfield.getText());
            ps.setInt(3 , Integer.parseInt(reelYearTextfield.getText()));
            ps.setString(4 , reelTypeTextfield.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelId = rs.getInt("reelId");
                System.out.println("this works");
                rs.close();
                ps.close();
                conn.close();
                verifyUserReelIdIsNotDuplicate(foundReelId);
                populateReelsList();
            } else {
                rs.close();
                ps.close();
                conn.close();

                addReel();
            }


        } catch (Exception e) {
            databaseRetrievalError.setContentText(" there was a problem verifying that the new reel to add is not a duplicate");
            databaseRetrievalError.showAndWait();
            e.printStackTrace();
        }


    }

    public void verifyUserReelIdIsNotDuplicate(int reelIdToCheck) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT userReelId  FROM User_Reels  Where userId = ?  AND reelId  = ? ";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelIdToCheck);
            rs = ps.executeQuery();
            if (rs.next()) {

                System.out.println(" nothing to do");
                rs.close();
                ps.close();
                conn.close();
                populateReelsList();

            }

        } catch (Exception e) {
            databaseRetrievalError.setContentText(" there was a problem verifying that the new reel to add is not a duplicate");
            databaseRetrievalError.showAndWait();
            e.printStackTrace();
        }

        addUserReelIdRecord(reelIdToCheck);
    }

    public void addUserReelIdRecord(int reelIdToInsert) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "INSERT INTO  User_Reels  (userId,reelId) Values (?,?) ";

            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelIdToInsert);
            ps.execute();

            ps.close();
            conn.close();
            retrievedUserReelIdRecord(reelIdToInsert);


        } catch (Exception e) {
            databaseModificationError.setContentText(" there was a problem inserting the user reel Id record");
            databaseModificationError.showAndWait();
            e.printStackTrace();
        }

    }

    public void retrievedUserReelIdRecord(int reelIdToSearch) {

        int userReelId = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT userReelId  FROM User_Reels  Where userId = ?  AND reelId  = ? ";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelIdToSearch);
            rs = ps.executeQuery();
            if (rs.next()) {
                userReelId = rs.getInt("userReelId");
            } else {
                databaseRetrievalError.setContentText(" there was a problem retrieving the userReelId Record based on provided credentials");
                databaseRetrievalError.showAndWait();
            }

            ps.close();
            conn.close();
            insertUniqueReelAttributeRecord(userReelId);
            insertReelLocationRecord(userReelId);

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }


    }

    public void insertUniqueReelAttributeRecord(int userReelId) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "INSERT INTO  uniqueReelAttributes  (userReelId,note,reelOwnStatus,reelWantStatus,reelDigitallyArchivedStatus) Values (?,?,?,?,?) ";

            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            ps.setString(2 , reelNotesTextfield.getText());
            ps.setInt(3 , ConvertCheckboxResultToInt(reelOwnStatusCheckbox.isSelected()));
            ps.setInt(4 , ConvertCheckboxResultToInt(reelWantStatusCheckbox.isSelected()));
            ps.setInt(5 , ConvertCheckboxResultToInt(reelDigitallyArchivedStatusCheckBox.isSelected()));
            ps.execute();
            ps.close();
            conn.close();
            retrieveUniqueReelAttributeId(userReelId);


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

    }

    public void retrieveUniqueReelAttributeId(int userReelId) {

        int uniqueReelAttributeId = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT uniqueReelAttributeId FROM uniqueReelAttributes  Where userReelId = ? ";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);

            rs = ps.executeQuery();
            if (rs.next()) {
                uniqueReelAttributeId = rs.getInt("uniqueReelAttributeId");
            } else {
                databaseRetrievalError.setContentText(" there was a problem retrieving the uniqueReelAttributeId Record based on provided credentials");
                databaseRetrievalError.showAndWait();
            }
            ps.close();
            conn.close();
            updateUserReelRecordUniqueReelAttributeId(uniqueReelAttributeId , userReelId);


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }


    }


    public void updateUserReelRecordUniqueReelAttributeId(int uniqueReelAttributeId , int userReelId) {


        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");

            String sql = "UPDATE User_Reels SET uniqueReelAttributeId = ? " +
                    "WHERE userReelId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , uniqueReelAttributeId);
            ps.setInt(2 , userReelId);
            ps.executeUpdate();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();


        }


    }

    public void insertReelLocationRecord(int userReelId) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "INSERT INTO  reelLocation  (userReelId,reelLocationName,reelLocationPageNumber,reelLocationPageSize,reelLocationSlotNumber) Values (?,?,?,?,?) ";

            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            ps.setString(2 , reelLocationNameTextField.getText());
            ps.setInt(3 , Integer.parseInt(reelLocationPageNumberTextField.getText()));
            ps.setInt(4 , Integer.parseInt(reelLocationPageSizeTextField.getText()));
            ps.setInt(5 , Integer.parseInt(reelLocationSlotNumberTextField.getText()));
            ps.execute();
            ps.close();
            conn.close();
            retrieveReelLocationId(userReelId);

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }


    }

    public void retrieveReelLocationId(int userReelId) {

        int reelLocationId = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelLocationId FROM reelLocation  Where userReelId = ? ";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);

            rs = ps.executeQuery();
            if (rs.next()) {
                reelLocationId = rs.getInt("reelLocationId");
            } else {

                databaseRetrievalError.setContentText(" there was a problem retrieving the reelLocationId based on the given credentials");
                databaseRetrievalError.showAndWait();
            }
            ps.close();
            conn.close();
            updateUserReelRecordReelLocationId(reelLocationId , userReelId);

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }


    }

    public void updateUserReelRecordReelLocationId(int reelLocationId , int userReelId) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");

            String sql = "UPDATE User_Reels SET reelLocationId = ? " +
                    "WHERE userReelId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , reelLocationId);
            ps.setInt(2 , userReelId);
            ps.executeUpdate();
            ps.close();
            conn.close();

        } catch (SQLException e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();


        }


    }


    public int ConvertCheckboxResultToInt(Boolean checkboxState) {

        int convertedState = 0;

        if (checkboxState) {
            convertedState = 1;

        }

        return convertedState;

    }

    public void addReel() {
        Reel reel = new Reel();
        reel.setReelName(reelNameTextfield.getText());
        reel.setReelArtist(reelArtistTextfield.getText());
        reel.setReelYear(Integer.parseInt(reelYearTextfield.getText()));
        reel.setReelType(reelTypeTextfield.getText());
        reel.setReelGenre(reelGenreTextField.getText());
        reel.setReelRating(reelRatingTextfield.getText());
        reel.setReelIsOriginal(reelIsOriginalCheckBox.isSelected());
        reel.setReelNotes(reelNotesTextfield.getText());
        reel.setReelOwn(reelOwnStatusCheckbox.isSelected());
        reel.setReelWant(reelWantStatusCheckbox.isSelected());
        reel.setReelLocationName(reelLocationNameTextField.getText());
        reel.setReelLocationPageNumber(Integer.parseInt(reelLocationPageNumberTextField.getText()));
        reel.setReelLocationPageSize(Integer.parseInt(reelLocationPageSizeTextField.getText()));
        reel.setReelLocationSlotNumber(Integer.parseInt(reelLocationSlotNumberTextField.getText()));
        reel.setReelDigitallyArchived(reelDigitallyArchivedStatusCheckBox.isSelected());
        reelzsTable.getItems().addAll(reel);

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "INSERT INTO  reelzs  (reelName,reelArtist,reelYear,reelType, reelGenre,reelRating ,reelIsOriginalStatus) Values (?,?,?,?,?,?,?) ";

            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelNameTextfield.getText());
            ps.setString(2 , reelArtistTextfield.getText());
            ps.setInt(3 , Integer.parseInt(reelYearTextfield.getText()));
            ps.setString(4 , reelTypeTextfield.getText());
            ps.setString(5 , reelGenreTextField.getText());
            ps.setString(6 , reelRatingTextfield.getText());
            ps.setInt(7 , ConvertCheckboxResultToInt(reelIsOriginalCheckBox.isSelected()));
            ps.execute();
            ps.close();
            conn.close();
            retrieveNewReelId();

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }


        populateReelsList();
        successfulInsertNotification.setContentText("reel Successfully inserted");
        successfulInsertNotification.showAndWait();

    }

    public void retrieveNewReelId() {

        int foundNewReelId;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT reelId FROM reelzs  Where reelName = ?  AND reelArtist = ? AND reelYear = ? AND reelType = ?";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelNameTextfield.getText());
            ps.setString(2 , reelArtistTextfield.getText());
            ps.setInt(3 , Integer.parseInt(reelYearTextfield.getText()));
            ps.setString(4 , reelTypeTextfield.getText());
            rs = ps.executeQuery();
            if (rs.next()) {
                foundNewReelId = rs.getInt("reelId");
                System.out.println("this works");

                rs.close();
                ps.close();
                conn.close();
                addUserReelIdRecord(foundNewReelId);

            } else {
                rs.close();
                ps.close();
                conn.close();

                databaseRetrievalError.setContentText("there was a problem retrieving the newReelId");
                databaseRetrievalError.showAndWait();
            }

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

    }

//   update method( will be using checkbox boolean to int method from single add reel)

    public void updateReel() {

        if (reelIdTextfield.getText().equals("") || reelNameTextfield.getText().equals("") || reelArtistTextfield.getText().equals("") || reelYearTextfield.getText().equals("")
                || reelTypeTextfield.getText().equals("") || reelGenreTextField.getText().equals("") || reelLocationNameTextField.getText().equals("")
                || reelLocationPageNumberTextField.getText().equals("") || reelLocationPageSizeTextField.getText().equals("") || reelLocationSlotNumberTextField.getText().equals("")) {

            reelModificationError.setContentText("one or more of the required fields are empty");
            reelModificationError.showAndWait();
        } else {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");

                String sql = "UPDATE reelzs" + " SET  reelName = ?, " +
                        "reelArtist = ?," +
                        "reelYear = ?," +
                        "reelType = ?," +
                        "reelGenre = ? ," +
                        "reelRating = ?," +
                        "reelIsOriginalStatus = ?" +
                        "WHERE reelId = ?";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , reelNameTextfield.getText());
                ps.setString(2 , reelArtistTextfield.getText());
                ps.setInt(3 , Integer.parseInt(reelYearTextfield.getText()));
                ps.setString(4 , reelTypeTextfield.getText());
                ps.setString(5 , reelGenreTextField.getText());
                ps.setString(6 , reelRatingTextfield.getText());
                ps.setInt(7 , ConvertCheckboxResultToInt(reelIsOriginalCheckBox.isSelected()));
                ps.setInt(8 , Integer.parseInt(reelIdTextfield.getText()));
                ps.executeUpdate();
                ps.close();
                conn.close();
                updateUniqueReelAttributes();

            } catch (SQLException e) {
                databaseQueryExecutionError.setContentText(" there was a problem executing a query");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

            populateReelsList();
            successfulUpdateNotification.setContentText("reel successfully updated");
            successfulUpdateNotification.showAndWait();

        }

    }

    public void updateUniqueReelAttributes() {

        int userReelId = getUserReelId();

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");

            String sql = "UPDATE uniqueReelAttributes SET note = ?, " +
                    "reelOwnStatus = ?," +
                    "reelWantStatus = ?," +
                    "reelDigitallyArchivedStatus = ?" +
                    "WHERE   userReelId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelNotesTextfield.getText());
            ps.setInt(2 , ConvertCheckboxResultToInt(reelOwnStatusCheckbox.isSelected()));
            ps.setInt(3 , ConvertCheckboxResultToInt(reelWantStatusCheckbox.isSelected()));
            ps.setInt(4 , ConvertCheckboxResultToInt(reelDigitallyArchivedStatusCheckBox.isSelected()));
            ps.setInt(5 , userReelId);
            ps.executeUpdate();
            ps.close();
            conn.close();
            // remove index//

        } catch (SQLException e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();

        }

        updateReelLocationRecord(userReelId);

    }

    public int getUserReelId() {

        int userReelId = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT userReelId FROM User_Reels  Where userId = ? AND  reelId = ? ";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , Integer.parseInt(reelIdTextfield.getText()));

            rs = ps.executeQuery();
            if (rs.next()) {
                userReelId = rs.getInt("userReelId");
            } else {
                databaseRetrievalError.setContentText(" there was a problem retrieving the userReelId based on provided reelId TextField text");
                databaseRetrievalError.showAndWait();
            }
            ps.close();
            conn.close();


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

        return userReelId;
    }


    public void updateReelLocationRecord(int userReelId) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");

            String sql = "UPDATE reelLocation SET reelLocationName = ?, " +
                    "reelLocationPageNumber = ?," +
                    "reelLocationPageSize = ?," +
                    "reelLocationSlotNumber = ?" +
                    "WHERE   userReelId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelLocationNameTextField.getText());
            ps.setInt(2 , Integer.parseInt(reelLocationPageNumberTextField.getText()));
            ps.setInt(3 , Integer.parseInt(reelLocationPageSizeTextField.getText()));
            ps.setInt(4 , Integer.parseInt(reelLocationSlotNumberTextField.getText()));
            ps.setInt(5 , userReelId);
            ps.executeUpdate();
            ps.close();
            conn.close();
            // remove index//

        } catch (SQLException e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();

        }

    }

// delete method //

    public void checkNumberOfUserReelIdsWithReelId() {

        deleteWarning.setContentText("Are you sure you want to delete this reel ?");
        Optional<ButtonType> result = deleteWarning.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && reelIdTextfield.getText().equals("") || reelNameTextfield.getText().equals("") || reelArtistTextfield.getText().equals("") || reelYearTextfield.getText().equals("")
                || reelTypeTextfield.getText().equals("") || reelGenreTextField.getText().equals("") || reelLocationNameTextField.getText().equals("")
                || reelLocationPageNumberTextField.getText().equals("") || reelLocationPageSizeTextField.getText().equals("") || reelLocationSlotNumberTextField.getText().equals("")) {

            reelModificationError.setContentText("one or more of the required fields are empty");
            reelModificationError.showAndWait();
        } else {


            int resultSize = 0;

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT COUNT(*) AS Count  FROM User_Reels  Where reelId = ? ";
                ResultSet rs;
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , Integer.parseInt(reelIdTextfield.getText()));

                rs = ps.executeQuery();
                if (rs.next()) {
                    resultSize = rs.getInt("Count");
                    System.out.println("result size print out next Line (line 1073");
                    System.out.println(resultSize);


                }


                ps.close();
                rs.close();
                conn.close();


            } catch (Exception e) {
                databaseQueryExecutionError.setContentText(" there was a problem executing a query");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();
            }

            if (resultSize == 1) {
                int retrievedUserReelId = getUserReelId();
                removeReelLocationRecord(retrievedUserReelId);
                removeUniqueReelAttributeRecords(retrievedUserReelId);
                removeUserReelIdRecord(retrievedUserReelId);
                deleteReel();
                populateReelsList();

            } else if (resultSize > 1) {
                int retrievedUserReelId = getUserReelId();
                removeReelLocationRecord(retrievedUserReelId);
                removeUniqueReelAttributeRecords(retrievedUserReelId);
                removeUserReelIdRecord(retrievedUserReelId);
                populateReelsList();

            }

            successfulDeleteNotification.setContentText("reel successfully deleted");
            successfulDeleteNotification.showAndWait();

        }
    }


    public void removeUniqueReelAttributeRecords(int uniqueReelAttributeId) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "DELETE FROM uniqueReelAttributes  WHERE uniqueReelAttributeId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , uniqueReelAttributeId);
            ps.execute();
            ps.close();
            conn.close();
            System.out.println("Data has been Deleted successfully(line 1191)");

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem deleting the uniqueReelAttributes record based on the give uniqueReelAttributeId");
            databaseQueryExecutionError.showAndWait();

            e.printStackTrace();

        }

    }

    public void removeReelLocationRecord(int retrievedUserReelId) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "Delete  From reelLocation   WHERE  reelLocationId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , retrievedUserReelId);
            ps.execute();
            System.out.println("Data has been Deleted successfully");
            ps.close();
            conn.close();

        } catch (Exception e) {

            databaseQueryExecutionError.setContentText(" there was a problem deleting  the selected reel Location Record");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();

        }


    }

    public void removeUserReelIdRecord(int foundUserReelId) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "Delete  From User_Reels   WHERE  userReelId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , foundUserReelId);
            ps.execute();
            System.out.println("Data has been Deleted successfully");
            ps.close();
            conn.close();

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText("there was a problem deleting  the selected userReelId record");
            databaseQueryExecutionError.showAndWait();
        }


    }

    public void deleteReel() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "Delete  From reelzs   WHERE  reelId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , Integer.parseInt(reelIdTextfield.getText()));
            ps.execute();
            System.out.println("Data has been Deleted successfully");
            ps.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
            databaseQueryExecutionError.setContentText("there was a problem deleting  the selected reel");
            databaseQueryExecutionError.showAndWait();

        }

    }

//    bulk import code will go here   //

    public void advEnter() throws IOException {

        FileChooser csvImport = new FileChooser();
        csvImport.setInitialDirectory(new File("."));
        csvImport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        file = csvImport.showOpenDialog(null);

        Scanner fileIn = new Scanner(file);
        if (file.isFile()) {

            blkImport();

        } else {

            fileLoadingError.setContentText("That was not a valid file please make sure the file ends in .csv and try again");
            fileLoadingError.showAndWait();
        }
        fileIn.close();


    }

    public void blkImport() {
        bulkReelList.removeAll(bulkReelList);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String Line;

            while ((Line = br.readLine()) != null) {

                bulkArray = Line.split(",");
                Reel bulkReel = new Reel();
                bulkReel.setReelName(bulkArray[0]);
                bulkReel.setReelArtist(bulkArray[1]);
                bulkReel.setReelYear(Integer.parseInt(bulkArray[2]));
                bulkReel.setReelType(bulkArray[3]);
                bulkReel.setReelGenre(bulkArray[4]);
                bulkReel.setReelRating(bulkArray[5]);
                bulkReel.setReelIsOriginal(ConvertIntToBoolean(Integer.parseInt(bulkArray[6])));
                bulkReel.setReelNotes(bulkArray[7]);
                bulkReel.setReelOwn(ConvertIntToBoolean(Integer.parseInt(bulkArray[8])));
                bulkReel.setReelWant(ConvertIntToBoolean(Integer.parseInt(bulkArray[9])));
                bulkReel.setReelLocationName(bulkArray[10]);
                bulkReel.setReelLocationPageNumber(Integer.parseInt(bulkArray[11]));
                bulkReel.setReelLocationPageSize(Integer.parseInt(bulkArray[12]));
                bulkReel.setReelLocationSlotNumber(Integer.parseInt(bulkArray[13]));
                bulkReel.setReelDigitallyArchived(ConvertIntToBoolean(Integer.parseInt(bulkArray[14])));
                bulkReelList.add(bulkReel);

            }

        } catch (IOException e) {

            fileReadingError.setContentText(" there was a problem reading the given csv file please make sure all fields(excluding reel notes) have a value");
            fileReadingError.showAndWait();
            e.printStackTrace();
        }

        checkForDuplicateReelzs(bulkReelList);

    }

    public void checkForDuplicateReelzs(ArrayList<Reel> bulkReelList) {
        duplicateBulkReelListForUserReelDuplicateCheck.removeAll(duplicateBulkReelListForUserReelDuplicateCheck);
        approvedBulkReelzsList.removeAll(approvedBulkReelzsList);
        bulkReelList2.removeAll(bulkReelList2);

        for (Reel reel : bulkReelList) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT reelId FROM reelzs  Where reelName = ?  AND reelArtist = ? AND reelYear = ? AND reelType = ?";
                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , reel.getReelName());
                ps.setString(2 , reel.getReelArtist());
                ps.setInt(3 , reel.getReelYear());
                ps.setString(4 , reel.getReelType());
                rs = ps.executeQuery();
                if (rs.next()) {
                    Reel duplicateReel = new Reel();
                    duplicateReel.setReelId(rs.getInt("reelId"));
                    duplicateReel.setReelName(reel.getReelName());
                    duplicateReel.setReelArtist(reel.getReelArtist());
                    duplicateReel.setReelYear(reel.getReelYear());
                    duplicateReel.setReelType(reel.getReelType());
                    duplicateReel.setReelGenre(reel.getReelGenre());
                    duplicateReel.setReelRating(reel.getReelRating());
                    duplicateReel.setReelIsOriginal(reel.getReelIsOriginal2());
                    duplicateReel.setReelNotes(reel.getReelNotes());
                    duplicateReel.setReelOwn(reel.getReelOwn2());
                    duplicateReel.setReelWant(reel.getReelWant2());
                    duplicateReel.setReelLocationName(reel.getReelLocationName());
                    duplicateReel.setReelLocationPageNumber(reel.getReelLocationPageNumber());
                    duplicateReel.setReelLocationPageSize(reel.getReelLocationPageSize());
                    duplicateReel.setReelLocationSlotNumber(reel.getReelLocationSlotNumber());
                    duplicateReel.setReelDigitallyArchived(reel.getReelDigitallyArchived2());
                    duplicateBulkReelListForUserReelDuplicateCheck.add(duplicateReel);
                    System.out.println("this works");


                } else if (!rs.next()) {

                    Reel bulkApprovedReel = new Reel();
                    bulkApprovedReel.setReelName(reel.getReelName());
                    bulkApprovedReel.setReelArtist(reel.getReelArtist());
                    bulkApprovedReel.setReelYear(reel.getReelYear());
                    bulkApprovedReel.setReelType(reel.getReelType());
                    bulkApprovedReel.setReelGenre(reel.getReelGenre());
                    bulkApprovedReel.setReelRating(reel.getReelRating());
                    bulkApprovedReel.setReelIsOriginal(reel.getReelIsOriginal2());
                    bulkApprovedReel.setReelNotes(reel.getReelNotes());
                    bulkApprovedReel.setReelOwn(reel.getReelOwn2());
                    bulkApprovedReel.setReelWant(reel.getReelWant2());
                    bulkApprovedReel.setReelLocationName(reel.getReelLocationName());
                    bulkApprovedReel.setReelLocationPageNumber(reel.getReelLocationPageNumber());
                    bulkApprovedReel.setReelLocationPageSize(reel.getReelLocationPageSize());
                    bulkApprovedReel.setReelLocationSlotNumber(reel.getReelLocationSlotNumber());
                    bulkApprovedReel.setReelDigitallyArchived(reel.getReelDigitallyArchived2());
                    approvedBulkReelzsList.add(bulkApprovedReel);
                    System.out.println(approvedBulkReelzsList);

                }
                ps.close();
                rs.close();
                conn.close();


            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem retrieving a reel based on given credentials");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        verifyBulkUserReelIdIsNotDuplicate(duplicateBulkReelListForUserReelDuplicateCheck);
        addBulkReelzs(approvedBulkReelzsList);

    }

    public void verifyBulkUserReelIdIsNotDuplicate(ArrayList<Reel> duplicateBulkReelListForUserReelDuplicateCheck) {
        duplicateUserReelIdsToInsert.removeAll(duplicateUserReelIdsToInsert);

        for (Reel reel : duplicateBulkReelListForUserReelDuplicateCheck) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT userReelId FROM User_Reels Where  userId = ? AND   reelId = ?";

                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , User.getUserId());
                ps.setInt(2 , reel.getReelId());
                rs = ps.executeQuery();
                if (rs.next()) {

                    databaseRetrievalError.setContentText(" one or more instances of  this combination of the user id and reel id already exist ignoring record");
                    databaseRetrievalError.showAndWait();

                } else {

                    duplicateUserReelIdsToInsert.add(reel);

                }
                ps.close();
                rs.close();
                conn.close();


            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem verifying if one or more userReelIds are duplicates");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        addBulkUserReelIdRecord(duplicateUserReelIdsToInsert);

    }

    public void addBulkUserReelIdRecord(ArrayList<Reel> duplicateUserReelIdsToInsert) {

        for (Reel reel : duplicateUserReelIdsToInsert) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "INSERT INTO   User_Reels  (userId,reelId) Values  (?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , User.getUserId());
                ps.setInt(2 , reel.getReelId());
                ps.execute();
                ps.close();
                conn.close();
                System.out.println("successful data insertion (line 1072)");

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting  one or more userReelId record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        insertBulkReelLocationRecords2(duplicateUserReelIdsToInsert);
        insertBulkUniqueReelAttributeRecords2(duplicateUserReelIdsToInsert);

    }

    public void insertBulkUniqueReelAttributeRecords2(ArrayList<Reel> duplicateUserReelIdsToInsert) {
        bulkierReliesForReeLocationIdRetrieval.removeAll(bulkierReliesForReeLocationIdRetrieval);
        for (Reel reel : duplicateUserReelIdsToInsert) {
            int userReelIdForInsert = retrieveUserReelId2(reel.getReelId());
            bulkierReliesForUniqueReelAttributeRetrieval.add(userReelIdForInsert);

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "INSERT INTO  uniqueReelAttributes  (userReelId,note,reelOwnStatus,reelWantStatus,reelDigitallyArchivedStatus) VALUES (?,?,?,?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , userReelIdForInsert);
                ps.setString(2 , reel.getReelNotes());
                ps.setInt(3 , ConvertCheckboxResultToInt(reel.getReelOwn2()));
                ps.setInt(4 , ConvertCheckboxResultToInt(reel.getReelWant2()));
                ps.setInt(5 , ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()));
                ps.execute();
                ps.close();
                conn.close();


            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more uniqueReelAttributes record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        retrieveBulkUniqueReelAttributeRecordId(bulkierReliesForUniqueReelAttributeRetrieval);

    }


    public void insertBulkReelLocationRecords2(ArrayList<Reel> duplicateUserReelIdsToInsert) {
        bulkierReliesForReeLocationIdRetrieval.removeAll(bulkierReliesForReeLocationIdRetrieval);

        for (Reel reel : duplicateUserReelIdsToInsert) {
            int userReelIdForInsert = retrieveUserReelId2(reel.getReelId());
            bulkierReliesForReeLocationIdRetrieval.add(userReelIdForInsert);

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "INSERT INTO  reelLocation  (userReelId,reelLocationName,reelLocationPageNumber,reelLocationPageSize,reelLocationSlotNumber) VALUES (?,?,?,?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , userReelIdForInsert);
                ps.setString(2 , reel.getReelLocationName());
                ps.setInt(3 , reel.getReelLocationPageNumber());
                ps.setInt(4 , reel.getReelLocationPageSize());
                ps.setInt(5 , reel.getReelLocationSlotNumber());
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more reelLocation record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        retrieveBulkReelLocationId(bulkierReliesForReeLocationIdRetrieval);

    }

    public int retrieveUserReelId2(int reelId) {

        int userReelId = 0;
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT userReelId From  User_Reels  WHERE  userId = ? And reelId = ?";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                userReelId = rs.getInt("userReelId");
            }
            ps.close();
            rs.close();
            conn.close();

        } catch (Exception e) {

            databaseQueryExecutionError.setContentText("there was a problem retrieving  the  userReelId record based on given credentials");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();

        }

        return userReelId;
    }


    public void retrieveBulkUniqueReelAttributeRecordId(ArrayList<Integer> duplicateUserReelIdsForUniqueReelAttributeRetrieval) {

        int uniqueReelAttributeId;
        for (Integer integer : duplicateUserReelIdsForUniqueReelAttributeRetrieval) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT uniqueReelAttributeId From uniqueReelAttributes   WHERE  userReelId = ? ";
                ResultSet rs;
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , integer);
                rs = ps.executeQuery();
                if (rs.next()) {
                    uniqueReelAttributeId = rs.getInt("uniqueReelAttributeId");
                    rs.close();
                    ps.close();
                    conn.close();
                    updateUserReelRecordUniqueReelAttributeId(uniqueReelAttributeId , integer);
                }

            } catch (Exception e) {
                databaseQueryExecutionError.setContentText("there was a problem retrieving one or more uniqueReelAttribute record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();
            }

        }


        populateReelsList();
        bulkSuccessfulInsertNotification.setContentText("bulk reelzs insert successful");
        bulkSuccessfulInsertNotification.showAndWait();

    }


    public void retrieveBulkReelLocationId(ArrayList<Integer> bulkierReliesForReeLocationIdRetrieval) {

        int reelLocationId;
        for (Integer integer : bulkierReliesForReeLocationIdRetrieval) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT reelLocationId From reelLocation  WHERE  userReelId = ? ";
                ResultSet rs;
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , integer);
                rs = ps.executeQuery();
                if (rs.next()) {
                    reelLocationId = rs.getInt("reelLocationId");
                    rs.close();
                    ps.close();
                    conn.close();
                    updateUserReelRecordReelLocationId(reelLocationId , integer);
                }

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem retrieving one or more reelLocation record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

    }

    public void addBulkReelzs(ArrayList<Reel> bulkReelzsForInsertReel) {

        for (Reel reel : bulkReelzsForInsertReel) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "INSERT INTO  reelzs  (reelName,reelArtist,reelYear,reelType, reelGenre, reelRating ,reelIsOriginalStatus) Values (?,?,?,?,?,?,?) ";

                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , reel.getReelName());
                ps.setString(2 , reel.getReelArtist());
                ps.setInt(3 , reel.getReelYear());
                ps.setString(4 , reel.getReelType());
                ps.setString(5 , reel.getReelGenre());
                ps.setString(6 , reel.getReelRating());
                ps.setInt(7 , ConvertCheckboxResultToInt(reel.getReelIsOriginal2()));
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more reel");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }
        }

        retrieveBulkNewReelIds(bulkReelzsForInsertReel);
    }

    public void retrieveBulkNewReelIds(ArrayList<Reel> bulkReelzsForInsertReel) {
        int foundNewBulkReelId;

        for (Reel reel : bulkReelzsForInsertReel) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT reelId FROM reelzs  Where reelName = ?  AND reelArtist = ? AND reelYear = ? AND reelType = ? AND reelGenre = ?";
                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , reel.getReelName());
                ps.setString(2 , reel.getReelArtist());
                ps.setInt(3 , reel.getReelYear());
                ps.setString(4 , reel.getReelType());
                ps.setString(5 , reel.getReelGenre());
                rs = ps.executeQuery();
                if (rs.next()) {
                    foundNewBulkReelId = rs.getInt("reelId");
                    repackageBulkReelzsList(foundNewBulkReelId , reel);

                }
                rs.close();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem selecting one or more reelzs record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        addBulkUserReelIdRecord2(bulkReelList2);

    }

    public void repackageBulkReelzsList(int bulkReelzsId , Reel reel) {

        Reel bulkReel2 = new Reel();
        bulkReel2.setReelId(bulkReelzsId);
        bulkReel2.setReelName(reel.getReelName());
        bulkReel2.setReelArtist(reel.getReelArtist());
        bulkReel2.setReelYear(reel.getReelYear());
        bulkReel2.setReelType(reel.getReelType());
        bulkReel2.setReelGenre(reel.getReelGenre());
        bulkReel2.setReelRating(reel.getReelRating());
        bulkReel2.setReelNotes(reel.getReelNotes());
        bulkReel2.setReelIsOriginal(reel.getReelIsOriginal2());
        bulkReel2.setReelOwn(reel.getReelOwn2());
        bulkReel2.setReelWant(reel.getReelWant2());
        bulkReel2.setReelLocationName(reel.getReelLocationName());
        bulkReel2.setReelLocationPageNumber(reel.getReelLocationPageNumber());
        bulkReel2.setReelLocationPageSize(reel.getReelLocationPageSize());
        bulkReel2.setReelLocationSlotNumber(reel.getReelLocationSlotNumber());
        bulkReel2.setReelDigitallyArchived(reel.getReelDigitallyArchived2());
        bulkReelList2.add(bulkReel2);

    }


    public void addBulkUserReelIdRecord2(ArrayList<Reel> bulkReelList2) {

        for (Reel reel : bulkReelList2) {

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "INSERT INTO   User_Reels  (userId,reelId) Values  (?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , User.getUserId());
                ps.setInt(2 , reel.getReelId());
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more  userReelId record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        insertBulkReelLocationRecords(bulkReelList2);
        insertBulkUniqueReelAttributeRecords(bulkReelList2);

    }


    public void insertBulkUniqueReelAttributeRecords(ArrayList<Reel> bulkReelList2) {
        bulkUserReelIdsForUniqueReelAttributeRetrieval2.removeAll(bulkUserReelIdsForUniqueReelAttributeRetrieval2);

        for (Reel reel : bulkReelList2) {
            int userReelIdForInsert = retrieveUserReelId2(reel.getReelId());
            bulkUserReelIdsForUniqueReelAttributeRetrieval2.add(userReelIdForInsert);

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "INSERT INTO  uniqueReelAttributes  (userReelId,note,reelOwnStatus,reelWantStatus,reelDigitallyArchivedStatus) VALUES (?,?,?,?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , userReelIdForInsert);
                ps.setString(2 , reel.getReelNotes());
                ps.setInt(3 , ConvertCheckboxResultToInt(reel.getReelOwn2()));
                ps.setInt(4 , ConvertCheckboxResultToInt(reel.getReelWant2()));
                ps.setInt(5 , ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()));
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {
                databaseQueryExecutionError.setContentText("there was a problem inserting one or more uniqueReelAttributes record ");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();
            }


        }

        retrieveBulkUniqueReelAttributeRecordId(bulkUserReelIdsForUniqueReelAttributeRetrieval2);

    }


    public void insertBulkReelLocationRecords(ArrayList<Reel> bulkReelList2) {
        bulkierReliesForReeLocationIdRetrieval2.removeAll(bulkierReliesForReeLocationIdRetrieval2);

        for (Reel reel : bulkReelList2) {
            int userReelIdForInsert = retrieveUserReelId2(reel.getReelId());
            bulkierReliesForReeLocationIdRetrieval2.add(userReelIdForInsert);

            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "INSERT INTO  reelLocation  (userReelId,reelLocationName,reelLocationPageNumber,reelLocationPageSize,reelLocationSlotNumber) VALUES (?,?,?,?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , userReelIdForInsert);
                ps.setString(2 , reel.getReelLocationName());
                ps.setInt(3 , reel.getReelLocationPageNumber());
                ps.setInt(4 , reel.getReelLocationPageSize());
                ps.setInt(5 , reel.getReelLocationSlotNumber());
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more reelLocation record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        retrieveBulkReelLocationId(bulkierReliesForReeLocationIdRetrieval2);

    }


    public boolean ConvertIntToBoolean(int status) {
        boolean returnedStatus = false;
        if (status == 1) {
            returnedStatus = true;

        }

        return returnedStatus;
    }


// export reelzs  //


    public void writeCsv() throws Exception {

        Writer writer = null;
        FileChooser csvExport = new FileChooser();
        csvExport.setInitialDirectory(new File("."));
        csvExport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        file2 = csvExport.showSaveDialog(null);
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes ,Reel Own Status,ReelWant Status,Reel Location Name, Reel Location Page Number , Reel Location Page Size, Reel Slot Number , ReelDigitallyArchived Status" + "\n";
        try {

            writer = new BufferedWriter(new FileWriter(file2));
            writer.write(headers);

            String hiddenBackupPath;
            for (Reel reel : reelzsList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," + ConvertCheckboxResultToInt(reel.getReelOwn2()) + "," +
                        ConvertCheckboxResultToInt(reel.getReelWant2()) + "," + reel.getReelLocationName() + "," + reel.getReelLocationPageNumber() + "," + reel.getReelLocationPageSize() +
                        "," + reel.getReelLocationSlotNumber() + "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);


            }
            writer.close();

            String OriginalPath = file2.toPath().toString();
            int pathInsertIndex = OriginalPath.indexOf(".");

            hiddenBackupPath = convertOriginalPathToHiddenBackupPath(file2.toPath().toString() , pathInsertIndex);
            csvBackup(hiddenBackupPath);

        } catch (IOException ex) {

            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();


        }

    }

    public String convertOriginalPathToHiddenBackupPath(String s , int pathInsertIndex) {
        String convertedPath;
        int newPathInsertIndex = pathInsertIndex - 1;
        String backUpText = "backUp";
        StringBuffer resString = new StringBuffer(s);
        resString.insert(newPathInsertIndex , backUpText);
        convertedPath = resString.toString();
        System.out.println(convertedPath);

        return convertedPath;
    }

    public void csvBackup(String hiddenBackupPath) throws Exception {
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes ,Reel Own Status,ReelWant Status,Reel Location Name, Reel Location Page Number , Reel Location Page Size, Reel Slot Number , ReelDigitallyArchived Status" + "\n";
        Writer writer = null;

        try {
            file3 = new File(hiddenBackupPath);
            writer = new BufferedWriter(new FileWriter(file3));
            writer.write(headers);
            for (Reel reel : reelzsList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," + ConvertCheckboxResultToInt(reel.getReelOwn2()) + "," +
                        ConvertCheckboxResultToInt(reel.getReelWant2()) + "," + reel.getReelLocationName() + "," + reel.getReelLocationPageNumber() + "," + reel.getReelLocationPageSize() +
                        "," + reel.getReelLocationSlotNumber() + "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);
                Path path = Paths.get(hiddenBackupPath);
                Files.setAttribute(path , "dos:hidden" ,
                        true , LinkOption.NOFOLLOW_LINKS);

            }
            writer.close();

        } catch (IOException ex) {


            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();

        }
        successfulReelzsExport.setContentText("reelzs successfully exported");
        successfulReelzsExport.showAndWait();

    }


    public void cdExport(){
        reelzsCdExportList.removeAll(reelzsCdExportList);

        for (Reel reel : reelzsList) {

            if (reel.getReelType().contains("cd")) {

                reelzsCdExportList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating() ,
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) ,
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));



            }

        }

        Writer writer = null;
        FileChooser csvExport = new FileChooser();
        csvExport.setInitialDirectory(new File("."));
        csvExport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        file4 = csvExport.showSaveDialog(null);
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes ,Reel Own Status,ReelWant Status,Reel Location Name, Reel Location Page Number , Reel Location Page Size, Reel Slot Number , ReelDigitallyArchived Status" + "\n";
        try {

            writer = new BufferedWriter(new FileWriter(file4));
            writer.write(headers);

            String hiddenBackupPath;
            for (Reel reel : reelzsCdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," + ConvertCheckboxResultToInt(reel.getReelOwn2()) + "," +
                        ConvertCheckboxResultToInt(reel.getReelWant2()) + "," + reel.getReelLocationName() + "," + reel.getReelLocationPageNumber() + "," + reel.getReelLocationPageSize() +
                        "," + reel.getReelLocationSlotNumber() + "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";
                System.out.println(text);
                writer.write(text);


            }
            writer.close();

            String OriginalPath = file4.toPath().toString();
            int pathInsertIndex = OriginalPath.indexOf(".");

            hiddenBackupPath = convertOriginalPathToHiddenBackupPath(file4.toPath().toString() , pathInsertIndex);
            cdExportBackup(hiddenBackupPath);

        } catch (IOException ex) {

            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();


        }






    }

    public void cdExportBackup(String hiddenBackupPath){

        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes ,Reel Own Status,ReelWant Status,Reel Location Name, Reel Location Page Number , Reel Location Page Size, Reel Slot Number , ReelDigitallyArchived Status" + "\n";
        Writer writer = null;

        try {
            file5 = new File(hiddenBackupPath);
            writer = new BufferedWriter(new FileWriter(file5));
            writer.write(headers);
            for (Reel reel : reelzsCdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," + ConvertCheckboxResultToInt(reel.getReelOwn2()) + "," +
                        ConvertCheckboxResultToInt(reel.getReelWant2()) + "," + reel.getReelLocationName() + "," + reel.getReelLocationPageNumber() + "," + reel.getReelLocationPageSize() +
                        "," + reel.getReelLocationSlotNumber() + "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);
                Path path = Paths.get(hiddenBackupPath);
                Files.setAttribute(path , "dos:hidden" ,
                        true , LinkOption.NOFOLLOW_LINKS);

            }
            writer.close();

        } catch (IOException ex) {


            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();

        }
        successfulReelzsExport.setContentText("reelzs successfully exported");
        successfulReelzsExport.showAndWait();


    }


    public void dvdExport(){
        reelzsDvdExportList.removeAll(reelzsDvdExportList);

        for (Reel reel : reelzsList) {

            if (reel.getReelType().contains("dvd")) {

                reelzsDvdExportList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating() ,
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) ,
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber() ,
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));



            }

        }

        Writer writer = null;
        FileChooser csvExport = new FileChooser();
        csvExport.setInitialDirectory(new File("."));
        csvExport.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files" , "*csv"));
        file6 = csvExport.showSaveDialog(null);
        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes ,Reel Own Status,ReelWant Status,Reel Location Name, Reel Location Page Number , Reel Location Page Size, Reel Slot Number , ReelDigitallyArchived Status" + "\n";
        try {

            writer = new BufferedWriter(new FileWriter(file6));
            writer.write(headers);

            String hiddenBackupPath;
            for (Reel reel : reelzsDvdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," + ConvertCheckboxResultToInt(reel.getReelOwn2()) + "," +
                        ConvertCheckboxResultToInt(reel.getReelWant2()) + "," + reel.getReelLocationName() + "," + reel.getReelLocationPageNumber() + "," + reel.getReelLocationPageSize() +
                        "," + reel.getReelLocationSlotNumber() + "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";
                System.out.println(text);
                writer.write(text);


            }
            writer.close();

            String OriginalPath = file6.toPath().toString();
            int pathInsertIndex = OriginalPath.indexOf(".");

            hiddenBackupPath = convertOriginalPathToHiddenBackupPath(file6.toPath().toString() , pathInsertIndex);
            dvdExportBackup(hiddenBackupPath);

        } catch (IOException ex) {

            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();


        }







    }

    public void dvdExportBackup(String hiddenBackupPath){

        String headers = "Reel Name ,Reel Artist ,Reel Year, Reel Type ,Reel Genre,Reel Rating, Reel Original ,Reel Notes ,Reel Own Status,ReelWant Status,Reel Location Name, Reel Location Page Number , Reel Location Page Size, Reel Slot Number , ReelDigitallyArchived Status" + "\n";
        Writer writer = null;

        try {
            file7 = new File(hiddenBackupPath);
            writer = new BufferedWriter(new FileWriter(file7));
            writer.write(headers);
            for (Reel reel : reelzsDvdExportList) {

                String text = reel.getReelName() + "," + reel.getReelArtist() + "," + reel.getReelYear() + "," + reel.getReelType() + "," + reel.getReelGenre() + "," + reel.getReelRating() + ","
                        + ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) + "," + reel.getReelNotes() + "," + ConvertCheckboxResultToInt(reel.getReelOwn2()) + "," +
                        ConvertCheckboxResultToInt(reel.getReelWant2()) + "," + reel.getReelLocationName() + "," + reel.getReelLocationPageNumber() + "," + reel.getReelLocationPageSize() +
                        "," + reel.getReelLocationSlotNumber() + "," + ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) + "\n";

                writer.write(text);
                Path path = Paths.get(hiddenBackupPath);
                Files.setAttribute(path , "dos:hidden" ,
                        true , LinkOption.NOFOLLOW_LINKS);

            }
            writer.close();

        } catch (IOException ex) {


            fileWritingError.setContentText(" there was a problem exporting your reelzs please try again");
            fileWritingError.showAndWait();
            ex.printStackTrace();

        }
        successfulReelzsExport.setContentText("reelzs successfully exported");
        successfulReelzsExport.showAndWait();




    }



    public void searchTypeChooserFunction() {
        if (advancedSearchFiltersChoiceBox.getValue().equals("Standard")) {
            standardReelzsSearchMethod();

        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Cd")){
            cdSearchMethod();
        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Dvd")){
            dvdSearchMethod();
        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Artist")) {
            artistSearchMethod();

        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Year")) {
            yearSearchMethod();

        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Is Original Reel")){

            isOriginalMethod();

        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Is Not Original Reel")){

            isNotOriginalMethod();

        }

        if (advancedSearchFiltersChoiceBox.getValue().equals("Type")) {
            typeSearchMethod();
        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Genre")) {
            genreSearchMethod();
        }

        if (advancedSearchFiltersChoiceBox.getValue().equals("Owned Reelzs")) {
            ownReelzsSearchMethod();

        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Wanted Reelzs")) {
            wantReelzsSearchMethod();

        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Location")) {
            locationSearchMethod();
        }
        if(advancedSearchFiltersChoiceBox.getValue().equals("Digitally Archived Reelzs")){
            digitallyArchivedSearchMethod();
        }
        if (advancedSearchFiltersChoiceBox.getValue().equals("Not Digitally Archived")){
            notDigitallyArchivedSearchMethod();
        }


    }

    public void standardReelzsSearchMethod() {

        reelzsWantList.removeAll(reelzsWantList);
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);
        reelzsTable.setItems(reelzsList);
        FilteredList<Reel> filteredData = new FilteredList<>(reelzsList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredData.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }

                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelRating().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }

                if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);

            }));


            SortedList<Reel> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void cdSearchMethod(){
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (reel.getReelType().contains("cd")) {

                reelzsCdList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber(),
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsCdList);

            }

        }


        FilteredList<Reel> filteredCdReelzs = new FilteredList<>(reelzsCdList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredCdReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredCdReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void dvdSearchMethod(){
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);


        for (Reel reel : reelzsList) {

            if (reel.getReelType().toLowerCase().contains("dvd")) {

                reelzsDvdList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber(),
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsDvdList);

            }

        }


        FilteredList<Reel> filteredDvdReelzs = new FilteredList<>(reelzsDvdList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredDvdReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredDvdReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });



    }

    public void artistSearchMethod() {
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);


        FilteredList<Reel> filteredArtists = new FilteredList<>(reelzsList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredArtists.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reel.getReelArtist().toLowerCase().contains(newValue)) {
                    return true;
                }

                return reel.getReelArtist().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredArtists);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }


    public void yearSearchMethod() {

        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        FilteredList<Reel> filteredYears = new FilteredList<>(reelzsList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredYears.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }


                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }

                return String.valueOf(reel.getReelYear()).contains(newValue);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredYears);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });


    }

    public void isOriginalMethod(){

        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) == 1) {

                reelzsIsOriginalList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber(),
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsIsOriginalList);

            }

        }


        FilteredList<Reel> filteredOriginalReelzs = new FilteredList<>(reelzsIsOriginalList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredOriginalReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredOriginalReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }


    public void isNotOriginalMethod(){

        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsIsOriginalList.removeAll(reelzsIsOriginalList);
        reelzsNotOriginalList.removeAll(reelzsNotOriginalList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelIsOriginal2()) == 0) {

                reelzsNotOriginalList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber(),
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsNotOriginalList);

            }

        }


        FilteredList<Reel> filteredNotOriginalReelzs = new FilteredList<>(reelzsNotOriginalList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredNotOriginalReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredNotOriginalReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }


    public void typeSearchMethod() {
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        FilteredList<Reel> filteredTypes = new FilteredList<>(reelzsList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredTypes.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reel.getReelType().toLowerCase().contains(newValue)) {
                    return true;
                }

                return reel.getReelType().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredTypes);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });


    }


    public void genreSearchMethod() {

        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        FilteredList<Reel> filteredGenres = new FilteredList<>(reelzsList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredGenres.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reel.getReelGenre().toLowerCase().contains(newValue)) {
                    return true;
                }

                return reel.getReelGenre().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredGenres);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });


    }

    public void ownReelzsSearchMethod() {
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelOwn2()) == 1) {

                reelzsOwnList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber(),
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));

                reelzsTable.setItems(reelzsOwnList);

            }

        }


        FilteredList<Reel> filteredOwnedReelzs = new FilteredList<>(reelzsOwnList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredOwnedReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredOwnedReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }


    public void wantReelzsSearchMethod() {

        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);

        for (Reel reel : reelzsList) {

            if (ConvertCheckboxResultToInt(reel.getReelWant2()) == 1) {

                reelzsWantList.addAll(new Reel(
                        reel.getReelId() ,
                        reel.getReelName() ,
                        reel.getReelArtist() ,
                        reel.getReelYear() ,
                        reel.getReelType() ,
                        reel.getReelGenre() ,
                        reel.getReelRating(),
                        ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                        reel.getReelNotes() ,
                        ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                        ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                        reel.getReelLocationName() ,
                        reel.getReelLocationPageNumber() ,
                        reel.getReelLocationPageSize() ,
                        reel.getReelLocationSlotNumber(),
                        ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));



                reelzsTable.setItems(reelzsWantList);

            }

        }

        FilteredList<Reel> filteredWantedReelzs = new FilteredList<>(reelzsWantList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredWantedReelzs.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reel.getReelId()).contains(newValue)) {
                    return true;
                }

                if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }
                if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                    return true;
                }
                if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                    return true;

                }

                if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                if(reel.getReelRating().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }



                if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredWantedReelzs);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

    public void locationSearchMethod() {
        reelzsOwnList.removeAll(reelzsOwnList);
        reelzsWantList.removeAll(reelzsWantList);
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
        reelzsArchivedList.removeAll(reelzsArchivedList);
        reelzsCdList.removeAll(reelzsCdList);
        reelzsDvdList.removeAll(reelzsDvdList);
        reelzsTable.setItems(reelzsList);


        FilteredList<Reel> filteredLocationData = new FilteredList<>(reelzsList , e -> true);
        searchReelzsTextField.setOnKeyPressed(e -> {
            searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredLocationData.setPredicate((Predicate<? super Reel>) reel -> {

                if (newValue == null || newValue.isEmpty()) {


                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (reel.getReelLocationName().toLowerCase().contains(newValue)) {

                    return true;
                }

                return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);

            }));

            SortedList<Reel> sortedData = new SortedList<>(filteredLocationData);
            sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
            reelzsTable.setItems(sortedData);

        });

    }

   public void digitallyArchivedSearchMethod(){

       reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
       reelzsArchivedList.removeAll(reelzsArchivedList);
       reelzsOwnList.removeAll(reelzsOwnList);
       reelzsWantList.removeAll(reelzsWantList);
       reelzsCdList.removeAll(reelzsCdList);
       reelzsDvdList.removeAll(reelzsDvdList);

       for (Reel reel : reelzsList) {

           if (ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) == 1) {

               reelzsArchivedList.addAll(new Reel(
                       reel.getReelId() ,
                       reel.getReelName() ,
                       reel.getReelArtist() ,
                       reel.getReelYear() ,
                       reel.getReelType() ,
                       reel.getReelGenre() ,
                       reel.getReelRating(),
                       ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                       reel.getReelNotes() ,
                       ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                       ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                       reel.getReelLocationName() ,
                       reel.getReelLocationPageNumber() ,
                       reel.getReelLocationPageSize() ,
                       reel.getReelLocationSlotNumber(),
                       ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));



               reelzsTable.setItems(reelzsArchivedList);

           }

       }
       FilteredList<Reel> filteredArchivedReelzs = new FilteredList<>(reelzsArchivedList , e -> true);
       searchReelzsTextField.setOnKeyPressed(e -> {
           searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredArchivedReelzs.setPredicate((Predicate<? super Reel>) reel -> {

               if (newValue == null || newValue.isEmpty()) {


                   return true;
               }
               String lowerCaseFilter = newValue.toLowerCase();

               if (String.valueOf(reel.getReelId()).contains(newValue)) {
                   return true;
               }

               if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                   return true;
               }

               if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                   return true;

               }
               if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                   return true;
               }
               if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                   return true;

               }

               if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                   return true;
               }
               if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                   return true;
               }
               return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


           }));

           SortedList<Reel> sortedData = new SortedList<>(filteredArchivedReelzs);
           sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
           reelzsTable.setItems(sortedData);

       });



   }

   public void notDigitallyArchivedSearchMethod(){
        reelzsNotArchivedList.removeAll(reelzsNotArchivedList);
       reelzsArchivedList.removeAll(reelzsArchivedList);
       reelzsOwnList.removeAll(reelzsOwnList);
       reelzsWantList.removeAll(reelzsWantList);
       reelzsCdList.removeAll(reelzsCdList);
       reelzsDvdList.removeAll(reelzsDvdList);

       for (Reel reel : reelzsList) {

           if (ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()) == 0) {

               reelzsNotArchivedList.addAll(new Reel(
                       reel.getReelId() ,
                       reel.getReelName() ,
                       reel.getReelArtist() ,
                       reel.getReelYear() ,
                       reel.getReelType() ,
                       reel.getReelGenre() ,
                       reel.getReelRating(),
                       ConvertCheckboxResultToInt(reel.getReelIsOriginal2()),
                       reel.getReelNotes() ,
                       ConvertCheckboxResultToInt(reel.getReelOwn2()) ,
                       ConvertCheckboxResultToInt(reel.getReelWant2()) ,
                       reel.getReelLocationName() ,
                       reel.getReelLocationPageNumber() ,
                       reel.getReelLocationPageSize() ,
                       reel.getReelLocationSlotNumber(),
                       ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2())));



               reelzsTable.setItems(reelzsNotArchivedList);

           }

       }
       FilteredList<Reel> filteredNotArchivedReelzs = new FilteredList<>(reelzsNotArchivedList , e -> true);
       searchReelzsTextField.setOnKeyPressed(e -> {
           searchReelzsTextField.textProperty().addListener((observableValue , oldValue , newValue) -> filteredNotArchivedReelzs.setPredicate((Predicate<? super Reel>) reel -> {

               if (newValue == null || newValue.isEmpty()) {


                   return true;
               }
               String lowerCaseFilter = newValue.toLowerCase();

               if (String.valueOf(reel.getReelId()).contains(newValue)) {
                   return true;
               }

               if (reel.getReelName().toLowerCase().contains(lowerCaseFilter)) {
                   return true;
               }

               if (reel.getReelArtist().toLowerCase().contains(lowerCaseFilter)) {

                   return true;

               }
               if (String.valueOf(reel.getReelYear()).contains(newValue)) {
                   return true;
               }
               if (reel.getReelType().toLowerCase().contains(lowerCaseFilter)) {

                   return true;

               }

               if (reel.getReelGenre().toLowerCase().contains(lowerCaseFilter)) {
                   return true;
               }

               if(reel.getReelRating().toLowerCase().contains(lowerCaseFilter)){
                   return true;
               }
               if (reel.getReelNotes().toLowerCase().contains(lowerCaseFilter)){
                   return true;
               }
               return reel.getReelLocationName().toLowerCase().contains(lowerCaseFilter);


           }));

           SortedList<Reel> sortedData = new SortedList<>(filteredNotArchivedReelzs);
           sortedData.comparatorProperty().bind(reelzsTable.comparatorProperty());
           reelzsTable.setItems(sortedData);

       });


   }



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
        reelOwnStatusCheckbox.setSelected(reelzsTable.getSelectionModel().getSelectedItem().getReelOwn().isSelected());
        reelWantStatusCheckbox.setSelected(reelzsTable.getSelectionModel().getSelectedItem().getReelWant().isSelected());
        reelLocationNameTextField.setText(reelLocationNameColumn.getCellData(index));
        reelLocationPageNumberTextField.setText(reelLocationPageNumberColumn.getCellData(index).toString());
        reelLocationPageSizeTextField.setText(reelLocationPageSizeColumn.getCellData(index).toString());
        reelLocationSlotNumberTextField.setText(reelLocationSlotNumberColumn.getCellData(index).toString());
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
        reelOwnStatusCheckbox.setSelected(false);
        reelWantStatusCheckbox.setSelected(false);
        reelLocationNameTextField.setText("");
        reelLocationPageNumberTextField.setText("");
        reelLocationPageSizeTextField.setText("0");
        reelLocationSlotNumberTextField.setText("0");
        reelDigitallyArchivedStatusCheckBox.setSelected(false);

    }

    public void createAboutWindow() {

        try {

            FXMLLoader createAboutWindowLoader = new FXMLLoader(getClass().getResource("AboutWindow.fxml"));
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

            FXMLLoader createModifyUserWindow = new FXMLLoader(getClass().getResource("ModifyUsersWindow.fxml"));
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
        reelOwnColumn.setCellValueFactory(new PropertyValueFactory<>("reelOwn"));
        reelWantColumn.setCellValueFactory(new PropertyValueFactory<>("reelWant"));
        reelLocationNameColumn.setCellValueFactory(new PropertyValueFactory<>("reelLocationName"));
        reelLocationPageNumberColumn.setCellValueFactory(new PropertyValueFactory<>("reelLocationPageNumber"));
        reelLocationPageSizeColumn.setCellValueFactory(new PropertyValueFactory<>("reelLocationPageSize"));
        reelLocationSlotNumberColumn.setCellValueFactory(new PropertyValueFactory<>("reelLocationSlotNumber"));
        reelDigitallyArchivedColumn.setCellValueFactory(new PropertyValueFactory<>("reelDigitallyArchived"));


        populateReelsList();

    }

    public class Reel {
        private int reelId;
        private String reelName;
        private String reelArtist;
        private int reelYear;
        private String reelType;
        private String reelGenre;



        private String reelRating;
        private CheckBox reelIsOriginal;
        private String reelNotes;
        private CheckBox reelOwn;
        private CheckBox reelWant;
        private String reelLocationName;
        private int reelLocationPageNumber;
        private int reelLocationPageSize;
        private int reelLocationSlotNumber;
        private CheckBox reelDigitallyArchived;


        public Reel() {
            this.reelId = 0;
            this.reelName = "";
            this.reelArtist = "";
            this.reelYear = 2000;
            this.reelType = "";
            this.reelGenre = "";
            this.reelRating = "";
            this.reelIsOriginal = new CheckBox();
            this.reelNotes = "";
            this.reelOwn = new CheckBox();
            this.reelWant = new CheckBox();
            this.reelLocationName = "";
            this.reelLocationPageSize = 0;
            this.reelLocationSlotNumber = 0;
            this.reelDigitallyArchived = new CheckBox();

        }

        public Reel(int reelId , String reelName , String reelArtist , int reelYear , String reelType , String reelGenre , String reelRating ,int selectedOriginalStatus   , String reelNotes , int selectedOwnStatus , int selectedWantStatus ,   String reelLocationName , int reelLocationPageNumber , int reelLocationPageSize , int reelLocationSlotNumber,int selectedArchivedStatus) {
            this.reelId = reelId;
            this.reelName = reelName;
            this.reelArtist = reelArtist;
            this.reelYear = reelYear;
            this.reelType = reelType;
            this.reelGenre = reelGenre;
            this.reelRating = reelRating;
            this.reelIsOriginal = new CheckBox();
            if (selectedOriginalStatus == 0){
                reelIsOriginal.setSelected(false);

            } else if (selectedOriginalStatus == 1){
                reelIsOriginal.setSelected(true);
            }

            this.reelNotes = reelNotes;
            this.reelOwn = new CheckBox();
            if (selectedOwnStatus == 0) {
                reelOwn.setSelected(false);

            } else if (selectedOwnStatus == 1) {
                reelOwn.setSelected(true);

            }
            this.reelWant = new CheckBox();
            if (selectedWantStatus == 0) {
                reelWant.setSelected(false);


            } else if (selectedWantStatus == 1) {
                reelWant.setSelected(true);

            }

            this.reelLocationName = reelLocationName;
            this.reelLocationPageNumber = reelLocationPageNumber;
            this.reelLocationPageSize = reelLocationPageSize;
            this.reelLocationSlotNumber = reelLocationSlotNumber;
            this.reelDigitallyArchived = new CheckBox();
            if(selectedArchivedStatus == 0){
                reelDigitallyArchived.setSelected(false);


            }else if(selectedArchivedStatus == 1){
                reelDigitallyArchived.setSelected(true);
            }

        }



        public int getReelId() {
            return reelId;
        }

        public void setReelId(int reelId) {
            this.reelId = reelId;
        }

        public String getReelName() {
            return reelName;
        }

        public void setReelName(String reelName) {
            this.reelName = reelName;
        }

        public String getReelArtist() {
            return reelArtist;
        }

        public void setReelArtist(String reelArtist) {
            this.reelArtist = reelArtist;
        }

        public int getReelYear() {
            return reelYear;
        }

        public void setReelYear(int reelYear) {
            this.reelYear = reelYear;
        }

        public String getReelType() {
            return reelType;
        }

        public void setReelType(String reelType) {
            this.reelType = reelType;
        }


        public String getReelGenre() {
            return reelGenre;
        }

        public void setReelGenre(String reelGenre) {
            this.reelGenre = reelGenre;
        }


        public String getReelRating() {
            return reelRating;
        }

        public void setReelRating(String reelRating) {
            this.reelRating = reelRating;
        }

        public CheckBox getReelIsOriginal() {
            return reelIsOriginal;
        }

        public void setReelIsOriginal(boolean reelOriginalStatus) {
            this.reelIsOriginal.setSelected(reelOriginalStatus);
        }

        public boolean getReelIsOriginal2(){
            return reelIsOriginal.isSelected();

        }



        public String getReelNotes() {
            return reelNotes;
        }

        public void setReelNotes(String reelNotes) {
            this.reelNotes = reelNotes;
        }

        public boolean getReelOwn2() {
            return reelOwn.isSelected();
        }

        public CheckBox getReelOwn() {
            return reelOwn;
        }

        public void setReelOwn(boolean ownStatus) {
            this.reelOwn.setSelected(ownStatus);
        }

        public boolean getReelWant2() {
            return reelWant.isSelected();
        }

        public CheckBox getReelWant() {
            return reelWant;
        }

        public void setReelWant(boolean ownStatus) {
            this.reelWant.setSelected(ownStatus);
        }

        public String getReelLocationName() {
            return reelLocationName;
        }

        public void setReelLocationName(String reelLocationName) {
            this.reelLocationName = reelLocationName;
        }

        public int getReelLocationPageNumber() {
            return reelLocationPageNumber;
        }

        public void setReelLocationPageNumber(int reelLocationPageNumber) {
            this.reelLocationPageNumber = reelLocationPageNumber;
        }

        public int getReelLocationPageSize() {
            return reelLocationPageSize;
        }

        public void setReelLocationPageSize(int reelLocationPageSize) {
            this.reelLocationPageSize = reelLocationPageSize;
        }

        public int getReelLocationSlotNumber() {
            return reelLocationSlotNumber;
        }

        public void setReelLocationSlotNumber(int reelLocationSlotNumber) {
            this.reelLocationSlotNumber = reelLocationSlotNumber;
        }

       public boolean getReelDigitallyArchived2(){
            return reelDigitallyArchived.isSelected();
       }

        public CheckBox getReelDigitallyArchived() {
            return reelDigitallyArchived;
        }

        public void setReelDigitallyArchived(boolean archivedStatus) {
            this.reelDigitallyArchived.setSelected(archivedStatus);
        }



    }


}

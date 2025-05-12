package Classes;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateUsersController extends Classes.LoginController implements Initializable {


// alerts
public Alert duplicateUserFound = new Alert(Alert.AlertType.WARNING);
public Alert userSuccessfullyCreated = new Alert(Alert.AlertType.INFORMATION);
public Alert databaseModificationError = new Alert(Alert.AlertType.ERROR);
public Alert userCreationError = new Alert(Alert.AlertType.ERROR);


// arrays
@FXML
private final String[] questions = {"Please select a security Question" , "What is your favorite color?" , "First pets name?" , "favorite musical artist or band?" , "Favorite animal?" , "favorite tv Show"};

// labels

public Label createUserKeyLabel;
public Label createUserLabel;
public Label securityQuestion1AnswerLabel;
public Label securityQuestion2AnswerLabel;
public Label securityQuestion3AnswerLabel;
public Label securityQuestion1Label;
public Label securityQuestion2Label;
public Label securityQuestion3Label;

// textFields
public TextField showNewUserKey = new TextField();
public TextField newUserName = new TextField();
public TextField showConfirmUserKey = new TextField();
public TextField securityQuestion2Answer;
public TextField securityQuestion3Answer;
public TextField securityQuestion1Answer;

// passwordFields
public PasswordField newUserKey = new PasswordField();
public PasswordField confirmUserKey = new PasswordField();


// checkBoxes

public CheckBox showCreatedUserKey;
public CheckBox showConfirmedUserKey;

// choiceBoxes

@FXML
private ChoiceBox<String> securityQuestion1 = new ChoiceBox<>();
@FXML
private ChoiceBox<String> securityQuestion2 = new ChoiceBox<>();
@FXML
private ChoiceBox<String> securityQuestion3 = new ChoiceBox<>();


// buttons

public Button createUser;



    public void createNewUser() {

        if(securityQuestion1.getValue().equals("Please select a security Question")|| securityQuestion1Answer.getText().equals("")||
        securityQuestion2.getValue().equals("Please select a security Question")|| securityQuestion2Answer.getText().equals("")||
        securityQuestion3.getValue().equals("Please select a security Question")|| securityQuestion3Answer.getText().equals("")||
        newUserName.getText().equals("")|| newUserKey.getText().equals("")|| confirmUserKey.getText().equals("")){

            userCreationError.setContentText("one or more fields are not filled ");
            userCreationError.showAndWait();

        }else {

            verifyUniqueUserName(newUserName.getText());
        }
    }


    public void showNewUserKeyText() {

        if (showCreatedUserKey.isSelected()) {
            showNewUserKey.setText(newUserKey.getText());
            newUserKey.setVisible(false);
            showNewUserKey.setVisible(true);

        } else {

            newUserKey.setVisible(true);
            showNewUserKey.setVisible(false);
        }


    }

    public void showConfirmedUserKeyText() {
        if (showConfirmedUserKey.isSelected()) {
            showConfirmUserKey.setText(confirmUserKey.getText());
            confirmUserKey.setVisible(false);
            showConfirmUserKey.setVisible(true);
        } else {
            confirmUserKey.setVisible(true);
            showConfirmUserKey.setVisible(false);
        }

    }

    public void verifyUniqueUserName(String userName) {










        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT * FROM  Users WHERE userName = ? ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , userName);
            rs = ps.executeQuery();
            if (rs.next()) {

                duplicateUserFound.setContentText
                        ("This username or passKey already exists please try again");
                duplicateUserFound.showAndWait();
                rs.close();
                ps.close();
                conn.close();
            } else {
                rs.close();
                ps.close();
                conn.close();
                addNewUser();


            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        userSuccessfullyCreated.setContentText("New User Was successfully created");
        userSuccessfullyCreated.showAndWait();
    }



    public String HashNewUserPassword(String userKey) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        // Read password from user
        String hash = null;
        if (newUserKey.getText().equals(confirmUserKey.getText())) {


            char[] password = userKey.toCharArray();
            hash = null;

            try {
                // Hash password
                hash = argon2.hash(10 ,
                        65536 ,
                        1 ,
                        password);

// insert hash method//

                Boolean verify = argon2.verify(hash , password);
                System.out.println(verify);

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {

            if (newUserKey.getText().equals(confirmUserKey.getText())) {

                userCreationError.setContentText("the new  user key and confirm new userKey");
                userCreationError.showAndWait();

            }


        }

        return hash;
    }

    public String HashSecurityQuestionAnswers(String securityQuestionAnswer) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);
        char[] Answer = securityQuestionAnswer.toCharArray();
        String answerHash = null;
        try {

            answerHash = argon2.hash(10 ,
                    65536 ,
                    1 ,
                    Answer);

            Boolean verify = argon2.verify(answerHash , Answer);
            System.out.println(verify);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return answerHash;
    }


    public void addNewUser() {

        String userName = newUserName.getText();
        String hash = HashNewUserPassword(newUserKey.getText());
        String securityQuestion1Selection = securityQuestion1.getValue();
        String securityQuestion2Selection = securityQuestion2.getValue();
        String securityQuestion3Selection = securityQuestion3.getValue();
        String answer1 = HashSecurityQuestionAnswers(securityQuestion1Answer.getText());
        String answer2 = HashSecurityQuestionAnswers(securityQuestion2Answer.getText());
        String answer3 = HashSecurityQuestionAnswers(securityQuestion3Answer.getText());

        // note answers will be hashed using the same method as for new user password with some modification//

        try {

            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "INSERT INTO Users " +
                    "( userName , hash , securityQuestion1 , securityQuestion1Answer ," +
                    " securityQuestion2 , securityQuestion2Answer , " +
                    "securityQuestion3 , securityQuestion3Answer  ) " +
                    "Values( ? ,? , ? , ? , ? , ? , ? , ?)";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , userName);
            ps.setString(2 , hash);
            ps.setString(3 , securityQuestion1Selection);
            ps.setString(4 , answer1);
            ps.setString(5 , securityQuestion2Selection);
            ps.setString(6 , answer2);
            ps.setString(7 , securityQuestion3Selection);
            ps.setString(8 , answer3);
            ps.execute();
            ps.close();
            conn.close();

        } catch (SQLException e) {

            databaseModificationError.setContentText("there was a problem inserting the new user record");
            databaseModificationError.showAndWait();
            e.printStackTrace();

        }

    }

    @Override
    public void initialize(URL url , ResourceBundle resourceBundle) {

        newUserKey.setVisible(true);
        showNewUserKey.setVisible(false);
        confirmUserKey.setVisible(true);
        showConfirmUserKey.setVisible(false);
        securityQuestion1.getItems().addAll(questions);
        securityQuestion1.setValue("Please select a security Question");
        securityQuestion2.getItems().addAll(questions);
        securityQuestion2.setValue("Please select a security Question");
        securityQuestion3.getItems().addAll(questions);
        securityQuestion3.setValue("Please select a security Question");
        securityQuestion1.setOnAction(this::getQuestion);
        securityQuestion2.setOnAction(this::getQuestion);
        securityQuestion3.setOnAction(this::getQuestion);

    }

    public void getQuestion(ActionEvent event) {
        String selectedQuestion;
        if (event.getSource() == securityQuestion1) {

            selectedQuestion = securityQuestion1.getValue();
            System.out.println("Security question 1 is :" + selectedQuestion);

        } else if (event.getSource() == securityQuestion2) {
            selectedQuestion = securityQuestion2.getValue();
            System.out.println("Security question 2 is :" + selectedQuestion);


        } else if (event.getSource() == securityQuestion3) {
            selectedQuestion = securityQuestion3.getValue();
            System.out.println("Security question 3 is :" + selectedQuestion);

        }


    }


}


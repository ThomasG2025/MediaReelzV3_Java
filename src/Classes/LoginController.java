package Classes;


import Objects.User;
import javafx.scene.control.*;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

        // keep login failed for when that functionality is being added later//

// alerts
public Alert  loginError = new Alert(Alert.AlertType.ERROR);
public final Alert loginFailed = new Alert(Alert.AlertType.WARNING);
public Alert databaseQueryExecutionError = new Alert(Alert.AlertType.ERROR);
public Alert windowCreationError = new Alert(Alert.AlertType.ERROR);

// labels
public Label userNameLabel;
public Label userKeyLabel;
public Label loginLabel;

// textField
public TextField userNameTextField;

// passwordField
public PasswordField userKey;

// hyperlink
public Hyperlink forgotUserKey;


// buttons
public Button createUser;
public Button login;


    public void login() {
        if (userNameTextField.getText().equals("") || userKey.getText().equals("")) {

            loginError.setContentText(" there was a problem verifying user credentials please make sure both the userName and userKey entry fields are filled and the user exits");
            loginError.showAndWait();

        } else {

        try{

            HashPassword(userKey.getText());

        } catch (Exception L){
            loginError.setContentText(" there was a problem verifying user credentials with the given credentials");
            loginError.showAndWait();

        }

    }
        }

        public void HashPassword(String userKey) {
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

            // Read password from user
            char[] password = userKey.toCharArray();
            String storedHash = getStoredHash(userNameTextField.getText());
            System.out.println(storedHash);

            System.out.println(password);

            boolean verify = argon2.verify(storedHash , password);

            System.out.println(verify);
            if (verify) {
                setVerifiedUserId();
                createDataEntryWindow();
            } else {
                loginFailed.setContentText("there was a problem verifying the given user credentials");
                loginFailed.showAndWait();
            }


        }


        public String getStoredHash(String userName) {


            String storedHash = null;
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT hash FROM Users Where userName = ? ";

                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , userName);
                rs = ps.executeQuery();
                if (rs.next()) {
                    storedHash = rs.getString("hash");
                    System.out.println("this works");
                    rs.close();
                    ps.close();
                    conn.close();
                }


            } catch (Exception e) {

                e.printStackTrace();
            }

            System.out.println(storedHash);

            return storedHash;
        }

        public void setVerifiedUserId() {

            String userName = userNameTextField.getText();
            String storedHash = getStoredHash(userNameTextField.getText());

            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "SELECT  userId From Users Where  userName =  ? AND hash = ? ";
                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , userName);
                ps.setString(2 , storedHash);
                rs = ps.executeQuery();
                if (rs.next()) {
                    int userId;
                    userId = rs.getInt("userId");
                    System.out.println("userId = " + userId);
                    User.setUserId(userId);
                    rs.close();
                    ps.close();
                    conn.close();

                } else {
                    loginFailed.setContentText("users credentials do not match");
                    loginFailed.showAndWait();
                }


            } catch (SQLException throwables) {
                databaseQueryExecutionError.setContentText("there was a problem  retrieving user Id based on given credentials  ");
                databaseQueryExecutionError.showAndWait();

                throwables.printStackTrace();


            }


        }


        public void createUsersWindow() {


            try {

                FXMLLoader createUsersWindowLoader = new FXMLLoader(getClass().getResource("CreateUsers.fxml"));
                Parent root3 = createUsersWindowLoader.load();
                Stage createUsersStage = new Stage();
                createUsersStage.initModality(Modality.APPLICATION_MODAL);
                createUsersStage.setTitle("Create User");
                createUsersStage.setScene(new Scene(root3));
                createUsersStage.show();


            } catch (IOException e) {
                windowCreationError.setContentText("there was a problem creating the create user window");
                windowCreationError.showAndWait();
                e.printStackTrace();
            }


        }


        public void VerifyUserNameForRecoveryWindow() {

            try {

                FXMLLoader createModifyUsersWindowLoader = new FXMLLoader(getClass().getResource("verifyUserNameforRecoveryWindowController.fxml"));
                Parent root5 = createModifyUsersWindowLoader.load();
                Stage modifyUsersStage = new Stage();
                modifyUsersStage.initModality(Modality.APPLICATION_MODAL);
                modifyUsersStage.setTitle("Recover lost UserKey");
                modifyUsersStage.setScene(new Scene(root5));
                modifyUsersStage.show();


            } catch (IOException e) {
                windowCreationError.setContentText("there was a problem creating the verify userNameRecoveryWindowController");
                windowCreationError.showAndWait();
                e.printStackTrace();
            }


        }

        public void createDataEntryWindow() {


            try {

                FXMLLoader createDataEntryWindowLoader = new FXMLLoader(getClass().getResource("ReelzsViewer.fxml"));
                Parent root2 = createDataEntryWindowLoader.load();
                Stage modifyUsersStage = new Stage();
                modifyUsersStage.initModality(Modality.APPLICATION_MODAL);
                modifyUsersStage.setTitle("Data Entry");
                modifyUsersStage.setScene(new Scene(root2));
                modifyUsersStage.show();


            } catch (IOException e) {
                windowCreationError.setContentText("there was a problem creating the reelzsViewer window");
                windowCreationError.showAndWait();
                e.printStackTrace();
            }


        }


        @Override
        public void initialize(URL url , ResourceBundle resourceBundle) {
            //loadFonts();
        }
    }






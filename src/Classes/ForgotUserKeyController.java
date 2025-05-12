package Classes;


import Objects.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

import static javafx.application.Platform.exit;

public class ForgotUserKeyController   implements Initializable {

// alerts

public Alert resetUserKeyErrorAlert =  new Alert(Alert.AlertType.ERROR);
public Alert resetUserKeyAlert =  new Alert(Alert.AlertType.INFORMATION);

// labels
    public Label confirmResetUserKeyLabel;
    public Label resetUserKeyLabel;
    public Label forgotUserKeyHeader;

// password fields

    public PasswordField resetNewUserKey;
    public PasswordField confirmResetUserKey;

    // buttons

    public Button resetUserKeyButton;


    // booleans
    public boolean successfulReset = false;


    public void resetUserKey() {
        successfulReset = false;
        if (!resetNewUserKey.getText().isEmpty() && !confirmResetUserKey.getText().isEmpty() && resetNewUserKey.getText().equals(confirmResetUserKey.getText())) {

            String updatedHash = HashRecoveryUserPassword(confirmResetUserKey.getText());
            String userName = User.getUserName();

            try {
                // sql and ps parameters need to be reworked//
                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "Update  Users" + " SET hash = ? " + " Where userName = ? ";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , updatedHash);
                ps.setString(2 , userName);

                ps.executeUpdate();
                ps.close();
                conn.close();


            } catch (Exception e) {
                e.printStackTrace();

            }
            successfulReset = true;
            resetUserKeyAlert.setContentText("User Key has  been successfully updated");
            resetUserKeyAlert.showAndWait();
            restartMediaReelzs();

        } else {

            resetUserKeyErrorAlert.setContentText("please make sure both the new password and confirm new password fields match");
            resetUserKeyErrorAlert.showAndWait();
        }

    }

    public String HashRecoveryUserPassword(String userKey) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        // Read password from user
        char[] resetPassword = userKey.toCharArray();


        String hash = null;
        try {
            // Hash password
            hash = argon2.hash(10 ,
                    65536 ,
                    1 ,
                    resetPassword);

// insert hash method//
            System.out.println(resetPassword);

            Boolean verify = argon2.verify(hash , resetPassword);
            System.out.println(hash);
            System.out.println(verify);


        } catch (Exception e) {
            e.printStackTrace();


        }


        return hash;
    }

    public void restartMediaReelzs() {
        if (successfulReset) {

            exit();

        } else {
            resetUserKeyErrorAlert.setContentText("please make sure both the new password and confirm new password fields match");
            resetUserKeyErrorAlert.showAndWait();
        }


    }

    @Override
    public void initialize(URL url , ResourceBundle resourceBundle) {

    }
}

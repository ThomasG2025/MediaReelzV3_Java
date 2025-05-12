package Classes;


import Objects.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class VerifyUserNameForRecoveryWindowController implements Initializable {
    // Alert
    public Alert  userNameVerificationError = new Alert(Alert.AlertType.ERROR);
    public Alert databaseRetrievalError = new Alert(Alert.AlertType.ERROR);
    public Alert windowCreationError = new Alert(Alert.AlertType.ERROR);

    // labels

    public Label verifyUserNameForRecoveryLabel;
    public Label userNameLabel;
    public Label confirmUserNameLabel;

    // textFields

    public TextField recoveryUserNameTextField;
    public TextField recoveryConfirmUserNameTextField;

    // buttons

    public Button verifyUserNameButton;


    public void verifyUserNameForRecoveryMethod() {

        String recoveryUserName = recoveryConfirmUserNameTextField.getText();
        String confirmRecoveryUserName = recoveryConfirmUserNameTextField.getText();

        if (recoveryUserName.equals(confirmRecoveryUserName)) {

            try {

                Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
                String sql = "Select userName FROM Users Where userName = ?";
                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , confirmRecoveryUserName);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String userName;
                    userName = rs.getString("userName");
                    System.out.println(userName);
                    User.setuserName(userName);
                    createAlternativeUserVerificationWindow();
                    rs.close();
                    ps.close();
                    conn.close();
                } else {

                    userNameVerificationError.setContentText("this user does not exist please verify the username is typed in correctly");
                    userNameVerificationError.showAndWait();

                }

            } catch (SQLException e) {
                databaseRetrievalError.setContentText("There was an error executing a query");
                databaseRetrievalError.showAndWait();
                e.printStackTrace();
            }


        }


    }


    public void createAlternativeUserVerificationWindow() {

        try {
            FXMLLoader createAlternativeUserVerificationWindow = new FXMLLoader(getClass().getResource("AlternativeUserVerificationController.fxml"));
            Parent root6 = createAlternativeUserVerificationWindow.load();
            Stage AlternativeUserVerificationStage = new Stage();
            AlternativeUserVerificationStage.initModality(Modality.APPLICATION_MODAL);
            AlternativeUserVerificationStage.setTitle("AlternativeUserVerification");
            AlternativeUserVerificationStage.setScene(new Scene(root6));
            AlternativeUserVerificationStage.show();


        } catch (IOException e) {
            windowCreationError.setContentText(" there was a problem creating the alternative user verification window");
            windowCreationError.showAndWait();
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL url , ResourceBundle resourceBundle) {

        // loadVerifyUserNameForRecoveryFonts();
    }
}

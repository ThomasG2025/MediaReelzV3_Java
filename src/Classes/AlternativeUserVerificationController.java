package Classes;



import Objects.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
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

public class AlternativeUserVerificationController implements Initializable {


// Alerts for error checking

    public Alert securityQuestionHashingError = new Alert(Alert.AlertType.ERROR);
    public Alert windowCreationError = new Alert(Alert.AlertType.ERROR);

// labels
    public Label verifyUserForRecoveryLabel;
    public Label answer3Label;
    public Label answer2Label;
    public Label answer1Label;
    public Label securityQuestion3Label;
    public Label securityQuestion2Label;
    public Label securityQuestion1Label;

// TextFields
    public TextField recoveryAnswer3;
    public TextField securityQuestion3;
    public TextField recoveryAnswer2;
    public TextField securityQuestion2;
    public TextField recoveryAnswer1;
    public TextField securityQuestion1;

// Button
    public Button verifyUserButton;

    public void verifySecurityQuestions() {

        HashSecurityQuestionAnswers();

    }

    public void HashSecurityQuestionAnswers() {
        boolean securityQuestion1AnswerCorrect = false;
        boolean securityQuestion2AnswerCorrect = false;
        boolean securityQuestion3AnswerCorrect = false;
        String questionNumber1 = (securityQuestion1.getText());
        String questionNumber2 = (securityQuestion2.getText());
        String questionNumber3 = (securityQuestion3.getText());
        String question1Answer = recoveryAnswer1.getText();
        String question2Answer = recoveryAnswer2.getText();
        String question3Answer = recoveryAnswer3.getText();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        if (!questionNumber1.isEmpty() && !recoveryAnswer1.getText().isEmpty()) {
            char[] answer = question1Answer.toCharArray();
            String storedAnswer = getSecurityQuestion1Answer();
            boolean verify = argon2.verify(storedAnswer , answer);
            if (verify) {
                securityQuestion1AnswerCorrect = true;
            }
        }
        if (!questionNumber2.isEmpty() && !recoveryAnswer2.getText().isEmpty()) {
            char[] answer = question2Answer.toCharArray();
            String storedAnswer = getSecurityQuestion2Answer();
            boolean verify = argon2.verify(storedAnswer , answer);
            if (verify) {
                securityQuestion2AnswerCorrect = true;
            }
        }
        if (!questionNumber3.isEmpty() && !recoveryAnswer3.getText().isEmpty()) {
            char[] answer = question3Answer.toCharArray();
            String storedAnswer = getSecurityQuestion3Answer();
            boolean verify = argon2.verify(storedAnswer , answer);
            if (verify) {
                securityQuestion3AnswerCorrect = true;

            }

        } else {
            System.out.println(" we have a problem");
            securityQuestionHashingError.setContentText("there was a problem verifying one or more security questions");
            securityQuestionHashingError.showAndWait();
        }

        if (securityQuestion1AnswerCorrect && securityQuestion2AnswerCorrect && securityQuestion3AnswerCorrect) {

            createForgotUserKeyWindow();
        }

    }

    public void getSecurityQuestion1() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT securityQuestion1 From Users WHERE  userName = ?  ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , User.getUserName());
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedSecurityQuestion1;
                storedSecurityQuestion1 = rs.getString("securityQuestion1");
                securityQuestion1.setText(storedSecurityQuestion1);
                rs.close();
                ps.close();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getSecurityQuestion2() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT securityQuestion2 From Users WHERE  userName = ?";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , User.getUserName());
            rs = ps.executeQuery();
            if (rs.next()) {
                String storedSecurityQuestion2;
                storedSecurityQuestion2 = rs.getString("securityQuestion2");
                securityQuestion2.setText(storedSecurityQuestion2);
                rs.close();
                ps.close();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getSecurityQuestion3() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT securityQuestion3 From Users WHERE  userName = ?";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , User.getUserName());
            rs = ps.executeQuery();
            if (rs.next()) {

                String storedSecurityQuestion3;
                storedSecurityQuestion3 = rs.getString("securityQuestion3");
                securityQuestion3.setText(storedSecurityQuestion3);
                rs.close();
                ps.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getSecurityQuestion1Answer() {
        String answer1 = null;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT securityQuestion1Answer FROM Users Where userName = ?";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , User.getUserName());
            rs = ps.executeQuery();
            if (rs.next()) {
                answer1 = rs.getString("securityQuestion1Answer");
                rs.close();
                ps.close();
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer1;

    }

    public String getSecurityQuestion2Answer() {
        String answer2 = null;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT securityQuestion2Answer FROM Users Where userName = ?";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , User.getUserName());
            rs = ps.executeQuery();
            if (rs.next()) {

                answer2 = rs.getString("securityQuestion2Answer");
                rs.close();
                ps.close();
                conn.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer2;

    }


    public String getSecurityQuestion3Answer() {
        String answer3 = null;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT securityQuestion3Answer FROM Users Where userName = ?";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , User.getUserName());

            rs = ps.executeQuery();
            if (rs.next()) {

                answer3 = rs.getString("securityQuestion3Answer");
                rs.close();
                ps.close();
                conn.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return answer3;

    }

    public void createForgotUserKeyWindow() {

        try {
            FXMLLoader createForgotUserKeyWindow = new FXMLLoader(getClass().getResource("ForgotUserKeyWindow.fxml"));
            Parent root7 = createForgotUserKeyWindow.load();
            Stage ForgotUserKeyStage = new Stage();
            ForgotUserKeyStage.initModality(Modality.APPLICATION_MODAL);
            ForgotUserKeyStage.setTitle("Forgot UserKey");
            ForgotUserKeyStage.setScene(new Scene(root7));
            ForgotUserKeyStage.show();


        } catch (IOException e) {
            windowCreationError.setContentText("there was a problem  creating  the forgotUserKey Window");
            windowCreationError.showAndWait();
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL url , ResourceBundle resourceBundle) {
        //loadAlternativeUserVerificationFonts();
        getSecurityQuestion1();
        getSecurityQuestion2();
        getSecurityQuestion3();

    }
}

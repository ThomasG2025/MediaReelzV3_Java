package Database.UserOperations;


import Classes.AlternativeUserVerificationController;
import Database.dbConn;
import Objects.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.scene.control.TextField;

import java.sql.*;

public class UserRecoverySecurityVerification extends AlternativeUserVerificationController {

    public void HashSecurityQuestionAnswers(String questionNumber1,String question1Answer,String questionNumber2,
                                            String question2Answer,String questionNumber3,String question3Answer) {
        boolean securityQuestion1AnswerCorrect = false;
        boolean securityQuestion2AnswerCorrect = false;
        boolean securityQuestion3AnswerCorrect = false;


        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        if (!questionNumber1.equals("") && !question1Answer.equals("")) {
            char[] answer = question1Answer.toCharArray();
            String storedAnswer = getSecurityQuestion1Answer();
            boolean verify = argon2.verify(storedAnswer , answer);
            if (verify) {
                securityQuestion1AnswerCorrect = true;
            }
        }
        if (!questionNumber2.equals("") && !question2Answer.equals("")) {
            char[] answer = question2Answer.toCharArray();
            String storedAnswer = getSecurityQuestion2Answer();
            boolean verify = argon2.verify(storedAnswer , answer);
            if (verify) {
                securityQuestion2AnswerCorrect = true;
            }
        }
        if (!questionNumber3.equals("") && !question3Answer.equals("")) {
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

    public void getSecurityQuestion1(TextField securityQuestion1) {

        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT securityQuestion1 From userSecurityView WHERE  userName = ?  ";
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

    public void getSecurityQuestion2(TextField securityQuestion2) {

        try {

            Connection conn = dbConn.connect();
            String sql = "SELECT securityQuestion2 From userSecurityView WHERE  userName = ?";
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

    public void getSecurityQuestion3(TextField securityQuestion3) {

        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT securityQuestion3 From userSecurityView WHERE  userName = ?";

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

            Connection conn = dbConn.connect();
            String sql = "SELECT securityQuestion1Answer FROM userSecurityView Where userName = ?";
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

            Connection conn = dbConn.connect();
            String sql = "SELECT securityQuestion2Answer FROM userSecurityView Where userName = ?";
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
            Connection conn = dbConn.connect();
            String sql = "SELECT securityQuestion3Answer FROM userSecurityView Where userName = ?";

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

}

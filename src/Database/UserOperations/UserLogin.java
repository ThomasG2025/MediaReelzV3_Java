package Database.UserOperations;


import Classes.LoginController;
import Database.dbConn;
import Objects.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin extends LoginController {

    public void HashPassword(String userName,String userKey) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        // Read password from user
        char[] password = userKey.toCharArray();
        String storedHash = getStoredHash(userName);
        System.out.println(storedHash);

        System.out.println(password);

        boolean verify = argon2.verify(storedHash , password);

        System.out.println(verify);
        if (verify) {
            setVerifiedUserId( userName,storedHash);
            createDataEntryWindow();
        } else {
            loginFailed.setContentText("there was a problem verifying the given user credentials");
            loginFailed.showAndWait();
        }


    }

    public String getStoredHash(String userName) {


        String storedHash = null;
        try {
            Connection conn = dbConn.connect();
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

    public void setVerifiedUserId(String userName,String storedHash) {




        try {

            Connection conn = dbConn.connect();
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

}
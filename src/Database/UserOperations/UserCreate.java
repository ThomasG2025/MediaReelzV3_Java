package Database.UserOperations;


import Classes.CreateUsersController;
import Database.dbConn;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCreate extends CreateUsersController {

    public void verifyUniqueUserName(String userName, String newUserKey, String confirmNewUserKey, String securityQuestion1, String securityQuestion1Answer,
                                     String securityQuestion2, String securityQuestion2Answer, String securityQuestion3, String securityQuestion3Answer) {

        boolean userCreated = false;
        try {

            Connection conn = dbConn.connect();
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
                userCreated = addNewUser( userName, newUserKey, confirmNewUserKey,  securityQuestion1, securityQuestion1Answer,
                         securityQuestion2, securityQuestion2Answer, securityQuestion3, securityQuestion3Answer);


            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        if (userCreated) {


            userSuccessfullyCreated.setContentText("New User Was successfully created");
            userSuccessfullyCreated.showAndWait();
        }else{
        }
    }


    public boolean addNewUser(String userName, String newUserKey, String confirmNewUserKey, String securityQuestion1, String securityQuestion1Answer,
                              String securityQuestion2, String securityQuestion2Answer, String securityQuestion3, String securityQuestion3Answer) {

        boolean userSecurityProfileCreated = false;

        String hash = HashNewUserPassword(newUserKey,confirmNewUserKey);


        // note answers will be hashed using the same method as for new user password with some modification//

        try {

            Connection conn = dbConn.connect();
            String sql = "INSERT INTO Users " +
                    "( userName , hash   ) " +
                    "Values( ? ,? )";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , userName);
            ps.setString(2 , hash);
            ps.execute();

            int newUserId = 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    newUserId = rs.getInt(1);
                }

            }

            userSecurityProfileCreated = createUserSecurityProfile(newUserId, securityQuestion1, securityQuestion1Answer,
                     securityQuestion2, securityQuestion2Answer, securityQuestion3, securityQuestion3Answer);
            ps.close();
            conn.close();

        } catch (SQLException e) {

            e.printStackTrace();
            return  userSecurityProfileCreated;

        }


        return true;
    }


    public boolean createUserSecurityProfile(int newUserId , String securityQuestion1 , String securityQuestion1Answer , String securityQuestion2 , String securityQuestion2Answer , String securityQuestion3 , String securityQuestion3Answer){

        boolean securityProfileCreated = false;
        String answer1 = HashSecurityQuestionAnswers(securityQuestion1Answer);
        String answer2 = HashSecurityQuestionAnswers(securityQuestion2Answer);
        String answer3 = HashSecurityQuestionAnswers(securityQuestion3Answer);

        try {

            Connection conn = dbConn.connect();
            String sql = "INSERT INTO securityProfiles " +
                    "( securityQuestion1 , securityQuestion1Answer , securityQuestion2 , securityQuestion2Answer ,  securityQuestion3 ,  securityQuestion3Answer ) " +
                    "Values( ? ,?,?,?,?,? )";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , securityQuestion1);
            ps.setString(2 , answer1);
            ps.setString(3 , securityQuestion2);
            ps.setString(4 , answer2);
            ps.setString(5 , securityQuestion3);
            ps.setString(6 , answer3);
            ps.execute();

            int newSecurityProfileId = 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    newSecurityProfileId = rs.getInt(1);
                }

            }


            ps.close();
            conn.close();
            securityProfileCreated = updateUserSecurityProfileId(newUserId,newSecurityProfileId);

        } catch (SQLException e) {

            e.printStackTrace();
            return securityProfileCreated;




        }
        securityProfileCreated = true;
        return securityProfileCreated;
    }

    public boolean updateUserSecurityProfileId(int newUserId , int newSecurityProfileId){

        try {
            Connection conn = dbConn.connect();

            String sql = "UPDATE Users SET securityProfileId = ? " +
                    "WHERE   userId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , newSecurityProfileId);
            ps.setInt(2 , newUserId);
            ps.executeUpdate();
            ps.close();
            conn.close();


        } catch (SQLException e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
            return false;

        }

        return true;
    }


    public String HashNewUserPassword(String userKey,String confirmUserKey) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        // Read password from user
        String hash = null;
        if (userKey.equals(confirmUserKey)) {


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

            userCreationError.setContentText("the new  user key and confirm new userKey do not match");
            userCreationError.showAndWait();


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


}

package Database.UserOperations;


import Classes.ModifyUsersWindowController;
import Database.dbConn;
import Objects.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserUpdate extends ModifyUsersWindowController {





    public void hashOriginalUserKey(String currentUserKey, String newUserKey , String confirmNewUserKey){
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        char[] currentPassword = currentUserKey.toCharArray();
        String storedHash = getStoredHashBasedOnUserId();

        boolean verify = argon2.verify(storedHash,currentPassword);
        if (verify){
            setUpdateUserHash(newUserKey,confirmNewUserKey);


        }else{

            hashingError.setContentText("there was a problem hashing the original userKey");
            hashingError.showAndWait();

        }


    }

    public String getStoredHashBasedOnUserId(){

        String storedHash = null;

        try {

            Connection conn = dbConn.connect();
            String sql = "SELECT hash FROM Users Where userId = ? ";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            rs = ps.executeQuery();
            if (rs.next()) {
                storedHash = rs.getString("hash");
                System.out.println("this works");

            }
            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {

            databaseQueryExecutionError.setContentText("there was a problem retrieving the user password based on the given userId");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

        System.out.println(storedHash);

        return storedHash;


    }


    public void setUpdateUserHash(String newUserKey , String confirmNewUserKey){
        if(newUserKey.equals(confirmNewUserKey)){

            try {
                // sql and ps parameters need to be reworked//
                Connection conn = dbConn.connect();
                String sql = "Update  Users" + " SET hash = ?" + " Where userId = ? ";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , hashUpdatedUserPassword(newUserKey));
                ps.setInt(2 , User.getUserId());
                ps.executeUpdate();
                ps.close();
                conn.close();
                System.out.println(" User  Data has been updated");

            } catch (Exception e) {
                databaseModificationError.setContentText("there was a problem updating  the user record");
                databaseModificationError.showAndWait();
                e.printStackTrace();

            }

            userKeyUpdatedSuccessNotification.setContentText(" userKey successfully updated");
            userKeyUpdatedSuccessNotification.showAndWait();

        }



    }

    public String hashUpdatedUserPassword(String newUserKey){
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

        char [] password = newUserKey.toCharArray();

        String hash = null;
        try{
            hash = argon2.hash(10,65536,1,password);

            Boolean verify = argon2.verify(hash,password);
            System.out.println(verify);

        } catch (Exception e) {
            hashingError.setContentText("there was a problem hashing the updated userKey");
            hashingError.showAndWait();
            e.printStackTrace();


        }


        return hash;
    }


}

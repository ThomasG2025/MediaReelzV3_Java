package Classes;


import Objects.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;

public class ModifyUsersWindowController {
// alerts
public Alert deleteWarning = new Alert(Alert.AlertType.CONFIRMATION);
public Alert databaseQueryExecutionError = new Alert(Alert.AlertType.ERROR);
public Alert databaseModificationError = new Alert(Alert.AlertType.ERROR);
public Alert hashingError = new Alert(Alert.AlertType.ERROR);
public Alert userKeyUpdatedSuccessNotification = new Alert(Alert.AlertType.INFORMATION);
public Alert userSuccessfullyDeleted = new Alert(Alert.AlertType.INFORMATION);

// arrayLists

public ArrayList<Integer> reelIdsToCheckForDeletion = new ArrayList<>();
public ArrayList<Integer> uniqueReelAttributesToDelete = new ArrayList<>();
public ArrayList<Integer> reelLocationIdsToDelete = new ArrayList<>();

// pane

public Pane updateUsersPane;

// label

public Label newUserKeyLabel;
public Label updateUserLabel;
public Label updateUserUserKeyLabel;
public Label confirmNewUserKeyLabel;

// textFields

public TextField userKeyTextField;
public TextField newUserKeyTextField;
public TextField confirmNewUserKeyTextField;

// passWordFields

public PasswordField userKeyPasswordField;
public PasswordField newUserKeyPasswordField;
public PasswordField confirmNewUserKeyPasswordField;

// checkBoxes

public CheckBox showUserKeyCheckBox;
public CheckBox showNewUserKeyBox;
public CheckBox showConfirmNewUserKeyCheckBox;
// buttons

public Button updateUserButton;
public Button deleteUserButton;



  public void showUserKeyText() {
      if(showUserKeyCheckBox.isSelected()){
          userKeyTextField.setText(userKeyPasswordField.getText());
          userKeyPasswordField.setVisible(false);
          userKeyTextField.setVisible(true);
      }else if(!showUserKeyCheckBox.isSelected()){
          userKeyPasswordField.setText(userKeyTextField.getText());
          userKeyPasswordField.setVisible(true);
          userKeyTextField.setVisible(false);


      }


  }

  public void showNewUserKeyText() {
      if(showNewUserKeyBox.isSelected()){
          newUserKeyTextField.setText(newUserKeyPasswordField.getText());
          newUserKeyPasswordField.setVisible(false);
          newUserKeyTextField.setVisible(true);
      }else if(!showNewUserKeyBox.isSelected()){
          newUserKeyPasswordField.setText(newUserKeyTextField.getText());
          newUserKeyPasswordField.setVisible(true);
          newUserKeyTextField.setVisible(false);
      }


  }

  public void showConfirmNewUserKeyText() {

      if(showConfirmNewUserKeyCheckBox.isSelected()){
          confirmNewUserKeyTextField.setText(confirmNewUserKeyPasswordField.getText());
          confirmNewUserKeyPasswordField.setVisible(false);
          confirmNewUserKeyTextField.setVisible(true);
      } else if(!showConfirmNewUserKeyCheckBox.isSelected()){
          confirmNewUserKeyPasswordField.setText(confirmNewUserKeyTextField.getText());
          confirmNewUserKeyPasswordField.setVisible(true);
          confirmNewUserKeyTextField.setVisible(false);
      }


    }





public void updateUserHash(){
      hashOriginalUserKey(userKeyPasswordField.getText());

}


public void hashOriginalUserKey(String currentUserKey){
    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id , 16 , 16);

    char[] currentPassword = currentUserKey.toCharArray();
    String storedHash = getStoredHashBasedOnUserId();

    boolean verify = argon2.verify(storedHash,currentPassword);
    if (verify){
        setUpdateUserHash();


    }else{

        hashingError.setContentText("there was a problem hashing the original userKey");
        hashingError.showAndWait();

    }


}



 public String getStoredHashBasedOnUserId(){

     String storedHash = null;
     try {
         Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
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


 public void setUpdateUserHash(){
    if(newUserKeyPasswordField.getText().equals(confirmNewUserKeyPasswordField.getText())){

        try {
            // sql and ps parameters need to be reworked//
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "Update  Users" + " SET hash = ?" + " Where userId = ? ";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , hashUpdatedUserPassword(newUserKeyPasswordField.getText()));
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

//  delete user code here //

public void getUserReelIdsForUserDeletion() {
    int foundReelId;
    int foundUniqueAttributeId;
    int foundReelLocationId;
    deleteWarning.setContentText("Are you sure you want to delete this user ?");
    Optional<ButtonType> result = deleteWarning.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {



    try {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
        String sql = "SELECT * FROM User_Reels Where userId = ? ";
        PreparedStatement ps;
        ResultSet rs;
        ps = conn.prepareStatement(sql);
        ps.setInt(1 , User.getUserId());
        rs = ps.executeQuery();
        while (rs.next()) {
            foundReelId = rs.getInt("reelId");
            reelIdsToCheckForDeletion.add(foundReelId);
            foundUniqueAttributeId = rs.getInt("uniqueReelAttributeId");
            uniqueReelAttributesToDelete.add(foundUniqueAttributeId);
            foundReelLocationId = rs.getInt("reelLocationId");
            System.out.println("this works");
            reelLocationIdsToDelete.add(foundReelLocationId);

        }
        rs.close();
        ps.close();
        conn.close();


    } catch (Exception e) {
        databaseQueryExecutionError.setContentText("there was a problem retrieving the userReelId based on the given userId");
        databaseQueryExecutionError.showAndWait();
        e.printStackTrace();
    }

    removeUniqueReelAttributeRecords(uniqueReelAttributesToDelete);
    removeReelLocationRecords(reelLocationIdsToDelete);
    checkForDuplicateOfReelIds(reelIdsToCheckForDeletion);
    removeUserReelRecords();
    removeUserRecord();
    userSuccessfullyDeleted.setContentText(" user Has Been Deleted");
    userSuccessfullyDeleted.showAndWait();
}
}

public void checkForDuplicateOfReelIds(ArrayList<Integer> reelIdsToCheckForDeletion) {


    int timesReelIdIsFound;
    for (Integer integer : reelIdsToCheckForDeletion) {
        timesReelIdIsFound = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "SELECT * FROM User_Reels Where reelId = ? ";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , integer);
            rs = ps.executeQuery();
            while (rs.next()) {
                timesReelIdIsFound++;


            }
            rs.close();
            ps.close();
            conn.close();


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText("there was a problem retrieving the userReelId  record  based on the given reelId");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

        if (timesReelIdIsFound == 1) {

            removeReel(integer);
        }

    }




}

public void removeReel(Integer integer){

    try{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
        String sql = "Delete  From reelzs   WHERE  reelId  = ?";
        PreparedStatement ps;
        ps = conn.prepareStatement(sql);
        ps.setInt(1 , integer);

        ps.execute();
        System.out.println("Data has been Deleted successfully");
        ps.close();
        conn.close();

    }catch (Exception e){
        databaseModificationError.setContentText("there was a problem deleting  the selected reel");
        databaseModificationError.showAndWait();

    }



}

public void removeUniqueReelAttributeRecords(ArrayList<Integer> uniqueReelAttributesToDelete){

    for (Integer integer : uniqueReelAttributesToDelete) {
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "Delete  From uniqueReelAttributes   WHERE  uniqueReelAttributeId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , integer);
            ps.execute();
            ps.close();
            conn.close();

        }catch (Exception e){

            databaseModificationError.setContentText("there was a problem deleting  the selected uniqueReelAttribute record");
            databaseModificationError.showAndWait();

            System.out.println(" there was a problem deleting  the selected sound");

        }


    }



}


public void removeReelLocationRecords(ArrayList<Integer> reelLocationIdsToDelete){

    for (Integer integer : reelLocationIdsToDelete) {

        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "Delete  From reelLocation   WHERE  reelLocationId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , integer);

            ps.execute();
            ps.close();
            conn.close();

        }catch (Exception e){
            databaseModificationError.setContentText("there was a problem deleting  the selected reel Location Record");
            databaseModificationError.showAndWait();

        }

    }

}


public void removeUserReelRecords(){

    try{
        Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
        String sql = "Delete  From User_Reels   WHERE  userId  = ?";
        PreparedStatement ps;
        ps = conn.prepareStatement(sql);
        ps.setInt(1 , User.getUserId());
        ps.execute();
        System.out.println("Data has been Deleted successfully");
        ps.close();
        conn.close();

    }catch (Exception e){
        databaseModificationError.setContentText("there was a problem deleting  the selectedUserReel Record");
        databaseModificationError.showAndWait();

        e.printStackTrace();


    }




}


    public void removeUserRecord(){

        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            String sql = "Delete From Users  WHERE  userId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());

            ps.execute();
            System.out.println("Data has been Deleted successfully");
            ps.close();
            conn.close();

        }catch (Exception e){
            databaseModificationError.setContentText("there was a problem deleting  the selected user Record");
            databaseModificationError.showAndWait();



        }




    }

}
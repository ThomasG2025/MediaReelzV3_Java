package Database.UserOperations;


import Classes.ModifyUsersWindowController;
import Database.dbConn;
import Objects.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDelete extends ModifyUsersWindowController {


    public void getUserReelIdsForUserDeletion(){

        int foundReelId;
        int foundUniqueAttributeId;
        int foundReelLocationId;

        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT * FROM User_Reels Where userId = ? ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            rs = ps.executeQuery();
            while (rs.next()) {
                foundReelId = rs.getInt("reelId");
                reelIdsToCheckForDeletion.add(foundReelId);
                foundUniqueAttributeId = rs.getInt("userReelId");
                uniqueReelAttributesToDelete.add(foundUniqueAttributeId);
                foundReelLocationId = rs.getInt("userReelId");
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
        removeUserSecurityProfile();
        removeUserRecord();
        userSuccessfullyDeleted.setContentText(" user Has Been Deleted");
        userSuccessfullyDeleted.showAndWait();
    }


    public void checkForDuplicateOfReelIds(ArrayList<Integer> reelIdsToCheckForDeletion) {


        int timesReelIdIsFound;
        for (Integer integer : reelIdsToCheckForDeletion) {
            timesReelIdIsFound = 0;

            try {
                Connection conn = dbConn.connect();
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
            Connection conn = dbConn.connect();
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
                Connection conn = dbConn.connect();
                String sql = "Delete  From uniqueReelAttributes   WHERE  userReelId  = ?";
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
                Connection conn = dbConn.connect();
                String sql = "Delete  From reelLocation   WHERE  userReelId  = ?";
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

    public int getSecurityProfileId(){
        int securityProfileId = 0;

        try {

            Connection conn = dbConn.connect();
            String sql = "SELECT securityProfileId FROM Users Where userId = ? ";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            rs = ps.executeQuery();
            if (rs.next()) {
                securityProfileId = rs.getInt("securityProfileId");


            }
            rs.close();
            ps.close();
            conn.close();


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText("there was a problem retrieving the userReelId  record  based on the given reelId");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }



    return  securityProfileId;



    }

    public void removeUserReelRecords(){

        try{
            Connection conn = dbConn.connect();
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

    public void removeUserSecurityProfile(){

        int securityProfileId = getSecurityProfileId();

        try{

            Connection conn = dbConn.connect();
            String sql = "Delete * From securityProfiles  WHERE  securityProfileId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , securityProfileId);

            ps.execute();
            System.out.println("Data has been Deleted successfully");
            ps.close();
            conn.close();

        }catch (Exception e){
            databaseModificationError.setContentText("there was a problem deleting  the selected user Record");
            databaseModificationError.showAndWait();



        }



    }

    public void removeUserRecord(){

        try{

            Connection conn = dbConn.connect();
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

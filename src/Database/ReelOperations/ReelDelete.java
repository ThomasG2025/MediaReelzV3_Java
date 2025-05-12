package Database.ReelOperations;


import Database.dbConn;
import Objects.Reel;
import Objects.User;
import javafx.scene.control.TableView;

import java.sql.*;

public class ReelDelete extends Reel {

    private TableView<Reel> reelzsTable;

    public ReelDelete(TableView<Reel> reelzsTable){
        this.reelzsTable =reelzsTable;

    }

    public void checkNumberOfUserReelIdsWithReelId(int reelId){

        int resultSize = 0;

        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT COUNT(*) AS Count  FROM User_Reels  Where reelId = ? ";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , reelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                resultSize = rs.getInt("Count");
                System.out.println("result size print out next Line (line 1073");
                System.out.println(resultSize);

            }

            ps.close();
            rs.close();
            conn.close();


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

        if (resultSize == 1) {
            int retrievedUserReelId = deleteReelGetUserReelId(reelId);
            removeUniqueReelAttributeRecords(retrievedUserReelId);
            removeUserReelIdRecord(retrievedUserReelId);
            deleteReel(reelId);
            reelDeleteGetReelzs();

        } else if (resultSize > 1) {
            int retrievedUserReelId = deleteReelGetUserReelId(reelId);

            removeUniqueReelAttributeRecords(retrievedUserReelId);
            removeUserReelIdRecord(retrievedUserReelId);
            reelDeleteGetReelzs();

        }

        successfulDeleteNotification.setContentText("reel successfully deleted");
        successfulDeleteNotification.showAndWait();

    }

    public void removeUniqueReelAttributeRecords(int uniqueReelAttributeId) {

        try {
            Connection conn = dbConn.connect();
            String sql = "DELETE FROM uniqueReelAttributes  WHERE userReelId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , uniqueReelAttributeId);
            ps.execute();
            ps.close();
            conn.close();
            System.out.println("Data has been Deleted successfully(line 1191)");

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem deleting the uniqueReelAttributes record based on the give uniqueReelAttributeId");
            databaseQueryExecutionError.showAndWait();

            e.printStackTrace();

        }

    }



    public int deleteReelGetUserReelId(int reelId) {

        int userReelId = 0;

        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT userReelId FROM User_Reels  Where userId = ? AND  reelId = ? ";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelId);

            rs = ps.executeQuery();
            if (rs.next()) {
                userReelId = rs.getInt("userReelId");
            } else {
                databaseRetrievalError.setContentText(" there was a problem retrieving the userReelId based on provided reelId TextField text");
                databaseRetrievalError.showAndWait();
            }
            ps.close();
            conn.close();


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

        return userReelId;
    }

    public void removeUserReelIdRecord(int foundUserReelId) {

        try {
            Connection conn = dbConn.connect();
            String sql = "Delete  From User_Reels   WHERE  userReelId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , foundUserReelId);
            ps.execute();
            System.out.println("Data has been Deleted successfully");
            ps.close();
            conn.close();

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText("there was a problem deleting  the selected userReelId record");
            databaseQueryExecutionError.showAndWait();
        }


    }

    public void deleteReel(int reelId) {

        try {
            Connection conn = dbConn.connect();
            String sql = "Delete  From reelzs   WHERE  reelId  = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , reelId);
            ps.execute();
            System.out.println("Data has been Deleted successfully");
            ps.close();
            conn.close();

        } catch (Exception e) {

            e.printStackTrace();
            databaseQueryExecutionError.setContentText("there was a problem deleting  the selected reel");
            databaseQueryExecutionError.showAndWait();

        }

    }

    public void reelDeleteGetReelzs() {

        reelzsList.removeAll(reelzsList);



        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT * FROM reelView Where userId = ? ";
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            rs = ps.executeQuery();
            while (rs.next()) {

                reelzsList.addAll(new Reel(
                        rs.getInt("reelId") ,
                        rs.getString("reelName") ,
                        rs.getString("reelArtist") ,
                        rs.getInt("reelYear") ,
                        rs.getString("reelType") ,
                        rs.getString("reelGenre") ,
                        rs.getString("reelRating") ,
                        rs.getInt("reelIsOriginalStatus") ,
                        rs.getString("note") ,
                        rs.getInt("reelDigitallyArchivedStatus")));

                reelzsTable.setItems(reelzsList);

            }
            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException throwables) {
            databaseRetrievalError.setContentText(" there was a problem retrieving your reelzs from the database");
            databaseRetrievalError.showAndWait();
            throwables.printStackTrace();
        }

    }

    }
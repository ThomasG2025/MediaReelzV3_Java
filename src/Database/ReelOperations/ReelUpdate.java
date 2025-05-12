package Database.ReelOperations;


import Database.dbConn;
import Objects.Reel;
import Objects.User;
import javafx.scene.control.TableView;
import java.sql.*;


public class ReelUpdate extends Reel{

    private TableView<Reel> reelzsTable;

    public ReelUpdate(TableView<Objects.Reel> reelzsTable){
        this.reelzsTable =reelzsTable;

    }


   public  int retrieveUserReelId(int reelId) {

        int userReelId = 0;

       try {
           Connection conn = dbConn.connect();
           String sql = "SELECT userReelId FROM reelView  Where userId = ?  AND reelId  = ?  ";

           PreparedStatement ps;
           ResultSet rs;
           ps = conn.prepareStatement(sql);
           ps.setInt(1 , User.getUserId());
           ps.setInt(2 , reelId);

           rs = ps.executeQuery();
           if (rs.next()) {

             userReelId = rs.getInt("userReelId");



               rs.close();
               ps.close();



           }else{
               rs.close();
               ps.close();

           }
           conn.close();


       } catch (Exception e) {
           databaseRetrievalError.setContentText(" there was a problem verifying that the new reel to add is not a duplicate");
           databaseRetrievalError.showAndWait();
           e.printStackTrace();
       }

       return userReelId;


    }

    public void updateReelTable(int reelId,String reelName,String reelArtist,int reelYear,String reelType, String reelGenre,String reelRating,int reelOriginalStatus,
                                int reelDigitallyArchivedStatus,String reelNotes){
        try {
            Connection conn = dbConn.connect();

            String sql = "UPDATE reelzs" + " SET  reelName = ?, " +
                    "reelArtist = ?," +
                    "reelYear = ?," +
                    "reelType = ?," +
                    "reelGenre = ? ," +
                    "reelRating = ?," +
                    "reelIsOriginalStatus = ?" +
                    "WHERE reelId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelName);
            ps.setString(2 , reelArtist);
            ps.setInt(3 , reelYear);
            ps.setString(4 , reelType);
            ps.setString(5 , reelGenre);
            ps.setString(6 , reelRating);
            ps.setInt(7 , reelOriginalStatus);
            ps.setInt(8 , reelId);
            ps.executeUpdate();
            ps.close();
            conn.close();
            updateUniqueReelAttributes(reelId,reelNotes,reelDigitallyArchivedStatus );

        } catch (SQLException e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();

        }

        reelUpdateGetReelzs();
        successfulUpdateNotification.setContentText("reel successfully updated");
        successfulUpdateNotification.showAndWait();

    }

    public void updateUniqueReelAttributes(int reelId , String reelNotes , int reelDigitallyArchivedStatus ) {

        int userReelId = updateReelGetUserReelId(reelId);

        try {
            Connection conn = dbConn.connect();

            String sql = "UPDATE uniqueReelAttributes SET note = ?, " +
                    "reelDigitallyArchivedStatus = ?" +
                    "WHERE   userReelId = ?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 ,reelNotes);
            ps.setInt(2 , reelDigitallyArchivedStatus);
            ps.setInt(3 , userReelId);
            ps.executeUpdate();
            ps.close();
            conn.close();
            // remove index//

        } catch (SQLException e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();

        }



    }

    public int updateReelGetUserReelId(int reelId) {

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



    public void reelUpdateGetReelzs() {

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
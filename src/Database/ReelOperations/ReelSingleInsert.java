package Database.ReelOperations;


import Database.dbConn;
import Objects.Reel;
import Objects.User;
import javafx.scene.control.TableView;


import java.sql.*;


public class ReelSingleInsert extends Reel {

    private TableView<Reel>reelzsTable;

    public ReelSingleInsert(TableView<Reel> reelzsTable){
        this.reelzsTable =reelzsTable;

    }



    public void verifyReelIsNotDuplicate(String reelName,String reelArtist,int reelYear,String reelType,String reelGenre,String reelRating , int originalStatus,
                                         String  reelNotes,int archivedStatus) {

        int foundReelId;

        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT reelId FROM reelzs  Where reelName = ?  AND reelArtist = ? AND reelYear = ? AND reelType = ?";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelName);
            ps.setString(2 , reelArtist);
            ps.setInt(3 , reelYear);
            ps.setString(4 , reelType);
            rs = ps.executeQuery();
            if (rs.next()) {
                foundReelId = rs.getInt("reelId");
                System.out.println("this works");
                rs.close();
                ps.close();
                conn.close();
                verifyUserReelIdIsNotDuplicate(foundReelId  , reelNotes , archivedStatus);
                getReelzs();
            } else {
                rs.close();
                ps.close();
                conn.close();

                addReel(reelName , reelArtist , reelYear , reelType , reelGenre , reelRating , originalStatus ,  reelNotes , archivedStatus);
            }


        } catch (Exception e) {
            databaseRetrievalError.setContentText(" there was a problem verifying that the new reel to add is not a duplicate");
            databaseRetrievalError.showAndWait();
            e.printStackTrace();
        }


    }

    public void verifyUserReelIdIsNotDuplicate(int reelIdToCheck   , String reelNotes , int archivedStatus) {

        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT userReelId  FROM User_Reels  Where userId = ?  AND reelId  = ? ";

            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelIdToCheck);
            rs = ps.executeQuery();
            if (rs.next()) {

                System.out.println(" nothing to do");
                rs.close();
                ps.close();
                conn.close();


            }

        } catch (Exception e) {
            databaseRetrievalError.setContentText(" there was a problem verifying that the new reel to add is not a duplicate");
            databaseRetrievalError.showAndWait();
            e.printStackTrace();
        }

        addUserReelIdRecord(reelIdToCheck ,   reelNotes, archivedStatus);
    }

    public void addUserReelIdRecord(int reelIdToInsert  , String reelNotes , int archivedStatus) {

        try {
            Connection conn = dbConn.connect();
            String sql = "INSERT INTO  User_Reels  (userId,reelId) Values (?,?) ";

            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelIdToInsert);
            ps.execute();
            int newUserReelId = 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    newUserReelId = rs.getInt(1);
                }

            }

            insertUniqueReelAttributeRecord(newUserReelId , reelNotes , archivedStatus);


            ps.close();
            conn.close();



        } catch (Exception e) {
            databaseModificationError.setContentText(" there was a problem inserting the user reel Id record");
            databaseModificationError.showAndWait();
            e.printStackTrace();
        }

    }

    public void insertUniqueReelAttributeRecord(int userReelId , String reelNotes , int archivedStatus) {

        try {
            Connection conn = dbConn.connect();
            String sql = "INSERT INTO  uniqueReelAttributes  (userReelId,note,reelDigitallyArchivedStatus) Values (?,?,?) ";

            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , userReelId);
            ps.setString(2 , reelNotes);
            ps.setInt(3 , archivedStatus);
            ps.execute();
            ps.close();
            conn.close();

        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }

    }



    public void addReel(String reelName , String reelArtist , int reelYear , String reelType , String reelGenre , String reelRating , int originalStatus  , String reelNotes , int archivedStatus) {

        Reel reel = new Reel();
        reel.setReelName(reelName);
        reel.setReelArtist(reelArtist);
        reel.setReelYear(reelYear);
        reel.setReelType(reelType);
        reel.setReelGenre(reelGenre);
        reel.setReelRating(reelRating);
        reel.setReelIsOriginal(ConvertIntToBoolean(originalStatus));
        reel.setReelNotes(reelNotes);
        reel.setReelDigitallyArchived(ConvertIntToBoolean(archivedStatus));
        reelzsTable.getItems().addAll(reel);

        try {
            Connection conn = dbConn.connect();
            String sql = "INSERT INTO  reelzs  (reelName,reelArtist,reelYear,reelType, reelGenre,reelRating ,reelIsOriginalStatus) Values (?,?,?,?,?,?,?) ";

            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1 , reelName);
            ps.setString(2 , reelArtist);
            ps.setInt(3 , reelYear);
            ps.setString(4 , reelType);
            ps.setString(5 , reelGenre);
            ps.setString(6 , reelRating);
            ps.setInt(7 , originalStatus);
            ps.execute();
            int newReelId = 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    newReelId = rs.getInt(1);
                }

            }
            addUserReelIdRecord(newReelId  , reelNotes , archivedStatus);
            ps.close();
            conn.close();


        } catch (Exception e) {
            databaseQueryExecutionError.setContentText(" there was a problem executing a query");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();
        }


        reelSingleInsertGetReelzs();
        successfulInsertNotification.setContentText("reel Successfully inserted");
        successfulInsertNotification.showAndWait();

    }

    public void reelSingleInsertGetReelzs() {

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

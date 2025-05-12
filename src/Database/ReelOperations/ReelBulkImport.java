package Database.ReelOperations;


import Database.dbConn;
import Objects.Reel;
import Objects.User;
import javafx.scene.control.TableView;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReelBulkImport extends Reel {

    private TableView<Reel> reelzsTable;

    public ReelBulkImport(TableView<Reel> reelzsTable){
        this.reelzsTable =reelzsTable;

    }

    public ArrayList<Reel> bulkImport(File file){
        bulkReelList.removeAll(bulkReelList);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String Line;

            while ((Line = br.readLine()) != null) {

                bulkArray = Line.split(",");
                Reel bulkReel = new Reel();
                bulkReel.setReelName(bulkArray[0]);
                bulkReel.setReelArtist(bulkArray[1]);
                bulkReel.setReelYear(Integer.parseInt(bulkArray[2]));
                bulkReel.setReelType(bulkArray[3]);
                bulkReel.setReelGenre(bulkArray[4]);
                bulkReel.setReelRating(bulkArray[5]);
                bulkReel.setReelIsOriginal(reelBulkImportConvertIntToBoolean(Integer.parseInt(bulkArray[6])));
                bulkReel.setReelNotes(bulkArray[7]);
                bulkReel.setReelDigitallyArchived(reelBulkImportConvertIntToBoolean(Integer.parseInt(bulkArray[8])));

                bulkReelList.add(bulkReel);

            }

        } catch (IOException e) {

            fileReadingError.setContentText(" there was a problem reading the given csv file please make sure all fields(excluding reel notes) have a value");
            fileReadingError.showAndWait();
            e.printStackTrace();
        }

        System.out.println("bulk reel list size " + bulkReelList.size());
       return bulkReelList;



    }





    public void checkForDuplicateReelzs(ArrayList<Reel> approvedBulkReelLocations , ArrayList<Reel> duplicateBulkReelLocations) {
        duplicateBulkReelListForUserReelDuplicateCheck.removeAll(duplicateBulkReelListForUserReelDuplicateCheck);
        approvedBulkReelzsList.removeAll(approvedBulkReelzsList);
        bulkReelList2.removeAll(bulkReelList2);
       int  numberOfBulkReelzsInsertApproved = approvedBulkReelLocations.size();
       int  numberOfBulkReelzsInsertNotApproved = duplicateBulkReelLocations.size();

        for (Reel reel :approvedBulkReelLocations) {

            try {
                Connection conn = dbConn.connect();
                String sql = "SELECT reelId FROM reelzs  Where reelName = ?  AND reelArtist = ? AND reelYear = ? AND reelType = ?";
                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , reel.getReelName());
                ps.setString(2 , reel.getReelArtist());
                ps.setInt(3 , reel.getReelYear());
                ps.setString(4 , reel.getReelType());
                rs = ps.executeQuery();
                if (rs.next()) {
                    Reel duplicateReel = new Reel();
                    duplicateReel.setReelId(rs.getInt("reelId"));
                    duplicateReel.setReelName(reel.getReelName());
                    duplicateReel.setReelArtist(reel.getReelArtist());
                    duplicateReel.setReelYear(reel.getReelYear());
                    duplicateReel.setReelType(reel.getReelType());
                    duplicateReel.setReelGenre(reel.getReelGenre());
                    duplicateReel.setReelRating(reel.getReelRating());
                    duplicateReel.setReelIsOriginal(reel.getReelIsOriginal2());
                    duplicateReel.setReelNotes(reel.getReelNotes());
                    duplicateReel.setReelDigitallyArchived(reel.getReelDigitallyArchived2());
                    duplicateBulkReelListForUserReelDuplicateCheck.add(duplicateReel);


                } else if (!rs.next()) {

                    Reel bulkApprovedReel = new Reel();
                    bulkApprovedReel.setReelName(reel.getReelName());
                    bulkApprovedReel.setReelArtist(reel.getReelArtist());
                    bulkApprovedReel.setReelYear(reel.getReelYear());
                    bulkApprovedReel.setReelType(reel.getReelType());
                    bulkApprovedReel.setReelGenre(reel.getReelGenre());
                    bulkApprovedReel.setReelRating(reel.getReelRating());
                    bulkApprovedReel.setReelIsOriginal(reel.getReelIsOriginal2());
                    bulkApprovedReel.setReelNotes(reel.getReelNotes());
                    bulkApprovedReel.setReelDigitallyArchived(reel.getReelDigitallyArchived2());
                    approvedBulkReelzsList.add(bulkApprovedReel);


                }
                ps.close();
                rs.close();
                conn.close();


            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem retrieving a reel based on given credentials");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }


        verifyBulkUserReelIdIsNotDuplicate(duplicateBulkReelListForUserReelDuplicateCheck);
        addBulkReelzs(approvedBulkReelzsList,numberOfBulkReelzsInsertApproved,numberOfBulkReelzsInsertNotApproved);

    }

    public void verifyBulkUserReelIdIsNotDuplicate(ArrayList<Reel> duplicateBulkReelListForUserReelDuplicateCheck) {
        duplicateUserReelIdsToInsert.removeAll(duplicateUserReelIdsToInsert);

        for (Reel reel : duplicateBulkReelListForUserReelDuplicateCheck) {

            try {
                Connection conn = dbConn.connect();
                String sql = "SELECT userReelId FROM User_Reels Where  userId = ? AND   reelId = ?";

                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , User.getUserId());
                ps.setInt(2 , reel.getReelId());
                rs = ps.executeQuery();
                if (rs.next()) {

                    databaseRetrievalError.setContentText(" one or more instances of  this combination of the user id and reel id already exist ignoring record");
                    databaseRetrievalError.showAndWait();

                } else {

                    duplicateUserReelIdsToInsert.add(reel);

                }
                ps.close();
                rs.close();
                conn.close();


            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem verifying if one or more userReelIds are duplicates");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        addBulkUserReelIdRecord(duplicateUserReelIdsToInsert);

    }

    public void addBulkUserReelIdRecord(ArrayList<Reel> duplicateUserReelIdsToInsert) {

        for (Reel reel : duplicateUserReelIdsToInsert) {

            try {
                Connection conn = dbConn.connect();
                String sql = "INSERT INTO   User_Reels  (userId,reelId) Values  (?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , User.getUserId());
                ps.setInt(2 , reel.getReelId());
                ps.execute();
                ps.close();
                conn.close();
                System.out.println("successful data insertion (line 1072)");

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting  one or more userReelId record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }


        insertBulkUniqueReelAttributeRecords2(duplicateUserReelIdsToInsert);

    }

    public void insertBulkUniqueReelAttributeRecords2(ArrayList<Reel> duplicateUserReelIdsToInsert) {

        for (Reel reel : duplicateUserReelIdsToInsert) {
            int userReelIdForInsert = retrieveUserReelId2(reel.getReelId());


            try {
                Connection conn = dbConn.connect();
                String sql = "INSERT INTO  uniqueReelAttributes  (userReelId,note,reelDigitallyArchivedStatus) VALUES (?,?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , userReelIdForInsert);
                ps.setString(2 , reel.getReelNotes());
                ps.setInt(3 , ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()));
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more uniqueReelAttributes record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }



    }



    public int retrieveUserReelId2(int reelId) {

        int userReelId = 0;
        try {
            Connection conn = dbConn.connect();
            String sql = "SELECT userReelId From  User_Reels  WHERE  userId = ? And reelId = ?";
            ResultSet rs;
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setInt(1 , User.getUserId());
            ps.setInt(2 , reelId);
            rs = ps.executeQuery();
            if (rs.next()) {
                userReelId = rs.getInt("userReelId");

            }
            ps.close();
            rs.close();
            conn.close();

        } catch (Exception e) {

            databaseQueryExecutionError.setContentText("there was a problem retrieving  the  userReelId record based on given credentials");
            databaseQueryExecutionError.showAndWait();
            e.printStackTrace();

        }

        return userReelId;
    }

    public void addBulkReelzs(ArrayList<Reel> bulkReelzsForInsertReel , int numberOfBulkReelzsInsertApproved , int numberOfBulkReelzsInsertNotApproved) {

        for (Reel reel : bulkReelzsForInsertReel) {

            try {
                Connection conn = dbConn.connect();
                String sql = "INSERT INTO  reelzs  (reelName,reelArtist,reelYear,reelType, reelGenre, reelRating ,reelIsOriginalStatus) Values (?,?,?,?,?,?,?) ";

                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , reel.getReelName());
                ps.setString(2 , reel.getReelArtist());
                ps.setInt(3 , reel.getReelYear());
                ps.setString(4 , reel.getReelType());
                ps.setString(5 , reel.getReelGenre());
                ps.setString(6 , reel.getReelRating());
                ps.setInt(7 , ConvertCheckboxResultToInt(reel.getReelIsOriginal2()));
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more reel");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }
        }

        retrieveBulkNewReelIds(bulkReelzsForInsertReel,numberOfBulkReelzsInsertApproved,numberOfBulkReelzsInsertNotApproved);
    }

    public void retrieveBulkNewReelIds(ArrayList<Reel> bulkReelzsForInsertReel , int numberOfBulkReelzsInsertApproved , int numberOfBulkReelzsInsertNotApproved) {
        int foundNewBulkReelId;

        for (Reel reel : bulkReelzsForInsertReel) {

            try {
                Connection conn = dbConn.connect();
                String sql = "SELECT reelId FROM reelzs  Where reelName = ?  AND reelArtist = ? AND reelYear = ? AND reelType = ? AND reelGenre = ?";
                PreparedStatement ps;
                ResultSet rs;
                ps = conn.prepareStatement(sql);
                ps.setString(1 , reel.getReelName());
                ps.setString(2 , reel.getReelArtist());
                ps.setInt(3 , reel.getReelYear());
                ps.setString(4 , reel.getReelType());
                ps.setString(5 , reel.getReelGenre());
                rs = ps.executeQuery();
                if (rs.next()) {
                    foundNewBulkReelId = rs.getInt("reelId");
                    repackageBulkReelzsList(foundNewBulkReelId , reel);

                }
                rs.close();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem selecting one or more reelzs record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }

        addBulkUserReelIdRecord2(numberOfBulkReelzsInsertApproved,numberOfBulkReelzsInsertNotApproved);

    }

    public void repackageBulkReelzsList(int bulkReelzsId , Reel reel) {

        Reel bulkReel2 = new Reel();
        bulkReel2.setReelId(bulkReelzsId);
        bulkReel2.setReelName(reel.getReelName());
        bulkReel2.setReelArtist(reel.getReelArtist());
        bulkReel2.setReelYear(reel.getReelYear());
        bulkReel2.setReelType(reel.getReelType());
        bulkReel2.setReelGenre(reel.getReelGenre());
        bulkReel2.setReelRating(reel.getReelRating());
        bulkReel2.setReelNotes(reel.getReelNotes());
        bulkReel2.setReelIsOriginal(reel.getReelIsOriginal2());
        bulkReel2.setReelDigitallyArchived(reel.getReelDigitallyArchived2());
        bulkReelList2.add(bulkReel2);

    }

    public void addBulkUserReelIdRecord2( int numberOfBulkReelzsInsertApproved , int numberOfBulkReelzsInsertNotApproved) {

        for (Reel reel : bulkReelList2) {

            try {
                Connection conn = dbConn.connect();
                String sql = "INSERT INTO   User_Reels  (userId,reelId) Values  (?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , User.getUserId());
                ps.setInt(2 , reel.getReelId());
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {

                databaseQueryExecutionError.setContentText("there was a problem inserting one or more  userReelId record");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();

            }

        }


        insertBulkUniqueReelAttributeRecords(numberOfBulkReelzsInsertApproved,numberOfBulkReelzsInsertNotApproved);

    }

    public void insertBulkUniqueReelAttributeRecords(int numberOfBulkReelzsInsertApproved , int numberOfBulkReelzsInsertNotApproved) {


        for (Reel reel : bulkReelList2) {
            int userReelIdForInsert = retrieveUserReelId2(reel.getReelId());


            try {
                Connection conn = dbConn.connect();
                String sql = "INSERT INTO  uniqueReelAttributes  (userReelId,note,reelDigitallyArchivedStatus) VALUES (?,?,?)";
                PreparedStatement ps;
                ps = conn.prepareStatement(sql);
                ps.setInt(1 , userReelIdForInsert);
                ps.setString(2 , reel.getReelNotes());
                ps.setInt(3 , ConvertCheckboxResultToInt(reel.getReelDigitallyArchived2()));
                ps.execute();
                ps.close();
                conn.close();

            } catch (Exception e) {
                databaseQueryExecutionError.setContentText("there was a problem inserting one or more uniqueReelAttributes record ");
                databaseQueryExecutionError.showAndWait();
                e.printStackTrace();
            }


        }

        System.out.println(" number of approved for insert" + numberOfBulkReelzsInsertApproved);
        System.out.println(" number of not approved for insert" + numberOfBulkReelzsInsertNotApproved);


    }






    public boolean reelBulkImportConvertIntToBoolean(int status) {
        boolean returnedStatus = false;
        if (status == 1) {
            returnedStatus = true;

        }

        return returnedStatus;
    }


}

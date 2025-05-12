

import Database.dbConn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        createSecurityProfileTable();
        createUsersTable();
        createReelTable();
        createUserReelTable();
        createUniqueReelAttributesTable();
        createUserSecurityView();
        createReelView();











        Parent root = FXMLLoader.load(getClass().getResource("/MediaReelzV2/FXML/Login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root , 490 , 329));
        primaryStage.show();
    }





    public void createSecurityProfileTable(){

        try(Connection conn = dbConn.connect();
            Statement stmt = conn.createStatement()
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS securityProfiles " +
                    "( securityProfileId INTEGER not Null," +
                    "securityQuestion1 TEXT not NULL," +
                    "securityQuestion1Answer TEXT not NULL," +
                    "securityQuestion2 TEXT not NULL," +
                    "securityQuestion2Answer TEXT not NULL," +
                    "securityQuestion3 TEXT not NULL," +
                    "securityQuestion3Answer TEXT not NULL, " +
                    "PRIMARY KEY(securityProfileId ))";
            stmt.executeUpdate(sql);

        }catch (SQLException throwables){
//            tableCreationError.setContentText("there was a problem creating one or more database tables");
//            tableCreationError.showAndWait();
            throwables.printStackTrace();

        }

    }

    public void createUsersTable(){

        try(Connection conn = dbConn.connect();
            Statement stmt = conn.createStatement()
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS Users " +
                    "(userId INTEGER not Null," +
                    "userName TEXT not NULL ," +
                    " hash  TEXT not NULL ," +
                    "securityProfileId INTEGER,"+
                    "FOREIGN KEY (securityProfileId) REFERENCES securityProfiles (securityProfileId)"+
                    "PRIMARY KEY(userId ))";
            stmt.executeUpdate(sql);

        }catch (SQLException throwables){
//            tableCreationError.setContentText("there was a problem creating one or more database tables");
//            tableCreationError.showAndWait();
            throwables.printStackTrace();

        }

    }


    public void createUserSecurityView(){

        try(Connection conn = dbConn.connect();
            Statement stmt = conn.createStatement()
        ) {

            String sql = "CREATE VIEW IF NOT EXISTS userSecurityView  AS  " +
                    "SELECT u.userID ," +
                    "u.userName," +
                    "u.hash," +
                    "u.securityProfileId," +
                    "sp.securityQuestion1," +
                    "sp.securityQuestion1Answer," +
                    "sp.securityQuestion2," +
                    "sp.securityQuestion2Answer," +
                    "sp.securityQuestion3," +
                    "sp.securityQuestion3Answer " +
                    "FROM Users AS u " +
                    "INNER JOIN securityProfiles AS sp ON u.securityProfileId = sp.securityProfileId";
            stmt.executeUpdate(sql);

        }catch (SQLException throwables){
//            tableCreationError.setContentText("there was a problem creating one or more database tables");
//            tableCreationError.showAndWait();
            throwables.printStackTrace();

        }




    }




    public void createReelTable(){

        try(Connection conn = dbConn.connect();
            Statement stmt = conn.createStatement()
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS reelzs " +
                    "( reelId INTEGER not Null," +
                    "reelName TEXT not NULL ," +
                    "reelArtist  TEXT not NULL," +
                    "reelYear INTEGER not NULL," +
                    "reelType TEXT not NULL,"+
                    "reelGenre TEXT ,"+
                    "reelRating TEXT,"+
                    "reelIsOriginalStatus Tiny Int ,"+
                    "PRIMARY KEY(reelId ))";
            stmt.executeUpdate(sql);

        }catch (SQLException throwables){
//            tableCreationError.setContentText("there was a problem creating one or more database tables");
//            tableCreationError.showAndWait();
            throwables.printStackTrace();

        }

    }

    public void createUserReelTable(){


        try(Connection conn = dbConn.connect();
            Statement stmt = conn.createStatement()
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS User_Reels " +
                    "(userReelId INTEGER  not Null," +
                    "userId INTEGER NOT NULL ," +
                    "reelId INTEGER not NULL," +
                    "PRIMARY KEY(userReelId )"+
                    "FOREIGN KEY (userId) REFERENCES Users (userId)"+
                    "FOREIGN KEY (reelId) REFERENCES Reelz (reelId))";
            stmt.executeUpdate(sql);

        }catch (SQLException throwables){
//            tableCreationError.setContentText("there was a problem creating one or more database tables");
//            tableCreationError.showAndWait();
            throwables.printStackTrace();

        }


    }



    public void createUniqueReelAttributesTable(){

        try(Connection conn = dbConn.connect();
            Statement stmt = conn.createStatement()
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS uniqueReelAttributes " +
                    "( userReelId INTEGER  NOT Null ," +
                    "note Text," +
                    "reelDigitallyArchivedStatus TinyInt ,"+
                    "FOREIGN KEY (userReelId)  REFERENCES  User_Reels  (userReelId))";
            stmt.executeUpdate(sql);

        }catch (SQLException throwables){
//            tableCreationError.setContentText("there was a problem creating one or more database tables");
//            tableCreationError.showAndWait();
            throwables.printStackTrace();

        }


    }




    public void createReelView(){

        try(Connection conn = dbConn.connect();
            Statement stmt = conn.createStatement()
        ) {

            String sql = "CREATE VIEW IF NOT EXISTS reelView  AS  " +
                    "SELECT ur.userReelId , " +
                    "ur.userId, " +
                    "ur.reelId, " +
                    "r.reelName, " +
                    "r.reelArtist, " +
                    "r.reelYear, " +
                    "r.reelType," +
                    "r.reelGenre," +
                    "r.reelRating," +
                    "r.reelIsOriginalStatus," +
                    "rua.note, " +
                    "rua.reelDigitallyArchivedStatus " +
                    "FROM User_Reels AS ur " +
                    "INNER JOIN reelzs AS r ON ur.reelId = r.reelId "+
                    "INNER JOIN uniqueReelAttributes AS rua ON ur.userReelId = rua.userReelId";
            stmt.executeUpdate(sql);

        }catch (SQLException throwables){
//            tableCreationError.setContentText("there was a problem creating one or more database tables");
//            tableCreationError.showAndWait();
            throwables.printStackTrace();

        }

    }

}

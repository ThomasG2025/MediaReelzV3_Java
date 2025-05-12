package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// code in part from  https://stackoverflow.com/questions/11125053/jdbc-connection-using-different-files //
public class dbConn {


    public static Connection connect() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Reelzs.db");
            if (conn != null) {
                System.out.println(" ");
            } else {
                System.out.println("Failed to make connection!");
            }
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            throw e;
        }



    }

}

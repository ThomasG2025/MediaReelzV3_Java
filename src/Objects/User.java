package Objects;

public class User {
    private static int userId;

    private static String userName;
    private String hash;


    public User() {

        userId = 0;
        userName = "";
        this.hash = "";


    }


    public User(int userId , String userName , String hash) {
        User.userId = userId;
        User.userName = userName;
        this.hash = hash;


    }

    public static String getUserName() {
        return userName;
    }

    public static void setuserName(String userName) {
        User.userName = userName;
    }


    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        User.userId = userId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}


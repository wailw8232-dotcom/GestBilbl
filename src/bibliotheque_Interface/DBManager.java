package bibliotheque_Interface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBManager {
    private static final String URL = "jdbc:mysql:YOUR_db_URL:3306/GestBibl";
    private static String user;
    private static String password;

    // Called once, from LoginForm, right after a successful login
    public static void setCredentials(String user, String password) {
        DBManager.user = user;
        DBManager.password = password;
    }

    // Called by every panel whenever it needs a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, user, password);
    }
}

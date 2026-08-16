package bibliotheque_Interface;

import java.sql.Connection;
import java.sql.DriverManager;


public class DBConnection {

    public static Connection getConnection() {
        Connection conn = null;

        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Gestbibl;encrypt=true;trustServerCertificate=true;";
            String user = "";
            String password = "";

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie !");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}

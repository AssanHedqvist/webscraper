package se.hedsec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private Connection con;
    private static DBManager instance = null;

    private static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() {

        String url = "jdbc:mysql://localhost:3306/foodapp";
        String user = "root";
        String pass = "root";

        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() {
        return getInstance().con;
    }
}

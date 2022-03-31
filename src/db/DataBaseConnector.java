package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  Connection to MySQL database
 */

public class DataBaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/first_swing_db1";
    private static final String USER = "root";
    private static final String PASSWORD = "654200";

    private static Connection con;

    public void connect() {

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con;
    }
}
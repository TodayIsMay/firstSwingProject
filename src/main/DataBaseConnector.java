package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {

    private static final String url = "jdbc:mysql://localhost:3306/first_swing_db";
    private static final String user = "root";
    private static final String password = "654200";

    private static Connection con;

    public void connect() {

        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con;
    }
}
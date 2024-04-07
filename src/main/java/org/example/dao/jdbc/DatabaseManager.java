package org.example.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance = null;

    private final String url;

    private final String user;

    private final String password;

    public static DatabaseManager getInstance() {
        if (DatabaseManager.instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    private DatabaseManager() {
        this.url = "jdbc:postgresql://server/user";
        this.user = "user";
        this.password = "password";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver", e);
        }
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

}

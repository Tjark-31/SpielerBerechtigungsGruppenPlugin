package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection { // Datenbankverbindung herstellen 

    // Beispiel Verbindungsinformationen
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Datenbank";
    private static final String USER = "Benutzername";
    private static final String PASSWORD = "Passwort";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException( Config.getDatabaseError(), e);
        }
    }
}

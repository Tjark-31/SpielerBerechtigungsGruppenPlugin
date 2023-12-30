package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GruppenDAO { // baut auf die Commands auf, Schnittstelle Commands und Datenbankverbindung


    public static void erstelleGruppe(String name, String prefix) { //für /creategroup
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Groups (groupName, groupPrefix) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                
                statement.setString(1, name);
                statement.setString(2, prefix);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); //SqlException wird aufgefangen 
            
        }
    }

    public static void trittGruppeBei(MyPlayer myplayer, String groupName, String groupPrefix) { //für /joingroup 
        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertSql = "INSERT INTO players (playerName, groupName, prefix ) VALUES (?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, myplayer.getName());
                insertStatement.setString(2, groupName);
                insertStatement.setString(3, groupPrefix);
                myplayer.saveToDatabase(myplayer);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); //SqlException wird aufgefangen 
            
        }
    }

    public static void addPlayertoGroup(MyPlayer myplayer, String groupName, String groupPrefix){ //für /add
        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertSql = "INSERT INTO players (playerName, groupName, prefix) VALUES (?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, myplayer.getName());
                insertStatement.setString(2, groupName);
                insertStatement.setString(3, groupPrefix);
                myplayer.saveToDatabase(myplayer);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();  //SqlException wird aufgefangen 
            
        }
       
    }
// zur Überprüfung ob die gesuchte Gruppe in der Datenbank vorhanden ist 
    public static boolean findGroup(String groupName) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // SQL-Abfrage für die gewünschte Spalte
            String sqlQuery = "SELECT groupNmae FROM groups";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
    
                // Ergebnisse der Abfrage abrufen
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Iteriere über die Ergebnisse
                    while (resultSet.next()) {
                        // Hole den Wert aus der Spalte
                        String columnValue = resultSet.getString("groupName");
    
                        // Vergleiche den Wert 
                        if (columnValue.equals(groupName)) {
                            System.out.println("Gefunden: " + columnValue);
                            return true;
                        }
                    }
                }
            }
            return false; // Gruppe wurde nicht gefunden
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Keine Verbindung zur Datenbank");
        }
    }
}




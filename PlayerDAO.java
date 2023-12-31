package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerDAO {
    // für das erstellen von Player Objekten wenn ein Spieler dem Server joint 
    public static MyPlayer getPlayerFromDatabase(UUID playerID) {
        String sql = "SELECT * FROM players WHERE playerID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, playerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String playerName = resultSet.getString("playerName");
                    String groupName = "DefaultGroup";
                    String prefix = "[Def]";
                    boolean perm = true; //Standardgruppe immer permanent  
                    LocalDateTime groupAssignmentTime = null; // weil man der Standardgruppe hinzugefügt wird
                    long durationMonths = resultSet.getLong("durationMonths"); // Werte sind sowieso alle 0
                    long durationDays = resultSet.getLong("durationDays");
                    long durationHours = resultSet.getLong("durationHours");
                    long durationMinutes = resultSet.getLong("durationMinutes");
                    long durationSeconds = resultSet.getLong("durationSeconds");
                    Player bukkitPlayer = Bukkit.getPlayer(playerID);

                    MyPlayer player = new MyPlayer(playerName, playerID, prefix, groupName, groupAssignmentTime, perm, durationMonths, durationDays, durationHours, durationMinutes, durationSeconds, bukkitPlayer);
                    player.saveToDatabase(player);
                    
                    return  player ;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

        return null;
    }

    // um aktuelle Daten zu bekommen, immer wen man auf die Daten eines Spielers zugreift 
    public static MyPlayer getcurrentPlayerFromDatabase(UUID playerID) {
        String sql = "SELECT * FROM players WHERE playerID = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
    
            statement.setObject(1, playerID);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String playerName = resultSet.getString("playerName");
                    String groupName = resultSet.getString("groupName");
                    String prefix = resultSet.getString("prefix");
                    boolean perm = resultSet.getBoolean("perm");
                    LocalDateTime groupAssignmentTime = resultSet.getObject("groupAssignmentTime", LocalDateTime.class);
                    long durationMonths = resultSet.getLong("durationMonths");
                    long durationDays = resultSet.getLong("durationDays");
                    long durationHours = resultSet.getLong("durationHours");
                    long durationMinutes = resultSet.getLong("durationMinutes");
                    long durationSeconds = resultSet.getLong("durationSeconds");
    
                    // Prüfen, ob der Bukkit-Spieler online ist
                    Player bukkitPlayer = Bukkit.getPlayer(playerID);
                    if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
                        // Spieler ist offline, versuche Spieler-ID aus der Datenbank zu bekommen
                        UUID offlinePlayerID = getPlayerIDFromDatabase(playerName);
                        if (offlinePlayerID != null) {
                            bukkitPlayer = Bukkit.getPlayer(offlinePlayerID);
                        }
                    }

    
                    MyPlayer player = new MyPlayer(playerName, playerID, prefix, groupName, groupAssignmentTime, perm, durationMonths, durationDays, durationHours, durationMinutes, durationSeconds, bukkitPlayer);
    
                    player.saveToDatabase(player);

                    return player;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
    //überprüfen ob der Spieler schon in der Datenbank vorhanden ist 
    public static boolean findPlayer(UUID playerID) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // SQL-Abfrage für die gewünschte Spalte
            String sqlQuery = "SELECT playerID FROM bukkitplayers";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
    
                // Ergebnisse der Abfrage abrufen
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Iteriere über die Ergebnisse
                    while (resultSet.next()) {
                        // Hole den Wert aus der Spalte
                        UUID columnValue = UUID.fromString(resultSet.getString("playerID"));
    
                        // Vergleiche den Wert 
                        if (columnValue.equals(playerID)) {
                            System.out.println("Gefunden: " + columnValue);
                            return true;
                        }
                    }
                }
            }
            return false; // Spieler wurde nicht gefunden
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(Config.getNoDatabaseConnectionMessage());
        }
    }
    
    
//PlayerID bekommen wenn Player offline
    public static UUID getPlayerIDFromDatabase(String playerName) {
        String sql = "SELECT playerID FROM players WHERE playerName = ?";
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
    
            statement.setString(1, playerName);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return UUID.fromString(resultSet.getString(Config.getPlayerIDDatabaseErrorMessage()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    
        return null;
    }
    
}



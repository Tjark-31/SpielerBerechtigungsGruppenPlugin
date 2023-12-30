package com.example;

import org.bukkit.entity.Player;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class MyPlayer {  // zum erstellen und verwalten von Spielern 
    
    private Player bukkitPlayer; 
    private int n;        // für die checkassignment Methode für Fallunterscheidung 
    
    // wenn ein Spieler nur eine bestimmte Zeit einer Gruppe angehört
    public long durationMonths;
    public long durationDays; 
    public long durationHours;
    public long durationMinutes;
    public long durationSeconds;
    private String name;
    private UUID playerID;
    private String groupname;
    private String prefix;
    private boolean perm;
    private LocalDateTime groupAssignmentTime; // Zeitpunkt zu dem der Spieler einer Gruppe beigetreten ist, wenn diese null ist 
                                               // gehört der Spieler der Gruppe ohne Zeitbegrenzung an
    private String defaultGroupname = "DefaultGroup"; // Gruppe die jeder Spieler angehört wenn er keiner anderen Gruppe angehört 
    private String defaultPrefix = "[Def]"; 

    public MyPlayer(String name, UUID playerID, Player bukkitPlayer ) { // für Spielererstellung wenn der Spieler zum esten mal dem Server joint
        this.name = name;
        this.playerID = playerID;
        this.bukkitPlayer = bukkitPlayer;
        this.prefix = defaultPrefix;
        this.groupname = defaultGroupname;
        this.groupAssignmentTime = null; // Startwert für die Zeit der Zuordnung
        this.perm = true;
        durationMonths = 0;
        durationDays = 0;
        durationHours = 0;
        durationMinutes = 0;
        durationSeconds = 0;
    }

    // für Spielerobjektverwaltung mit der Datenbank 
    public MyPlayer(String name, UUID playerID, String prefix, String groupname, LocalDateTime groupAssignmentTime, boolean perm, long durationMonths, long durationDays, long durationHours, long durationMinutes, long durationSeconds, Player bukkitPlayer) {
        this.name = name;
        this.playerID = playerID;
        this.bukkitPlayer = bukkitPlayer;
        this.prefix = prefix;
        this.groupname = groupname;
        this.groupAssignmentTime = groupAssignmentTime;
        this.perm = perm;
        this.durationMonths = durationMonths;
        this.durationDays = durationDays;
        this.durationHours = durationHours;
        this.durationMinutes = durationMinutes;
        this.durationSeconds = durationSeconds;
    }

    
 
    // getMethoden:

                public Player getBukkitPlayer(){
                    return bukkitPlayer;
                }

                public String getName() {
                    return name;
                }

                public String getprefix(){
                    return  this.prefix ;
                }
        
                public UUID getPlayerID() {
                    return playerID;
                }

                public String getGroupname() {
                    return groupname;
                }

                public boolean getperm(){
                    return perm;
                }

                public long getDurationMonths(){
                    return this.durationMonths;
                }
                public long getDurationDays(){
                    return this.durationDays;
                }
                public long getDurationHours(){
                    return this.durationHours;
                }
                public long getDurationMinutes(){
                    return this.durationMinutes;
                }
                public long getDurationSeconds(){
                    return this.durationSeconds;
                }

                public LocalDateTime getAssignmenetTime(){
                    return this.groupAssignmentTime ;
                }

                public String getFormattedPrefix() {
                    String groupPrefix = getprefix();
                    return "[" + groupPrefix + "]";
                }
 
    // setzen des Gruppen Namens und falls nur temporär mit Zeitangabe und perm auf false
    public void setGroupname(String groupname, LocalDateTime assignmentTime) {
        this.groupname = groupname;
        if( assignmentTime != null){
            this.groupAssignmentTime = assignmentTime;
            this.perm = false;
        }
        saveToDatabase(this);
    }
    public void setTime( long dMo, long dD, long dH, long dMi, long dS){
                durationMonths = dMo; 
                durationHours = dH;
                durationDays = dD;
                durationMinutes = dMi;
                durationSeconds = dS;
                saveToDatabase(this);
    }
    public void setAssignmentTime(LocalDateTime assignmentTime){
        this.groupAssignmentTime =  assignmentTime;
        saveToDatabase(this);
    }

    public void setGroupprefix(String prefix){
        this.prefix = prefix ;
        saveToDatabase(this);
    }

    public void joinGroup( String groupName, LocalDateTime assignmentTime , long dMo, long dD, long dH, long dMi, long dS){
        
        if( assignmentTime != null ){

             this.setAssignmentTime(assignmentTime);
             this.setTime(dMo, dD, dH, dMi, dS);
        }
        this.setGroupname(groupName, assignmentTime);
        this.checkassignment();
        saveToDatabase(this);
        
    }
    
 // Methode um zu bestimmen welche Gruppe ein Spieler angehört und wenn diese temporär ist wie lange der Spieler der Gruppe angehört 
// wenn die Zeit abglaufen ist wird das auch angezeigt 

    public int checkassignment(){

        
        boolean x;
        

        LocalDateTime currentTime = LocalDateTime.now();


        x = this.isGroupAssignmentExpired( currentTime, getDurationMonths(), getDurationDays(), getDurationHours(), getDurationMinutes(), getDurationSeconds());

        

        if( x == true && perm == false )
        {
           n = 1;                        //"Deine Gruppenzeit in der Gruppe" + currentgroup + "ist abgelaufen"
        }
        else if(x == false && perm == false ){

           n = 2;                                  //"Deine Gruppenzeit läuft noch" + durationMonths + "Monate" + durationDays + "Tage" + durationHours + "Stunden" + durationMinutes + "Minuten" + durationSeconds + "Sekunden"
        }
        else if(x == true && perm == true){

           n = 3;                              //"Deine Gruppe ist:" + currentgroup permanente Gruppe
        }
        else{
            throw new RuntimeException("Ein Fehler ist bei der Gruppenzuweisung aufgetreten");
        }
       
        return n;

    }

    // wird von checkassignment benutzt 

    public boolean isGroupAssignmentExpired(LocalDateTime currentTime, long durationMonths, long durationDays, long durationHours, long durationMinutes, long durationSeconds) {
        if (this.groupAssignmentTime != null) {
            LocalDateTime expirationTime = groupAssignmentTime
                    .plus(durationMonths, ChronoUnit.MONTHS)
                    .plus(durationDays, ChronoUnit.DAYS)
                    .plus(durationHours, ChronoUnit.HOURS)
                    .plus(durationMinutes, ChronoUnit.MINUTES)
                    .plus(durationSeconds, ChronoUnit.SECONDS);
 
            if (currentTime.isAfter(expirationTime)) {
                // Wenn abgelaufen, setze Standardgruppe
                this.setGroupname(defaultGroupname , null);
                this.setGroupprefix(defaultPrefix);
                this.setAssignmentTime(null); // Zurücksetzen der Zeitangabe
                this.setTime(0, 0, 0, 0, 0);
                saveToDatabase(this);
                return true;
            }
            return false;
        }
        return true; // Wenn keine Zuordnung erfolgt ist, wird als abgelaufen, standard oder permanent  betrachtet
    }

    public void saveToDatabase(MyPlayer myplayer) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        // Überprüfen, ob der Spieler bereits in der Datenbank existiert
        if (playerExistsInDatabase(myplayer.getPlayerID())) {
            // Wenn der Spieler existiert, aktualisiere die Daten
            updateInDatabase(myplayer);
        } else {
            // Wenn der Spieler nicht existiert, füge einen neuen Datensatz hinzu
            String query = "INSERT INTO players (name, groupname, prefix, perm, group_assignment_time, duration_months, duration_days, duration_hours, duration_minutes, duration_seconds) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, myplayer.getName());
                preparedStatement.setString(2, myplayer.getGroupname());
                preparedStatement.setString(3, myplayer.getprefix());
                preparedStatement.setBoolean(4, myplayer.getperm());
                preparedStatement.setObject(5, myplayer.getAssignmenetTime());
                preparedStatement.setLong(6, myplayer.getDurationMonths());
                preparedStatement.setLong(7, myplayer.getDurationDays());
                preparedStatement.setLong(8, myplayer.getDurationHours());
                preparedStatement.setLong(9, myplayer.getDurationMinutes());
                preparedStatement.setLong(10, myplayer.getDurationSeconds());

                preparedStatement.executeUpdate();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Überprüfen, ob ein Spieler mit der gegebenen playerID bereits in der Datenbank existiert
public boolean playerExistsInDatabase(UUID playerID) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "SELECT COUNT(*) FROM players WHERE playerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, playerID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

// Methode zur Aktualisierung der Spielerdaten in der Datenbank
public void updateInDatabase(MyPlayer myplayer) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        String query = "UPDATE players SET name = ?, groupname = ?, prefix = ?, perm = ?, group_assignment_time = ?, " +
                       "duration_months = ?, duration_days = ?, duration_hours = ?, duration_minutes = ?, " +
                       "duration_seconds = ? WHERE playerID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, myplayer.getName());
            preparedStatement.setString(2, myplayer.getGroupname());
            preparedStatement.setString(3, myplayer.getprefix());
            preparedStatement.setBoolean(4, myplayer.getperm());
            preparedStatement.setObject(5, myplayer.getAssignmenetTime());
            preparedStatement.setLong(6, myplayer.getDurationMonths());
            preparedStatement.setLong(7, myplayer.getDurationDays());
            preparedStatement.setLong(8, myplayer.getDurationHours());
            preparedStatement.setLong(9, myplayer.getDurationMinutes());
            preparedStatement.setLong(10, myplayer.getDurationSeconds());
            preparedStatement.setObject(11, myplayer.getPlayerID());

            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
  }
}

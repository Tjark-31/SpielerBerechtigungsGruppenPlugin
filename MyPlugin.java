package com.example;

import java.io.File;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;



public class MyPlugin extends JavaPlugin {

    private FileConfiguration config;

    @Override 
    @SuppressWarnings("deprecation")
    public void onEnable() {
        // für anpassbare Konfigurationsdatei
        createConfig();
        config = getConfig();
        config.options().copyDefaults(true);


       
        // Füge Standardwerte für Schlüssel hinzu, falls sie fehlen
        config.addDefault("messages.welcome", "&a\"Willkommen, Spieler \" + \"[\" + %groupPrefix% + \"]\" %playerName% + \"!\" ");

        config.addDefault("messages.goodbye", "&c \"Auf Wiedersehen, Spieler \" + \"[\" + %groupPrefix% + \"]\" %playerName% + \"!\" ");

        config.addDefault("messages.abgelaufeneGruppenZeit", "&b\"Deine Gruppenzeit in Gruppe \" + %currentGroup% + \"[\" + %currentPrefix% + \"]\"\n" + //
                "    + \" ist abgelaufen. Deine neue Gruppe:\" + %afterGroup% + \"[\" + %afterPrefix% + \"]\" ");

        config.addDefault("messages.gruppenZuweisungsfehler", "&b \"Ein Fehler ist bei der Gruppenzuweisung aufgetreten\" !");

        config.addDefault("messages.databaseerror", "&b \"PostgreSQL %Datenbank% Driver nicht gefunden.\" !");

        config.addDefault("messages.joingrouperror", "&b\"Verwendung: /joingroup <%Gruppenname%> <%Prefix%> <%DurationMonths%> <%DurationDays%> <%DurationHours%> <%DurationMinutes%> <%DurationSeconds%>\" ");

        config.addDefault("messages.joingroup", "&b\"Du bist der Gruppe '\" + groupName + \"[\" + groupPrefix + \"]\" +\"' beigetreten!\" ");

        config.addDefault("messages.checkgroupname", "&b\"Fehler beim Beitreten zur Gruppe. Überprüfe den Gruppennamen.\" ");

        config.addDefault("messages.creategrouperror", "&b\"Verwendung: /creategroup <%Name%> <%Prefix%>\"");

        config.addDefault("messages.creategroup", "&b\"Gruppe '\" + %groupName% + \"' wurde erstellt!\"");

        config.addDefault("messages.showgrouperror", "&bVerwendung: /showgroup ");

        config.addDefault("messages.expiredshowgrouptime", "&b \"Deine Gruppenzeit in Gruppe \" + %currentGroup% + \"[\" + %currentPrefix% + \"]\"\n" + //
                "    + \" ist abgelaufen. Deine neue Gruppe:\" + &afterGroup% + \"[\" + %afterPrefix% + \"]\" ");

        config.addDefault("messages.showgrouptime", "&b \"Du bist in der Gruppe:\" + %afterGroup% + \"[\" + %afterPrefix% + \"]\" +\n" + //
                "                    \" für \" + %myplayer.getDurationMonths()% + \" Monate \" + %myplayer.getDurationDays()% + \" Tage \" +\n" + //
                "                    %myplayer.getDurationHours()% + \" Stunden \" + %myplayer.getDurationMinutes()% + \" Minuten \" +\n" + //
                "                    %myplayer.getDurationSeconds()% + \" Sekunden \" ");

        config.addDefault("messages.permshowgroup", "&b\"Du bist in der Gruppe:\" + %afterGroup% + \"[\" + %afterPrefix% + \"]\"");

        config.addDefault("messages.onlyplayercommand", "&b\"Dieser Befehl kann nur von Spielern verwendet werden.\"");

        config.addDefault("messages.adderror", "&bVerwendung: /add <Spielername> <%Gruppenname%> <%Prefix%> <%DurationMonths%> <%DurationDays%> <%DurationHours%> <%DurationMinutes%> <%DurationSeconds%>\"");

        config.addDefault("messages.add", "&b \"Du hast den Spieler \" + %targetPlayerName% + \" zur Gruppe \" + \"[\" + %groupPrefix% + \"]\" + %groupName%  + \" hinzugefügt!\"");

        config.addDefault("messages.addonlineerror", "&b\"Fehler beim Hinzufügen des Spielers zur Gruppe. Überprüfe den Spielernamen oder ob der Spieler online ist.\"");
        
        config.addDefault("messages.groupnamealreadyused", "&b\"Gruppenname schon vergeben. Wähle bitte einen anderen\"");

        // Speichere die Konfiguration, um sicherzustellen, dass alle Schlüssel vorhanden sind
        saveConfig();
        loadConfig();

        // Erhalte eine ConfigurationSection für den Nachrichtenabschnitt
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");

        // Überprüfe, ob die ConfigurationSection vorhanden ist
        if (messagesSection != null) {
            // Hole alle Schlüssel in der Nachrichtenabschnitt
            Set<String> messageKeys = messagesSection.getKeys(false);

            // Iteriere durch die Schlüssel und tue etwas mit ihnen
            for (String key : messageKeys) {
                String message = messagesSection.getString(key);
                getLogger().info("Schlüssel: " + key + ", Nachricht: " + message);
            }
        }
        loadConfig();

        getCommand("mycommand").setExecutor(new Commands()); //für die Commands
        //DefaultGruppe erstellen 
        createAndSaveDefaultGroup(); // damit am Anfang die DefaultGruppe in der Datenbank gespeichert ist
        // PlayerJoinListener registrieren
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this); // Event wenn Spieler zum ersten Mal den Server joinen
        // Event-Handler registrieren
        getServer().getPluginManager().registerEvents(new MyEventHandler(), this); //Serverjoinen
        // Event-Listener registrieren
        getServer().getPluginManager().registerEvents(new ChatListener(), this); // damit das GruppenPrefix vor dem Namen steht 
        // Event-Listener für Schild-Klick-Ereignisse registrieren
        getServer().getPluginManager().registerEvents(new SignClickListener(), this); // für das aktualisieren von Schildern
        // Zeitgesteuerten Task für die Schildaktualisierung alle 300 Ticks starten
        new SignUpdateTask().runTaskTimer(this, 0, 300);
        // Zeitüberprüfungs-Aufgabe starten 
        
        for (Player player : Bukkit.getOnlinePlayers()) {
                                        //veraltete Version
            getServer().getScheduler().runTaskTimer(this, new MyTimeCheckTask(player), 0, 12000); // Gruppenzugehörigkeit der Spieler prüfen 
        }
        
        
        
    }

    private void createConfig() { //KonfigurationsDatei erstellen 
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }

    private void loadConfig() { // Konfigurationsdatei laden
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void createAndSaveDefaultGroup() { //Standardgruppe in der Datenbank speichern wenn das Plugin gespeichert wird 
        
        String defaultGroupName = "DefaultGroup";
        String defaultGroupPrefix = "[Def]";
        
        // Gruppe erstellen und in der Datenbank speichern
        GruppenDAO.erstelleGruppe(defaultGroupName, defaultGroupPrefix);

        
        getLogger().info("Die Standardgruppe wurde erstellt und in der Datenbank gespeichert.");
    }


}






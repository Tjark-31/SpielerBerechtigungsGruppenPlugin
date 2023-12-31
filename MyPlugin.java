package com.example;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;




public class MyPlugin extends JavaPlugin {


    @Override 
    @SuppressWarnings("deprecation")
    public void onEnable() {
      

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

    private void createAndSaveDefaultGroup() { //Standardgruppe in der Datenbank speichern wenn das Plugin gespeichert wird 
        
        String defaultGroupName = "DefaultGroup";
        String defaultGroupPrefix = "[Def]";
        
        // Gruppe erstellen und in der Datenbank speichern
        GruppenDAO.erstelleGruppe(defaultGroupName, defaultGroupPrefix);

        
        getLogger().info(Config.getStandardGroupMessage());
    }


}






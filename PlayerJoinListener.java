package com.example;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener { //Event zur Playerobjekterstellung wenn ein Spieler das erste mal dem Server joint

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        
        UUID playerID = event.getPlayer().getUniqueId();

        // Spielerinformationen aus der Datenbank abrufen
        // nur wenn derSpieler noch nicht in der Datenbank ist 
        if( PlayerDAO.findPlayer( playerID ) == false ){
            MyPlayer player = PlayerDAO.getPlayerFromDatabase(playerID);
            player.saveToDatabase(player);
        }
    }
}


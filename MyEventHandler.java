package com.example;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class MyEventHandler implements Listener { // Begrüßung des Spielers beim Serverjoinen 
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MyPlayer myplayer = PlayerDAO.getcurrentPlayerFromDatabase(player.getUniqueId()); //Spieler beim im chatschreiben immer online

        // Spielername erhalten
        String playerName = myplayer.getName();
        String groupPrefix = myplayer.getprefix();

     
        player.sendMessage(Config.getWelcome(groupPrefix, playerName));
 
    }
}

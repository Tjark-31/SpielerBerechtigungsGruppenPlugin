package com.example;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        MyPlayer player = PlayerDAO.getcurrentPlayerFromDatabase(event.getPlayer().getUniqueId()); //Spieler beim im chatschreiben immer online
        String groupPrefix = player.getprefix(); 
        String chatFormat = "[" + groupPrefix + "] %s: %s"; //Gruppenprefix vor dem Namen 
        event.setFormat(chatFormat);
    }
}

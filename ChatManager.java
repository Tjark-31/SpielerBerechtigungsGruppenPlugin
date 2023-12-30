package com.example;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatManager {

    public static void displayChatMessage(Player player, String message) {
        String formattedMessage = formatChatMessage(player, message);
        // Hier wird die formatierte Nachricht im Chat angezeigt.
        player.sendMessage(formattedMessage);  // Nachricht an Spieler mit Gruppenprefix davor 
    }

    private static String formatChatMessage(Player player, String message) {
        //  Nachrichtenformat anpassen
        MyPlayer myplayer = PlayerDAO.getcurrentPlayerFromDatabase(player.getUniqueId()); // Spieler muss online sein für Befehle
        return ChatColor.GRAY + "[" + myplayer.getFormattedPrefix() + ChatColor.GRAY + "] " + myplayer.getName() + ": " + message;
    }
}
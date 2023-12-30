package com.example;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

// Schilder verwalten 
@SuppressWarnings("deprecation")
public class SignManager {
    public static void updatePlayerInfoSign(Sign sign, Player closestPlayer) {
        String playerName = closestPlayer.getName();
        MyPlayer myclosestPlayer = PlayerDAO.getcurrentPlayerFromDatabase(closestPlayer.getUniqueId());
        String playerGroup = myclosestPlayer.getGroupname(); // Hier die Methode implementieren, um den Rang des Spielers zu erhalten

        // Das Schild mit Spielerinformationen aktualisieren
        //veraltete Version
        sign.setLine(0, "Spieler:");
        sign.setLine(1, playerName);
        sign.setLine(2, "Gruppe:");
        sign.setLine(3, playerGroup);
        sign.update();
    }

}

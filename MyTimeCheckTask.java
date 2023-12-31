package com.example;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

// um Gruppenzugehörigkeiten regelmäßig zu prüfen und zu aktualisieren 
class MyTimeCheckTask extends BukkitRunnable {

    private final Player player;
    private final MyPlayer myplayer;


    public MyTimeCheckTask(Player player) {
        this.player = player; // um dem Spieler Nachrichten senden zu können 
        this.myplayer = PlayerDAO.getcurrentPlayerFromDatabase(player.getUniqueId()); // um mit dem Spielerobjet arbeiten zu können 

    }

    @Override
    public void run() {
        
        String currentGroup = myplayer.getGroupname();
        String currentPrefix = myplayer.getprefix();

        int w = myplayer.checkassignment();

        String afterGroup = myplayer.getGroupname();
        String afterPrefix = myplayer.getprefix();

        myplayer.saveToDatabase(myplayer);

        // nur ein Fall da der Spieler bei abgelaufener Zeit benachrichtig werden soll, sonst soll er mit /showgroup selbst nachschauen können welcher Gruppe er angehört und wie lange 
        if (w == 1) {
            ChatManager.displayChatMessage(player, Config.getExpiredShowGroupTime( currentPrefix, currentGroup, afterPrefix,  afterGroup));
        }
    }
}

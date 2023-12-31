package com.example;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.UUID;

public class Commands implements CommandExecutor {

    // Für die Verwaltung von Gruppen
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Config.getOnlyPlayerCommandError());
            return false;
        }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("showgroup")) {
            showGroupCommand(player, args);
        } else if (cmd.getName().equalsIgnoreCase("creategroup")) {
            createGroupCommand(player, args);
        } else if (cmd.getName().equalsIgnoreCase("joingroup")) {
            joinGroupCommand(player, args);
        } else if (cmd.getName().equalsIgnoreCase("add")){
            add(player, args);
        }

        return true;
    }

    private void showGroupCommand(Player player, String[] args) { // Anzeigen welcher Gruppe man selbst angehört 
        if (args.length != 0) {
            player.sendMessage("Verwendung: /showgroup ");
            return;
        }
        MyPlayer myplayer = PlayerDAO.getcurrentPlayerFromDatabase(player.getUniqueId()); // Spieler muss online sein für Befehle
        String currentGroup = myplayer.getGroupname();
        String currentPrefix = myplayer.getprefix();

        int w = myplayer.checkassignment();

        String afterGroup = myplayer.getGroupname();
        String afterPrefix = myplayer.getprefix();

        myplayer.saveToDatabase(myplayer);

        if (w == 1) {
            ChatManager.displayChatMessage(player, Config.getExpiredShowGroupTime( currentPrefix, currentGroup, afterPrefix, afterGroup) );  // Gruppenzeit ist abgelaufen
        } else if (w == 2) {
            ChatManager.displayChatMessage(player, Config.getShowGroupTime( afterPrefix, afterGroup, myplayer.getDurationMonths(), myplayer.getDurationDays() , myplayer.getDurationHours(), myplayer.getDurationMinutes() , myplayer.getDurationSeconds()) ); // Gruppenzeit noch nicht abgelaufen
        } else if (w == 3) {
            ChatManager.displayChatMessage(player, Config.getPermShowGroup(afterPrefix, afterGroup)); // gehört permanent einer Gruppe an
        } else {
            ChatManager.displayChatMessage(player, Config.getGruppenZuweisungsfehler() );
        }
    }

    private void createGroupCommand(Player player, String[] args) { //Gruppe erstellen
        if (args.length != 2) {
            player.sendMessage(Config.getCreateGroupError());
            return;
        }


        String groupName = args[0];
        String groupPrefix = args[1];

        if(!GruppenDAO.findGroup(groupName)){

            GruppenDAO.erstelleGruppe(groupName, groupPrefix);

            ChatManager.displayChatMessage(player, Config.getCreateGroup(groupName));
        }
        else{
            ChatManager.displayChatMessage(player, Config.getGroupNameAlreadyUsedError());
        }
        
    }

    private void joinGroupCommand(Player player, String[] args) { //Gruppe beitreten 
        if (args.length > 7) {
            player.sendMessage(Config.getJoinGroupError());
            return;
        }
        MyPlayer myplayer = PlayerDAO.getcurrentPlayerFromDatabase(player.getUniqueId()); // Spieler muss online sein für Befehle
        String groupName = args[0];
        String groupPrefix = args[1];
        LocalDateTime currentTime = LocalDateTime.now();
        myplayer.setTime(Long.parseLong(args[2]), Long.parseLong(args[3]), Long.parseLong(args[4]), Long.parseLong(args[5]), Long.parseLong(args[6]));
        myplayer.setAssignmentTime(currentTime);

        if (GruppenDAO.findGroup(groupName)) {
            GruppenDAO.trittGruppeBei(myplayer, groupName, groupPrefix);
            myplayer.joinGroup(groupName, myplayer.getAssignmenetTime(), myplayer.getDurationMonths(), myplayer.getDurationDays(), myplayer.getDurationHours(), myplayer.getDurationMinutes(), myplayer.getDurationSeconds());

            myplayer.saveToDatabase(myplayer);
            
            ChatManager.displayChatMessage(player, Config.getJoinGroup(groupPrefix, groupName));
        } else {
            player.sendMessage(Config.getCheckGroupNameError());
        }
    }

    
    private void add(Player player, String[] args) { // anderen Spieler einer Gruppe hinzufügen
        if (args.length > 8) {
            player.sendMessage(Config.getAddError());
            return;
        }

        String targetPlayerName = args[0];
        String groupName = args[1];
        String groupPrefix = args[2];
        UUID playerID = PlayerDAO.getPlayerIDFromDatabase(targetPlayerName);
        Player addPlayer = Bukkit.getPlayer(playerID);
        MyPlayer targetPlayer = PlayerDAO.getcurrentPlayerFromDatabase(playerID); // Spieler muss online sein für Befehle
        
        
       // Zeitinfos parsen
        targetPlayer.setTime(Long.parseLong(args[3]), Long.parseLong(args[4]), Long.parseLong(args[5]), Long.parseLong(args[6]), Long.parseLong(args[7]));

        if (addPlayer.isOnline() ) {
            GruppenDAO.addPlayertoGroup(targetPlayer, groupName, groupPrefix);
            LocalDateTime currentTime = LocalDateTime.now();
            targetPlayer.setGroupname(groupName, currentTime );
            targetPlayer.saveToDatabase(targetPlayer);
            targetPlayer.updateInDatabase(targetPlayer);

            ChatManager.displayChatMessage(player, Config.getAdd(targetPlayerName, groupPrefix, groupName));
            ChatManager.displayChatMessage(addPlayer, Config.getAddPlayerMessage( groupPrefix, groupName));
        } else {
            player.sendMessage(Config.getAddOnlineError());
        }
    }

}


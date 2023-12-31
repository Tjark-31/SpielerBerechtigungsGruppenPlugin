package com.example;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Config {

    private static FileConfiguration config;

    private Config() {
        loadConfiguration();
    }

    public static Config getInstance() {
        return ConfigHolder.INSTANCE;
    }

    private static class ConfigHolder {
        private static final Config INSTANCE = new Config();
    }

    private static void loadConfiguration() {
        File configFile = new File("/Users/tjark/sample-app/Legend/myproject/src/main/java/com/example/config.yml");
        config = new YamlConfiguration();

        try {
            config.load(configFile);
        } catch (FileNotFoundException e) {
            // Die Datei wurde nicht gefunden
            e.printStackTrace();
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            // Andere IOExceptions oder ungültige Konfiguration
            e.printStackTrace();
        }
    }

    public static String getWelcome(String groupPrefix, String playerName) {
        return config.getString("messages.welcome")
                .replace("%groupPrefix%", groupPrefix)
                .replace("%playerName%", playerName);
    }

    public String getGoodbye(String groupPrefix, String playerName) {
        return config.getString("messages.goodbye")
                .replace("%groupPrefix%", groupPrefix)
                .replace("%playerName%", playerName);
    }
    
    
    public static String getGruppenZuweisungsfehler() {
        return config.getString("messages.gruppenZuweisungsfehler");
    }
    
    public static String getDatabaseError() {
        return config.getString("messages.databaseerror");
    }

    public static String getNoDatabaseConnectionMessage() {
        return config.getString("messages.nodatabaseconnection");
    }

    public static String getPlayerIDDatabaseErrorMessage() {
        return config.getString("messages.playerIDdatabaseerror");
    }
    
    
    
    public static String getJoinGroupError() {
        return config.getString("messages.joingrouperror");
    }
    
    public static String getJoinGroup(String groupPrefix, String groupName) {
        return config.getString("messages.joingroup")
                .replace("%groupPrefix%", groupPrefix)
                .replace("%groupName%", groupName);
    }
    
    public static String getCheckGroupNameError() {
        return config.getString("messages.checkgroupname");
    }
    
    public static String getCreateGroupError() {
        return config.getString("messages.creategrouperror");
    }
    
    public static String getCreateGroup(String groupName) {
        return config.getString("messages.creategroup")
                .replace("%groupName%", groupName);
    }
    
    public String getShowGroupError() {
        return config.getString("messages.showgrouperror");
    }
    
    public static String getExpiredShowGroupTime(String currentPrefix, String currentGroup, String afterPrefix, String afterGroup) {
        return config.getString("messages.expiredshowgrouptime")
                .replace("%currentPrefix%", currentPrefix)
                .replace("%currentGroup%", currentGroup)
                .replace("[%afterPrefix%]", afterPrefix)
                .replace("%afterGroup%", afterGroup);
    }
    
    public static String getShowGroupTime(String afterPrefix, String afterGroup, long durationMonths, long durationDays, long durationHours, long durationMinutes, long durationSeconds) {
        return config.getString("messages.showgrouptime")
                .replace("%afterPrefix%", afterPrefix)
                .replace("%afterGroup%", afterGroup)
                .replace("%myplayer.getDurationMonths%", String.valueOf(durationMonths))
                .replace("%myplayer.getDurationDays%", String.valueOf(durationDays))
                .replace("%myplayer.getDurationHours%", String.valueOf(durationHours))
                .replace("%myplayer.getDurationMinutes%", String.valueOf(durationMinutes))
                .replace("%myplayer.getDurationSeconds%", String.valueOf(durationSeconds));
    }
    
    public static String getPermShowGroup(String afterPrefix, String afterGroup) {
        return config.getString("messages.permshowgroup")
                .replace("%afterPrefix%", afterPrefix)
                .replace("%afterGroup%", afterGroup);
    }
    
    public static String getOnlyPlayerCommandError() {
        return config.getString("messages.onlyplayercommand");
    }
    
    public static String getAddError() {
        return config.getString("messages.adderror");
    }
    
    public static String getAdd(String targetPlayerName, String groupPrefix, String groupName) {
        return config.getString("messages.add")
                .replace("%targetPlayerName%", targetPlayerName)
                .replace("%groupPrefix%", groupPrefix)
                .replace("%groupName%", groupName);
    }
    
    public static String getAddOnlineError() {
        return config.getString("messages.addonlineerror");
    }
    
    public static String getGroupNameAlreadyUsedError() {
        return config.getString("messages.groupnamealreadyused");
    }
    
    public static String getAddPlayerMessage(String groupPrefix, String groupName) {
        return config.getString("messages.addplayer")
                .replace("%groupPrefix%", groupPrefix)
                .replace("%groupName%", groupName);
    }
    
    public static String getStandardGroupMessage() {
        return config.getString("messages.standardgroup");
    }
    
}

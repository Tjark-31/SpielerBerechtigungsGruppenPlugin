package com.example;




import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

// Schilder regelmäßig aktualisieren 
public class SignUpdateTask extends BukkitRunnable {

    @Override
    public void run() {
        // Hier alle Schilder im Spiel durchgehen und aktualisieren
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (BlockState blockState : chunk.getTileEntities()) {
                    if (blockState instanceof Sign) {
                        Sign sign = (Sign) blockState;
                        Player closestPlayer = findClosestPlayer(sign.getLocation());
                        if (closestPlayer != null) {
                            SignManager.updatePlayerInfoSign(sign, closestPlayer);
                        }
                    }
                }
            }
        }
    }

    //um Schilder InGame zuordenen zu können 
    private Player findClosestPlayer(Location signLocation) {
        double closestDistanceSquared = Double.MAX_VALUE;
        Player closestPlayer = null;
    
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            org.bukkit.Location playerLocation = onlinePlayer.getLocation();
            double distanceSquared = signLocation.distanceSquared(playerLocation);
    
            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared;
                closestPlayer = onlinePlayer;
            }
        }
    
        return closestPlayer;
    }
    
}
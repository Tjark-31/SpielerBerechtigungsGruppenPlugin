package com.example;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

// Schilder aktualisiern wenn sie von Spielern angeklickt werden 
public class SignClickListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                updateSignInformation(sign, event.getPlayer());
            }
        }
    }

    private void updateSignInformation(Sign sign, Player player) {
        SignManager.updatePlayerInfoSign(sign, player);
    }
}

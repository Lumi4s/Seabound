package io.github.seabound.listeners;

import io.github.seabound.player.PlayerManager;
import io.github.seabound.service.SeaboundService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;


public final class PlayerListener implements Listener {
    private final PlayerManager playerManager;
    private final SeaboundService seaboundService;

    public PlayerListener(PlayerManager playerManager, SeaboundService seaboundService) {
        this.playerManager = playerManager;
        this.seaboundService = seaboundService;
    }

    @EventHandler
    public void onAirChange(EntityAirChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!playerManager.contains(player.getUniqueId())) {
            return;
        }
    }
}
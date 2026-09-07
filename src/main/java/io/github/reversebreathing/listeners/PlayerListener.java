package io.github.reversebreathing.listeners;

import io.github.reversebreathing.player.PlayerManager;
import io.github.reversebreathing.service.ReverseBreathingService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;


public final class PlayerListener implements Listener {
    private final PlayerManager playerManager;
    private final ReverseBreathingService reverseBreathingService;

    public PlayerListener(PlayerManager playerManager, ReverseBreathingService reverseBreathingService) {
        this.playerManager = playerManager;
        this.reverseBreathingService = reverseBreathingService;
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
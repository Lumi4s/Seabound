package io.github.seabound.listener;

import io.github.seabound.player.PlayerManager;
import io.github.seabound.service.SeaboundService;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public final class PlayerListener implements Listener {

    private final PlayerManager playerManager;
    private final SeaboundService seaboundService;

    public PlayerListener(PlayerManager playerManager, SeaboundService seaboundService) {
        this.playerManager = playerManager;
        this.seaboundService = seaboundService;
    }

    @EventHandler(ignoreCancelled = true)
    public void onAirChange(EntityAirChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!playerManager.contains(player.getUniqueId())) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (seaboundService.canBreathe(player)) {
            // Player can breathe (underwater, in rain, or under potion effects).
            // Prevent vanilla underwater mechanics from decreasing air.
            if (event.getAmount() < player.getRemainingAir()) {
                event.setCancelled(true);
                if (player.getRemainingAir() < player.getMaximumAir()) {
                    player.setRemainingAir(Math.min(player.getRemainingAir() + 4, player.getMaximumAir()));
                }
            }
        } else {
            // Player is on land and cannot breathe.
            // Prevent vanilla from automatically replenishing air.
            if (event.getAmount() > player.getRemainingAir()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        seaboundService.cleanup(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        seaboundService.cleanup(event.getPlayer().getUniqueId());
    }
}

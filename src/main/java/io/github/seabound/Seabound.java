package io.github.seabound;

import io.github.seabound.command.SeaboundCommand;
import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.listener.PlayerListener;
import io.github.seabound.player.PlayerManager;
import io.github.seabound.service.MovementService;
import io.github.seabound.service.SeaboundService;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public final class Seabound extends JavaPlugin {

    private PlayerManager playerManager;
    private SeaboundService seaboundService;
    private SeaboundConfig seaboundConfig;
    private MovementService movementService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        seaboundConfig = new SeaboundConfig(this);
        seaboundConfig.load();

        playerManager = new PlayerManager(this);
        playerManager.load();

        seaboundService = new SeaboundService();
        movementService = new MovementService(seaboundConfig);

        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager, seaboundService), this);

        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(
                        SeaboundCommand.create(playerManager, seaboundConfig)
                )
        );

        getServer().getScheduler().runTaskTimer(
                this,
                () -> {
                    for (UUID uuid : playerManager.getPlayers()) {
                        Player player = Bukkit.getPlayer(uuid);

                        if (player != null && player.isOnline()) {
                            seaboundService.tick(player);
                            movementService.tick(player);
                        }
                    }
                },
                0L,
                1L
        );
    }

    @Override
    public void onDisable() {
        if (playerManager != null) {
            for (UUID uuid : playerManager.getPlayers()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
                        player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                    }
                }
            }
        }
    }

    public SeaboundConfig getSeaboundConfig() {
        return seaboundConfig;
    }

    public MovementService getMovementService() {
        return movementService;
    }
}

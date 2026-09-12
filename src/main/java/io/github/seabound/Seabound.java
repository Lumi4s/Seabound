package io.github.seabound;

import io.github.seabound.command.SeaboundCommand;
import io.github.seabound.command.VisionCommand;
import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.listener.PlayerListener;
import io.github.seabound.player.PlayerManager;
import io.github.seabound.service.MovementService;
import io.github.seabound.service.SeaboundService;
import io.github.seabound.service.VisionService;
import io.papermc.paper.command.brigadier.Commands;
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
    private VisionService visionService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        seaboundConfig = new SeaboundConfig(this);
        seaboundConfig.load();

        playerManager = new PlayerManager(this);
        playerManager.load();

        seaboundService = new SeaboundService();
        movementService = new MovementService(seaboundConfig);
        visionService = new VisionService(seaboundConfig, playerManager);

        getServer().getPluginManager().registerEvents(
                new PlayerListener(playerManager, seaboundService, seaboundConfig, visionService),
                this
        );

        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> {
                    event.registrar().register(
                            SeaboundCommand.create(playerManager, seaboundConfig)
                    );
                    event.registrar().register(
                            Commands.literal("seavision")
                                    .executes(context -> VisionCommand.execute(
                                            context.getSource().getSender(),
                                            playerManager,
                                            seaboundConfig
                                    ))
                                    .build()
                    );
                }
        );

        getServer().getScheduler().runTaskTimer(
                this,
                () -> {
                    for (UUID uuid : playerManager.getPlayers()) {
                        Player player = Bukkit.getPlayer(uuid);

                        if (player != null && player.isOnline()) {
                            seaboundService.tick(player);
                            movementService.tick(player);
                            visionService.tick(player);
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
                    if (visionService != null) {
                        visionService.removeVisionEffect(player);
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

    public VisionService getVisionService() {
        return visionService;
    }
}

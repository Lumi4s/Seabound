package io.github.reversebreathing;

import io.github.reversebreathing.command.ReverseBreathingCommand;
import io.github.reversebreathing.listeners.PlayerListener;
import io.github.reversebreathing.player.PlayerManager;
import io.github.reversebreathing.service.ReverseBreathingService;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class ReverseBreathing extends JavaPlugin {

    private PlayerManager playerManager;
    private ReverseBreathingService reverseBreathingService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        playerManager = new PlayerManager(this);
        playerManager.load();
        reverseBreathingService = new ReverseBreathingService();

        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager, reverseBreathingService), this);

        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(
                        ReverseBreathingCommand.create(playerManager)
                )
        );

        getServer().getScheduler().runTaskTimer(
                this,
                () -> {
                    for (UUID uuid : playerManager.getPlayers()) {
                        Player player = Bukkit.getPlayer(uuid);

                        if (player != null) {
                            reverseBreathingService.tick(player);
                        }
                    }
                },
                0L,
                1L
        );

    }

    @Override
    public void onDisable() {
    }
}

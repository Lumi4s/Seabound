package io.github.seabound;

import io.github.seabound.command.SeaboundCommand;
import io.github.seabound.listener.PlayerListener;
import io.github.seabound.player.PlayerManager;
import io.github.seabound.service.SeaboundService;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Seabound extends JavaPlugin {

    private PlayerManager playerManager;
    private SeaboundService seaboundService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        playerManager = new PlayerManager(this);
        playerManager.load();
        seaboundService = new SeaboundService();

        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager, seaboundService), this);

        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(
                        SeaboundCommand.create(playerManager)
                )
        );

        getServer().getScheduler().runTaskTimer(
                this,
                () -> {
                    for (UUID uuid : playerManager.getPlayers()) {
                        Player player = Bukkit.getPlayer(uuid);

                        if (player != null && player.isOnline()) {
                            seaboundService.tick(player);
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

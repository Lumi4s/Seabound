package io.github.reversebreathing;

import io.github.reversebreathing.command.ReverseBreathingCommand;
import io.github.reversebreathing.listeners.PlayerListener;
import io.github.reversebreathing.player.PlayerManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class ReverseBreathing extends JavaPlugin {

    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        playerManager = new PlayerManager(this);
        playerManager.load();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(
                        ReverseBreathingCommand.create(playerManager)
                )
        );

    }

    @Override
    public void onDisable() {
    }
}

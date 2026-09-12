package io.github.seabound.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VisionCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(PlayerManager playerManager, SeaboundConfig config) {
        return Commands.literal("vision")
                .executes(context -> execute(context.getSource().getSender(), playerManager, config));
    }

    public static int execute(CommandSender sender, PlayerManager playerManager, SeaboundConfig config) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players.", NamedTextColor.RED));
            return 0;
        }

        if (!playerManager.contains(player.getUniqueId())) {
            sender.sendMessage(Component.text("You are not registered as a Seabound player.", NamedTextColor.RED));
            return 0;
        }

        if (!config.isVisionEnabled()) {
            sender.sendMessage(Component.text("Underwater vision is currently disabled by administrator.", NamedTextColor.RED));
            return 0;
        }

        boolean newState = playerManager.toggleVision(player.getUniqueId(), config.isVisionDefaultEnabled());

        Component message = Component.text("Underwater vision is now ", NamedTextColor.AQUA)
                .append(Component.text(newState ? "ENABLED" : "DISABLED", newState ? NamedTextColor.GREEN : NamedTextColor.RED));

        player.sendMessage(message);
        player.playSound(
                player.getLocation(),
                newState ? Sound.BLOCK_CONDUIT_ACTIVATE : Sound.BLOCK_CONDUIT_DEACTIVATE,
                0.6f,
                1.2f
        );

        return Command.SINGLE_SUCCESS;
    }
}

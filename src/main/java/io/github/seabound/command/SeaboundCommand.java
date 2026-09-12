package io.github.seabound.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class SeaboundCommand {

    public static LiteralCommandNode<CommandSourceStack> create(PlayerManager playerManager, SeaboundConfig config) {
        return Commands.literal("seabound")
                .executes(context -> {
                    var sender = context.getSource().getSender();
                    if (sender.isOp() || sender.hasPermission("seabound.admin")) {
                        InfoCommand.sendInfo(sender, config);
                    } else if (sender instanceof Player player && playerManager.contains(player.getUniqueId())) {
                        VisionCommand.execute(sender, playerManager, config);
                    } else {
                        sender.sendMessage(Component.text("Seabound - Aquatic survival plugin.", NamedTextColor.AQUA));
                    }
                    return Command.SINGLE_SUCCESS;
                })
                .then(VisionCommand.create(playerManager, config))
                .then(AddCommand.create(playerManager))
                .then(RemoveCommand.create(playerManager))
                .then(ListCommand.create(playerManager))
                .then(ConfigCommand.create(config))
                .then(InfoCommand.create(config))
                .build();
    }
}
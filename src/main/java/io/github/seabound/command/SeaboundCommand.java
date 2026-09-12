package io.github.seabound.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class SeaboundCommand {

    public static LiteralCommandNode<CommandSourceStack> create(PlayerManager playerManager, SeaboundConfig config) {
        return Commands.literal("seabound")
                .requires(source -> source.getSender().isOp() || source.getSender().hasPermission("seabound.admin"))
                .then(AddCommand.create(playerManager))
                .then(RemoveCommand.create(playerManager))
                .then(ListCommand.create(playerManager))
                .then(ConfigCommand.create(config))
                .then(InfoCommand.create(config))
                .build();
    }
}
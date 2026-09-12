package io.github.seabound.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.seabound.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class SeaboundCommand {

    public static LiteralCommandNode<CommandSourceStack> create(PlayerManager playerManager) {
        return Commands.literal("seabound")
                .then(AddCommand.create(playerManager))
                .then(RemoveCommand.create(playerManager))
                .then(ListCommand.create(playerManager))
                .build();
    }
}
package io.github.reversebreathing.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.reversebreathing.player.PlayerManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class ReverseBreathingCommand {

    public static LiteralCommandNode<CommandSourceStack> create(PlayerManager playerManager) {
        return Commands.literal("reversebreathing")
                .then(AddCommand.create(playerManager))
                .then(RemoveCommand.create(playerManager))
                .then(ListCommand.create(playerManager))
                .build();
    }
}
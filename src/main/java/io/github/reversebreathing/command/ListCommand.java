package io.github.reversebreathing.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.reversebreathing.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class ListCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            PlayerManager playerManager
    ) {
        return Commands.literal("list")
                .executes(context -> {
                            context.getSource()
                                    .getSender()
                                    .sendPlainMessage(
                                            playerManager.toString()
                                    );

                            return Command.SINGLE_SUCCESS;
                        }
                );
    }
}
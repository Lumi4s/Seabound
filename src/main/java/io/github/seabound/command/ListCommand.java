package io.github.seabound.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.seabound.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ListCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            PlayerManager playerManager
    ) {
        return Commands.literal("list")
                .executes(context -> {
                            if (!context.getSource().getSender().isOp()) {
                                context.getSource()
                                        .getSender()
                                        .sendMessage(Component.text("Only for operators!",
                                                NamedTextColor.RED));
                                return 0;
                            }

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
package io.github.reversebreathing.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.reversebreathing.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

public class AddCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            PlayerManager playerManager
    ) {
        return Commands.literal("add")
                .then(
                        Commands.argument(
                                        "player",
                                        ArgumentTypes.player()
                                )
                                .executes(context -> {

                                    PlayerSelectorArgumentResolver resolver =
                                            context.getArgument(
                                                    "player",
                                                    PlayerSelectorArgumentResolver.class
                                            );

                                    Player player = resolver.resolve(context.getSource()).getFirst();

                                    playerManager.add(player.getUniqueId());

                                    context.getSource()
                                            .getSender()
                                            .sendPlainMessage(
                                                    "Added " + player.getName()
                                            );

                                    return Command.SINGLE_SUCCESS;
                                })
                );
    }
}
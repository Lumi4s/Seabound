package io.github.reversebreathing.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.reversebreathing.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

public class RemoveCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            PlayerManager playerManager
    ) {
        return Commands.literal("remove")
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

                                    playerManager.remove(
                                            player.getUniqueId()
                                    );

                                    context.getSource()
                                            .getSender()
                                            .sendPlainMessage(
                                                    "Removed " + player.getName()
                                            );

                                    return Command.SINGLE_SUCCESS;
                                })
                );
    }
}
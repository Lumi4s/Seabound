package io.github.seabound.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.seabound.player.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
                                    if (!player.isOp()) {
                                        context.getSource()
                                                .getSender()
                                                .sendMessage(Component.text("Only for operators!",
                                                        NamedTextColor.RED));
                                        return 0;
                                    }

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
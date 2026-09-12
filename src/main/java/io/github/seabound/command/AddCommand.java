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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(
            PlayerManager playerManager
    ) {
        return Commands.literal("add")
                .requires(source -> source.getSender().isOp() || source.getSender().hasPermission("seabound.admin"))
                .then(
                        Commands.argument(
                                        "player",
                                        ArgumentTypes.player()
                                )
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();
                                    if (!sender.isOp() && !sender.hasPermission("seabound.admin")) {
                                        sender.sendMessage(Component.text("Only for operators!",
                                                NamedTextColor.RED));
                                        return 0;
                                    }

                                    PlayerSelectorArgumentResolver resolver =
                                            context.getArgument(
                                                    "player",
                                                    PlayerSelectorArgumentResolver.class
                                            );

                                    Player player = resolver.resolve(context.getSource()).getFirst();

                                    if (playerManager.contains(player.getUniqueId())) {
                                        sender.sendMessage(
                                                Component.text(player.getName() + " is already using reverse breathing.",
                                                        NamedTextColor.YELLOW)
                                        );
                                        return 0;
                                    }

                                    playerManager.add(player.getUniqueId());

                                    sender.sendMessage(
                                            Component.text("Added " + player.getName() + " to Seabound.",
                                                    NamedTextColor.GREEN)
                                    );

                                    return Command.SINGLE_SUCCESS;
                                })
                );
    }
}
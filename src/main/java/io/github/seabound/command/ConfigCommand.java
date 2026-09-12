package io.github.seabound.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.seabound.config.SeaboundConfig;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class ConfigCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(SeaboundConfig config) {
        return Commands.literal("config")
                .requires(source -> source.getSender().isOp() || source.getSender().hasPermission("seabound.admin"))
                .executes(context -> {
                    InfoCommand.sendInfo(context.getSource().getSender(), config);
                    return Command.SINGLE_SUCCESS;
                })
                .then(
                        Commands.literal("info")
                                .executes(context -> {
                                    InfoCommand.sendInfo(context.getSource().getSender(), config);
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .then(
                        Commands.literal("reload")
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();
                                    config.load();
                                    sender.sendMessage(
                                            Component.text("Seabound configuration reloaded from config.yml.", NamedTextColor.GREEN)
                                    );
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .then(
                        Commands.literal("reset")
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();
                                    config.reset();
                                    sender.sendMessage(
                                            Component.text("Seabound configuration reset to default values.", NamedTextColor.GREEN)
                                    );
                                    return Command.SINGLE_SUCCESS;
                                })
                )
                .then(
                        Commands.literal("get")
                                .then(
                                        Commands.argument("key", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    for (String param : SeaboundConfig.PARAMETER_KEYS) {
                                                        if (param.toLowerCase().startsWith(builder.getRemainingLowerCase())) {
                                                            builder.suggest(param);
                                                        }
                                                    }
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    CommandSender sender = context.getSource().getSender();
                                                    String key = StringArgumentType.getString(context, "key");
                                                    String value = config.getValue(key);

                                                    if (value == null) {
                                                        sender.sendMessage(
                                                                Component.text("Unknown parameter: " + key + ". Use /seabound config info for available parameters.",
                                                                        NamedTextColor.RED)
                                                        );
                                                        return 0;
                                                    }

                                                    sender.sendMessage(
                                                            Component.text("Parameter ", NamedTextColor.GRAY)
                                                                    .append(Component.text(config.canonicalKey(key), NamedTextColor.YELLOW))
                                                                    .append(Component.text(" = ", NamedTextColor.GRAY))
                                                                    .append(Component.text(value, NamedTextColor.GREEN))
                                                    );
                                                    return Command.SINGLE_SUCCESS;
                                                })
                                )
                )
                .then(
                        Commands.literal("set")
                                .then(
                                        Commands.argument("key", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    for (String param : SeaboundConfig.PARAMETER_KEYS) {
                                                        if (param.toLowerCase().startsWith(builder.getRemainingLowerCase())) {
                                                            builder.suggest(param);
                                                        }
                                                    }
                                                    return builder.buildFuture();
                                                })
                                                .then(
                                                        Commands.argument("value", StringArgumentType.word())
                                                                .suggests((context, builder) -> {
                                                                    String key = StringArgumentType.getString(context, "key");
                                                                    String canonical = config.canonicalKey(key);
                                                                    if (canonical != null) {
                                                                        if (canonical.endsWith(".enabled")) {
                                                                            builder.suggest("true");
                                                                            builder.suggest("false");
                                                                        } else if (canonical.contains("speed")) {
                                                                            builder.suggest("1.15");
                                                                            builder.suggest("1.25");
                                                                            builder.suggest("1.40");
                                                                            builder.suggest("1.50");
                                                                        }
                                                                    }
                                                                    return builder.buildFuture();
                                                                })
                                                                .executes(context -> {
                                                                    CommandSender sender = context.getSource().getSender();
                                                                    String key = StringArgumentType.getString(context, "key");
                                                                    String value = StringArgumentType.getString(context, "value");

                                                                    try {
                                                                        config.setValue(key, value);
                                                                        sender.sendMessage(
                                                                                Component.text("Successfully set ", NamedTextColor.GREEN)
                                                                                        .append(Component.text(config.canonicalKey(key), NamedTextColor.YELLOW))
                                                                                        .append(Component.text(" to ", NamedTextColor.GREEN))
                                                                                        .append(Component.text(config.getValue(key), NamedTextColor.AQUA))
                                                                                        .append(Component.text(" (saved to config.yml)", NamedTextColor.GRAY))
                                                                        );
                                                                        return Command.SINGLE_SUCCESS;
                                                                    } catch (IllegalArgumentException e) {
                                                                        sender.sendMessage(
                                                                                Component.text("Error: " + e.getMessage(), NamedTextColor.RED)
                                                                        );
                                                                        return 0;
                                                                    }
                                                                })
                                                )
                                )
                );
    }
}

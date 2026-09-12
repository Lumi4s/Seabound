package io.github.seabound.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.seabound.config.SeaboundConfig;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;

public class InfoCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create(SeaboundConfig config) {
        return Commands.literal("info")
                .requires(source -> source.getSender().isOp() || source.getSender().hasPermission("seabound.admin"))
                .executes(context -> {
                    sendInfo(context.getSource().getSender(), config);
                    return Command.SINGLE_SUCCESS;
                });
    }

    public static void sendInfo(CommandSender sender, SeaboundConfig config) {
        sender.sendMessage(Component.empty());
        sender.sendMessage(
                Component.text("====== [ Seabound ] ======", NamedTextColor.AQUA, TextDecoration.BOLD)
        );

        sender.sendMessage(
                Component.text("Fast Swimming:", NamedTextColor.GOLD, TextDecoration.BOLD)
        );
        sender.sendMessage(
                Component.text("  • ", NamedTextColor.DARK_GRAY)
                        .append(Component.text("fast-swimming.enabled", NamedTextColor.YELLOW))
                        .append(Component.text(": Current: ", NamedTextColor.GRAY))
                        .append(Component.text(config.isFastSwimmingEnabled(), config.isFastSwimmingEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED))
                        .append(Component.text(" (default: true)", NamedTextColor.DARK_AQUA))
        );
        sender.sendMessage(
                Component.text("  • ", NamedTextColor.DARK_GRAY)
                        .append(Component.text("fast-swimming.speed-multiplier", NamedTextColor.YELLOW))
                        .append(Component.text(": Current: ", NamedTextColor.GRAY))
                        .append(Component.text(String.format("%.2f", config.getFastSwimmingMultiplier()), NamedTextColor.GREEN))
                        .append(Component.text(" (default: 1.25)", NamedTextColor.DARK_AQUA))
        );
        sender.sendMessage(
                Component.text("    Recommended: ", NamedTextColor.GRAY)
                        .append(Component.text("1.15 - 1.50", NamedTextColor.WHITE))
                        .append(Component.text(" (max: 2.50)", NamedTextColor.DARK_GRAY))
        );
        sender.sendMessage(
                Component.text("    Advice: ", NamedTextColor.BLUE)
                        .append(Component.text("1.25 provides natural and smooth acceleration (like a dolphin's).", NamedTextColor.GRAY))
        );

        sender.sendMessage(Component.empty());

        sender.sendMessage(
                Component.text("Quick setup commands:", NamedTextColor.GOLD)
        );
        sender.sendMessage(
                Component.text("  /seabound config set <parameter> <value>", NamedTextColor.YELLOW)
                        .hoverEvent(HoverEvent.showText(Component.text("Click to insert the command into the chat")))
                        .clickEvent(ClickEvent.suggestCommand("/seabound config set "))
        );
        sender.sendMessage(
                Component.text("  /seabound config reset", NamedTextColor.YELLOW)
                        .append(Component.text(" - reset everything to default values", NamedTextColor.GRAY))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to reset settings")))
                        .clickEvent(ClickEvent.suggestCommand("/seabound config reset"))
        );
        sender.sendMessage(
                Component.text("  /seabound config reload", NamedTextColor.YELLOW)
                        .append(Component.text(" - reload config.yml from disk", NamedTextColor.GRAY))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to reload the config")))
                        .clickEvent(ClickEvent.suggestCommand("/seabound config reload"))
        );
        sender.sendMessage(
                Component.text("==================================================", NamedTextColor.AQUA)
        );
    }
}

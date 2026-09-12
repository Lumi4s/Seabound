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
                Component.text("====== [ Seabound: Справка и рекомендации ] ======", NamedTextColor.AQUA, TextDecoration.BOLD)
        );

        // Land sliding section
        sender.sendMessage(
                Component.text("1. Скольжение по суше (Land Sliding):", NamedTextColor.GOLD, TextDecoration.BOLD)
        );
        sender.sendMessage(
                Component.text("  • ", NamedTextColor.DARK_GRAY)
                        .append(Component.text("land-sliding.enabled", NamedTextColor.YELLOW))
                        .append(Component.text(": Текущее: ", NamedTextColor.GRAY))
                        .append(Component.text(config.isLandSlidingEnabled(), config.isLandSlidingEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED))
                        .append(Component.text(" (по умолчанию: true)", NamedTextColor.DARK_AQUA))
        );
        sender.sendMessage(
                Component.text("  • ", NamedTextColor.DARK_GRAY)
                        .append(Component.text("land-sliding.friction-multiplier", NamedTextColor.YELLOW))
                        .append(Component.text(": Текущее: ", NamedTextColor.GRAY))
                        .append(Component.text(String.format("%.2f", config.getLandSlidingMultiplier()), NamedTextColor.GREEN))
                        .append(Component.text(" (по умолчанию: 1.05)", NamedTextColor.DARK_AQUA))
        );
        sender.sendMessage(
                Component.text("    Рекомендуемый диапазон: ", NamedTextColor.GRAY)
                        .append(Component.text("1.02 - 1.08", NamedTextColor.WHITE))
                        .append(Component.text(" (макс: 1.20)", NamedTextColor.DARK_GRAY))
        );
        sender.sendMessage(
                Component.text("    Совет: ", NamedTextColor.BLUE)
                        .append(Component.text("1.05 даёт плавный эффект льда. Значения выше 1.12 могут провоцировать ложные кики строгих античитов.", NamedTextColor.GRAY))
        );

        sender.sendMessage(Component.empty());

        // Fast swimming section
        sender.sendMessage(
                Component.text("2. Ускоренное плавание (Fast Swimming):", NamedTextColor.GOLD, TextDecoration.BOLD)
        );
        sender.sendMessage(
                Component.text("  • ", NamedTextColor.DARK_GRAY)
                        .append(Component.text("fast-swimming.enabled", NamedTextColor.YELLOW))
                        .append(Component.text(": Текущее: ", NamedTextColor.GRAY))
                        .append(Component.text(config.isFastSwimmingEnabled(), config.isFastSwimmingEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED))
                        .append(Component.text(" (по умолчанию: true)", NamedTextColor.DARK_AQUA))
        );
        sender.sendMessage(
                Component.text("  • ", NamedTextColor.DARK_GRAY)
                        .append(Component.text("fast-swimming.speed-multiplier", NamedTextColor.YELLOW))
                        .append(Component.text(": Текущее: ", NamedTextColor.GRAY))
                        .append(Component.text(String.format("%.2f", config.getFastSwimmingMultiplier()), NamedTextColor.GREEN))
                        .append(Component.text(" (по умолчанию: 1.25)", NamedTextColor.DARK_AQUA))
        );
        sender.sendMessage(
                Component.text("    Рекомендуемый диапазон: ", NamedTextColor.GRAY)
                        .append(Component.text("1.15 - 1.40", NamedTextColor.WHITE))
                        .append(Component.text(" (макс: 2.50)", NamedTextColor.DARK_GRAY))
        );
        sender.sendMessage(
                Component.text("    Совет: ", NamedTextColor.BLUE)
                        .append(Component.text("1.25 даёт приятное ощущение дельфина. Выше 1.60 ощущается как сверхбыстрая торпеда.", NamedTextColor.GRAY))
        );

        sender.sendMessage(Component.empty());

        // Helpful command tips
        sender.sendMessage(
                Component.text("Команды быстрой настройки:", NamedTextColor.GOLD)
        );
        sender.sendMessage(
                Component.text("  /seabound config set <параметр> <значение>", NamedTextColor.YELLOW)
                        .hoverEvent(HoverEvent.showText(Component.text("Кликните, чтобы вставить команду в чат")))
                        .clickEvent(ClickEvent.suggestCommand("/seabound config set "))
        );
        sender.sendMessage(
                Component.text("  /seabound config reset", NamedTextColor.YELLOW)
                        .append(Component.text(" - сбросить всё на базовые значения", NamedTextColor.GRAY))
                        .hoverEvent(HoverEvent.showText(Component.text("Кликните, чтобы сбросить настройки")))
                        .clickEvent(ClickEvent.suggestCommand("/seabound config reset"))
        );
        sender.sendMessage(
                Component.text("  /seabound config reload", NamedTextColor.YELLOW)
                        .append(Component.text(" - перезагрузить config.yml с диска", NamedTextColor.GRAY))
                        .hoverEvent(HoverEvent.showText(Component.text("Кликните, чтобы перезагрузить конфиг")))
                        .clickEvent(ClickEvent.suggestCommand("/seabound config reload"))
        );
        sender.sendMessage(
                Component.text("==================================================", NamedTextColor.AQUA)
        );
    }
}

package io.github.seabound.player;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager {

    private final JavaPlugin plugin;
    private final Set<UUID> players = new HashSet<>();
    private final Map<UUID, Boolean> playerVision = new HashMap<>();

    public PlayerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        players.clear();
        for (String uuidStr : plugin.getConfig().getStringList("players")) {
            try {
                players.add(UUID.fromString(uuidStr));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Skipping invalid UUID in config: " + uuidStr);
            }
        }

        playerVision.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("player-vision");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    playerVision.put(uuid, section.getBoolean(key));
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Skipping invalid UUID in player-vision: " + key);
                }
            }
        }
    }

    public boolean add(UUID uuid) {
        boolean added = players.add(uuid);
        if (added) {
            save();
        }
        return added;
    }

    public boolean remove(UUID uuid) {
        boolean removed = players.remove(uuid);
        playerVision.remove(uuid);
        if (removed) {
            save();
        }
        return removed;
    }

    public boolean contains(UUID uuid) {
        return players.contains(uuid);
    }

    public boolean isVisionEnabled(UUID uuid, boolean defaultEnabled) {
        return playerVision.getOrDefault(uuid, defaultEnabled);
    }

    public boolean toggleVision(UUID uuid, boolean defaultEnabled) {
        boolean current = isVisionEnabled(uuid, defaultEnabled);
        boolean next = !current;
        playerVision.put(uuid, next);
        save();
        return next;
    }

    public void setVisionEnabled(UUID uuid, boolean enabled) {
        playerVision.put(uuid, enabled);
        save();
    }

    private void save() {
        plugin.getConfig().set(
                "players",
                players.stream()
                        .map(UUID::toString)
                        .toList()
        );

        ConfigurationSection section = plugin.getConfig().createSection("player-vision");
        for (Map.Entry<UUID, Boolean> entry : playerVision.entrySet()) {
            section.set(entry.getKey().toString(), entry.getValue());
        }

        plugin.saveConfig();
    }

    public String uuidToName(UUID uuid) {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        return name != null ? name : uuid.toString();
    }

    @Deprecated
    public String UUIDtoName(UUID uuid) {
        return uuidToName(uuid);
    }

    public Set<UUID> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    @Override
    public String toString() {
        if (players.isEmpty()) {
            return "List is empty.";
        }

        return players.stream()
                .map(this::uuidToName)
                .collect(Collectors.joining(", "));
    }
}
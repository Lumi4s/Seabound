package io.github.seabound.player;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerManager {

    private final JavaPlugin plugin;
    private final Set<UUID> players = new HashSet<>();

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
        if (removed) {
            save();
        }
        return removed;
    }

    public boolean contains(UUID uuid) {
        return players.contains(uuid);
    }

    private void save() {
        plugin.getConfig().set(
                "players",
                players.stream()
                        .map(UUID::toString)
                        .toList()
        );

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
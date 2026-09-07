package io.github.reversebreathing.player;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {

    private final JavaPlugin plugin;
    private final Set<UUID> players = new HashSet<>();

    public PlayerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        for (String uuid : plugin.getConfig().getStringList("players")) {
            players.add(UUID.fromString(uuid));
        }
    }

    public void add(UUID uuid) {
        players.add(uuid);
        save();
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
        save();
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


    public String UUIDtoName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public Set<UUID> getPlayers() {
        return players;
    }


    @Override
    public String toString() {
        StringBuilder listOfAllPlayers = new StringBuilder();

        for (UUID uuid : players) {
            if (!listOfAllPlayers.isEmpty()) {
                listOfAllPlayers.append(", ");
            }

            listOfAllPlayers.append(UUIDtoName(uuid));
        }

        if (listOfAllPlayers.isEmpty()) {
            listOfAllPlayers.append("List is empty.");
        }

        return listOfAllPlayers.toString();
    }
}
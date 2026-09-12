package io.github.seabound.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SeaboundConfig {

    public static final boolean DEFAULT_FAST_SWIMMING_ENABLED = true;
    public static final double DEFAULT_FAST_SWIMMING_MULTIPLIER = 1.25;
    public static final double MIN_FAST_SWIMMING_MULTIPLIER = 1.05;
    public static final double MAX_FAST_SWIMMING_MULTIPLIER = 2.50;

    public static final List<String> PARAMETER_KEYS = List.of(
            "fast-swimming.enabled",
            "fast-swimming.speed-multiplier"
    );

    private final JavaPlugin plugin;

    private boolean fastSwimmingEnabled = DEFAULT_FAST_SWIMMING_ENABLED;
    private double fastSwimmingMultiplier = DEFAULT_FAST_SWIMMING_MULTIPLIER;

    public SeaboundConfig(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        FileConfiguration config = plugin.getConfig();

        if (config.contains("movement.fast-swimming.enabled")) {
            fastSwimmingEnabled = config.getBoolean("movement.fast-swimming.enabled", DEFAULT_FAST_SWIMMING_ENABLED);
        } else {
            fastSwimmingEnabled = DEFAULT_FAST_SWIMMING_ENABLED;
        }

        if (config.contains("movement.fast-swimming.speed-multiplier")) {
            fastSwimmingMultiplier = clamp(
                    config.getDouble("movement.fast-swimming.speed-multiplier", DEFAULT_FAST_SWIMMING_MULTIPLIER),
                    MIN_FAST_SWIMMING_MULTIPLIER,
                    MAX_FAST_SWIMMING_MULTIPLIER
            );
        } else {
            fastSwimmingMultiplier = DEFAULT_FAST_SWIMMING_MULTIPLIER;
        }

        save();
    }

    public void save() {
        FileConfiguration config = plugin.getConfig();
        config.set("movement.land-sliding", null);
        config.set("movement.fast-swimming.enabled", fastSwimmingEnabled);
        config.set("movement.fast-swimming.speed-multiplier", fastSwimmingMultiplier);
        plugin.saveConfig();
    }

    public void reset() {
        fastSwimmingEnabled = DEFAULT_FAST_SWIMMING_ENABLED;
        fastSwimmingMultiplier = DEFAULT_FAST_SWIMMING_MULTIPLIER;
        save();
    }

    public String canonicalKey(String key) {
        String lower = key.toLowerCase().trim();
        return switch (lower) {
            case "fast-swimming.enabled", "swimming.enabled", "fast-swimming", "swimming" -> "fast-swimming.enabled";
            case "fast-swimming.speed-multiplier", "swimming.speed", "swim-speed", "speed-multiplier" -> "fast-swimming.speed-multiplier";
            default -> null;
        };
    }

    public String getValue(String key) {
        String canonical = canonicalKey(key);
        if (canonical == null) return null;

        return switch (canonical) {
            case "fast-swimming.enabled" -> String.valueOf(fastSwimmingEnabled);
            case "fast-swimming.speed-multiplier" -> String.format("%.2f", fastSwimmingMultiplier);
            default -> null;
        };
    }

    public void setValue(String key, String value) throws IllegalArgumentException {
        String canonical = canonicalKey(key);
        if (canonical == null) {
            throw new IllegalArgumentException("Unknown configuration key: " + key);
        }

        switch (canonical) {
            case "fast-swimming.enabled" -> {
                if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                    throw new IllegalArgumentException("Expected 'true' or 'false' for " + canonical);
                }
                fastSwimmingEnabled = Boolean.parseBoolean(value);
            }
            case "fast-swimming.speed-multiplier" -> {
                double val = parseDouble(value);
                if (val < MIN_FAST_SWIMMING_MULTIPLIER || val > MAX_FAST_SWIMMING_MULTIPLIER) {
                    throw new IllegalArgumentException(String.format("Value must be between %.2f and %.2f",
                            MIN_FAST_SWIMMING_MULTIPLIER, MAX_FAST_SWIMMING_MULTIPLIER));
                }
                fastSwimmingMultiplier = val;
            }
        }

        save();
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + str);
        }
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public boolean isFastSwimmingEnabled() {
        return fastSwimmingEnabled;
    }

    public double getFastSwimmingMultiplier() {
        return fastSwimmingMultiplier;
    }
}

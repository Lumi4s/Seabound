package io.github.seabound.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SeaboundConfig {

    public static final boolean DEFAULT_FAST_SWIMMING_ENABLED = true;
    public static final double DEFAULT_FAST_SWIMMING_MULTIPLIER = 1.25;
    public static final double MIN_FAST_SWIMMING_MULTIPLIER = 1.05;
    public static final double MAX_FAST_SWIMMING_MULTIPLIER = 2.50;

    public static final boolean DEFAULT_VISION_ENABLED = true;
    public static final boolean DEFAULT_VISION_DEFAULT_ENABLED = true;
    public static final boolean DEFAULT_VISION_HOTKEY_ENABLED = true;

    public static final boolean DEFAULT_GUARDIANS_ENABLED = true;
    public static final boolean DEFAULT_GUARDIANS_RETALIATE = true;
    public static final boolean DEFAULT_GUARDIANS_PROTECT_FATIGUE = true;

    public static final List<String> PARAMETER_KEYS = List.of(
            "fast-swimming.enabled",
            "fast-swimming.speed-multiplier",
            "vision.enabled",
            "vision.default-enabled",
            "vision.hotkey-toggle",
            "guardians.enabled",
            "guardians.retaliate",
            "guardians.protect-from-fatigue"
    );

    private final JavaPlugin plugin;

    private boolean fastSwimmingEnabled = DEFAULT_FAST_SWIMMING_ENABLED;
    private double fastSwimmingMultiplier = DEFAULT_FAST_SWIMMING_MULTIPLIER;

    private boolean visionEnabled = DEFAULT_VISION_ENABLED;
    private boolean visionDefaultEnabled = DEFAULT_VISION_DEFAULT_ENABLED;
    private boolean visionHotkeyEnabled = DEFAULT_VISION_HOTKEY_ENABLED;

    private boolean guardiansEnabled = DEFAULT_GUARDIANS_ENABLED;
    private boolean guardiansRetaliate = DEFAULT_GUARDIANS_RETALIATE;
    private boolean guardiansProtectFatigue = DEFAULT_GUARDIANS_PROTECT_FATIGUE;

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

        if (config.contains("vision.enabled")) {
            visionEnabled = config.getBoolean("vision.enabled", DEFAULT_VISION_ENABLED);
        } else {
            visionEnabled = DEFAULT_VISION_ENABLED;
        }

        if (config.contains("vision.default-enabled")) {
            visionDefaultEnabled = config.getBoolean("vision.default-enabled", DEFAULT_VISION_DEFAULT_ENABLED);
        } else {
            visionDefaultEnabled = DEFAULT_VISION_DEFAULT_ENABLED;
        }

        if (config.contains("vision.hotkey-toggle")) {
            visionHotkeyEnabled = config.getBoolean("vision.hotkey-toggle", DEFAULT_VISION_HOTKEY_ENABLED);
        } else {
            visionHotkeyEnabled = DEFAULT_VISION_HOTKEY_ENABLED;
        }

        if (config.contains("guardians.enabled")) {
            guardiansEnabled = config.getBoolean("guardians.enabled", DEFAULT_GUARDIANS_ENABLED);
        } else {
            guardiansEnabled = DEFAULT_GUARDIANS_ENABLED;
        }

        if (config.contains("guardians.retaliate")) {
            guardiansRetaliate = config.getBoolean("guardians.retaliate", DEFAULT_GUARDIANS_RETALIATE);
        } else {
            guardiansRetaliate = DEFAULT_GUARDIANS_RETALIATE;
        }

        if (config.contains("guardians.protect-from-fatigue")) {
            guardiansProtectFatigue = config.getBoolean("guardians.protect-from-fatigue", DEFAULT_GUARDIANS_PROTECT_FATIGUE);
        } else {
            guardiansProtectFatigue = DEFAULT_GUARDIANS_PROTECT_FATIGUE;
        }

        save();
    }

    public void save() {
        FileConfiguration config = plugin.getConfig();
        config.set("movement.land-sliding", null);
        config.set("movement.fast-swimming.enabled", fastSwimmingEnabled);
        config.set("movement.fast-swimming.speed-multiplier", fastSwimmingMultiplier);
        config.set("vision.enabled", visionEnabled);
        config.set("vision.default-enabled", visionDefaultEnabled);
        config.set("vision.hotkey-toggle", visionHotkeyEnabled);
        config.set("guardians.enabled", guardiansEnabled);
        config.set("guardians.retaliate", guardiansRetaliate);
        config.set("guardians.protect-from-fatigue", guardiansProtectFatigue);
        plugin.saveConfig();
    }

    public void reset() {
        fastSwimmingEnabled = DEFAULT_FAST_SWIMMING_ENABLED;
        fastSwimmingMultiplier = DEFAULT_FAST_SWIMMING_MULTIPLIER;
        visionEnabled = DEFAULT_VISION_ENABLED;
        visionDefaultEnabled = DEFAULT_VISION_DEFAULT_ENABLED;
        visionHotkeyEnabled = DEFAULT_VISION_HOTKEY_ENABLED;
        guardiansEnabled = DEFAULT_GUARDIANS_ENABLED;
        guardiansRetaliate = DEFAULT_GUARDIANS_RETALIATE;
        guardiansProtectFatigue = DEFAULT_GUARDIANS_PROTECT_FATIGUE;
        save();
    }

    public String canonicalKey(String key) {
        String lower = key.toLowerCase().trim();
        return switch (lower) {
            case "fast-swimming.enabled", "swimming.enabled", "fast-swimming", "swimming" -> "fast-swimming.enabled";
            case "fast-swimming.speed-multiplier", "swimming.speed", "swim-speed", "speed-multiplier" -> "fast-swimming.speed-multiplier";
            case "vision.enabled", "vision", "underwater-vision" -> "vision.enabled";
            case "vision.default-enabled", "vision.default", "vision-default" -> "vision.default-enabled";
            case "vision.hotkey-toggle", "vision.hotkey", "vision-hotkey" -> "vision.hotkey-toggle";
            case "guardians.enabled", "guardians", "guardian" -> "guardians.enabled";
            case "guardians.retaliate", "guardians.neutral", "guardian.retaliate" -> "guardians.retaliate";
            case "guardians.protect-from-fatigue", "guardians.fatigue", "guardians.protect-fatigue" -> "guardians.protect-from-fatigue";
            default -> null;
        };
    }

    public String getValue(String key) {
        String canonical = canonicalKey(key);
        if (canonical == null) return null;

        return switch (canonical) {
            case "fast-swimming.enabled" -> String.valueOf(fastSwimmingEnabled);
            case "fast-swimming.speed-multiplier" -> String.format("%.2f", fastSwimmingMultiplier);
            case "vision.enabled" -> String.valueOf(visionEnabled);
            case "vision.default-enabled" -> String.valueOf(visionDefaultEnabled);
            case "vision.hotkey-toggle" -> String.valueOf(visionHotkeyEnabled);
            case "guardians.enabled" -> String.valueOf(guardiansEnabled);
            case "guardians.retaliate" -> String.valueOf(guardiansRetaliate);
            case "guardians.protect-from-fatigue" -> String.valueOf(guardiansProtectFatigue);
            default -> null;
        };
    }

    public void setValue(String key, String value) throws IllegalArgumentException {
        String canonical = canonicalKey(key);
        if (canonical == null) {
            throw new IllegalArgumentException("Unknown configuration key: " + key);
        }

        switch (canonical) {
            case "fast-swimming.enabled" -> fastSwimmingEnabled = parseBoolean(canonical, value);
            case "fast-swimming.speed-multiplier" -> {
                double val = parseDouble(value);
                if (val < MIN_FAST_SWIMMING_MULTIPLIER || val > MAX_FAST_SWIMMING_MULTIPLIER) {
                    throw new IllegalArgumentException(String.format("Value must be between %.2f and %.2f",
                            MIN_FAST_SWIMMING_MULTIPLIER, MAX_FAST_SWIMMING_MULTIPLIER));
                }
                fastSwimmingMultiplier = val;
            }
            case "vision.enabled" -> visionEnabled = parseBoolean(canonical, value);
            case "vision.default-enabled" -> visionDefaultEnabled = parseBoolean(canonical, value);
            case "vision.hotkey-toggle" -> visionHotkeyEnabled = parseBoolean(canonical, value);
            case "guardians.enabled" -> guardiansEnabled = parseBoolean(canonical, value);
            case "guardians.retaliate" -> guardiansRetaliate = parseBoolean(canonical, value);
            case "guardians.protect-from-fatigue" -> guardiansProtectFatigue = parseBoolean(canonical, value);
        }

        save();
    }

    private boolean parseBoolean(String key, String value) {
        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Expected 'true' or 'false' for " + key);
        }
        return Boolean.parseBoolean(value);
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

    public boolean isVisionEnabled() {
        return visionEnabled;
    }

    public boolean isVisionDefaultEnabled() {
        return visionDefaultEnabled;
    }

    public boolean isVisionHotkeyEnabled() {
        return visionHotkeyEnabled;
    }

    public boolean isGuardiansEnabled() {
        return guardiansEnabled;
    }

    public boolean isGuardiansRetaliate() {
        return guardiansRetaliate;
    }

    public boolean isGuardiansProtectFatigue() {
        return guardiansProtectFatigue;
    }
}

package io.github.seabound.service;

import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.player.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public final class VisionService {

    private final SeaboundConfig config;
    private final PlayerManager playerManager;

    public VisionService(SeaboundConfig config, PlayerManager playerManager) {
        this.config = config;
        this.playerManager = playerManager;
    }

    public void tick(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR || player.isDead()) {
            removeVisionEffect(player);
            return;
        }

        if (!config.isVisionEnabled()) {
            removeVisionEffect(player);
            return;
        }

        boolean visionActive = playerManager.isVisionEnabled(player.getUniqueId(), config.isVisionDefaultEnabled());
        boolean eyeInWater = player.isUnderWater() || player.getEyeLocation().getBlock().isLiquid();

        if (visionActive && eyeInWater) {
            applyVisionEffect(player);
        } else {
            removeVisionEffect(player);
        }
    }

    private void applyVisionEffect(Player player) {
        PotionEffect current = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
        // Only apply if the player doesn't already have a night vision effect
        if (current == null) {
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.NIGHT_VISION,
                    PotionEffect.INFINITE_DURATION,
                    0,
                    false,
                    false,
                    false
            ));
        }
    }

    public void removeVisionEffect(Player player) {
        PotionEffect current = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
        // Only remove infinite effect given by Seabound, never clear regular user potions
        if (current != null && current.isInfinite()) {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    public void cleanup(UUID uuid) {
    }
}

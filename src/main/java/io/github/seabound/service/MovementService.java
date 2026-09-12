package io.github.seabound.service;

import io.github.seabound.config.SeaboundConfig;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.UUID;

public final class MovementService {

    private final SeaboundConfig config;

    public MovementService(SeaboundConfig config) {
        this.config = config;
    }

    public void tick(Player player) {
        if (player.getGameMode() == GameMode.SPECTATOR || player.isDead()) {
            removeWaterEffect(player);
            return;
        }

        boolean inWater = player.isInWater() || player.isUnderWater();

        if (inWater) {
            handleWater(player);
        } else {
            removeWaterEffect(player);
        }
    }

    private void handleWater(Player player) {
        if (!config.isFastSwimmingEnabled()) {
            removeWaterEffect(player);
            return;
        }

        int amplifier = config.getFastSwimmingMultiplier() >= 1.5 ? 1 : 0;
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.DOLPHINS_GRACE,
                30,
                amplifier,
                false,
                false,
                false
        ));

        if (player.isSwimming() && config.getFastSwimmingMultiplier() > 1.30) {
            double extra = (config.getFastSwimmingMultiplier() - 1.30) * 0.05;
            Vector look = player.getLocation().getDirection();
            Vector current = player.getVelocity();
            Vector added = current.add(look.multiply(extra));
            double speed = added.length();
            if (speed > 1.3) {
                added.multiply(1.3 / speed);
            }
            player.setVelocity(added);
        }
    }

    public void removeWaterEffect(Player player) {
        if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
            player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        }
    }

    public void cleanup(UUID uuid) {
    }
}

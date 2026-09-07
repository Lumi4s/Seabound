package io.github.reversebreathing.service;

import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class ReverseBreathingService {

    private static final int DAMAGE_INTERVAL = 30;

    private final Map<UUID, Integer> damageTimers = new HashMap<>();

    public void tick(Player player) {
        int air = player.getRemainingAir();
        int maxAir = player.getMaximumAir();
        int normalizedAir = air;
        int respirationLVL = getRespiration(player);

        boolean canBreath =
                (player.isUnderWater()
                        || player.isInRain()
                        || player.hasPotionEffect(PotionEffectType.WATER_BREATHING)
                        || player.hasPotionEffect(PotionEffectType.CONDUIT_POWER));

        if (canBreath) {
            if (player.isUnderWater()) {
                normalizedAir += 1;
            } else if (player.isInRain()
                    || player.hasPotionEffect(PotionEffectType.WATER_BREATHING)
                    || player.hasPotionEffect(PotionEffectType.CONDUIT_POWER)) {
                normalizedAir -= 4;
            }
            player.setRemainingAir(Math.min(normalizedAir + 4, maxAir));
            damageTimers.remove(player.getUniqueId());
            return;
        }

        normalizedAir = air - 4;

        if (shouldLoseAir(respirationLVL)) {
            player.setRemainingAir(Math.max(normalizedAir - 1, 0));
        } else {
            player.setRemainingAir(Math.max(normalizedAir, 0));
        }

        if (player.getRemainingAir() <= 0) {
            if (shouldLoseAir(respirationLVL)) {
                handleDrowning(player);
            }
        } else {
            damageTimers.remove(player.getUniqueId());
        }
    }

    private void handleDrowning(Player player) {
        UUID uuid = player.getUniqueId();

        int timer = damageTimers.getOrDefault(uuid, DAMAGE_INTERVAL);

        timer--;

        if (timer <= 0) {
            DamageSource damageSource =
                    DamageSource.builder(DamageType.DROWN).build();

            player.damage(2.0, damageSource);

            timer = DAMAGE_INTERVAL;
        }

        damageTimers.put(uuid, timer);
    }

    private int getRespiration(Player player) {
        return player.getInventory().getHelmet().getEnchantmentLevel(Enchantment.RESPIRATION);
    }

    private boolean shouldLoseAir(int respiration) {
        return ThreadLocalRandom.current().nextInt(respiration + 1) == 0;
    }
}
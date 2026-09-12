package io.github.seabound.service;

import org.bukkit.GameMode;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class SeaboundService {

    private static final int DAMAGE_INTERVAL = 30;

    private final Map<UUID, Integer> damageTimers = new HashMap<>();

    public void tick(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR
                || player.isDead()
                || player.isInvulnerable()) {
            damageTimers.remove(player.getUniqueId());
            return;
        }

        if (canBreathe(player)) {
            if (player.getRemainingAir() < player.getMaximumAir()) {
                player.setRemainingAir(Math.min(player.getRemainingAir() + 4, player.getMaximumAir()));
            }
            damageTimers.remove(player.getUniqueId());
            return;
        }

        int respirationLVL = getRespiration(player);

        if (shouldLoseAir(respirationLVL)) {
            player.setRemainingAir(Math.max(player.getRemainingAir() - 1, 0));
        }

        if (player.getRemainingAir() <= 0) {
            handleDrowning(player);
        } else {
            damageTimers.remove(player.getUniqueId());
        }
    }

    public boolean canBreathe(Player player) {
        return player.isUnderWater()
                || player.isInRain()
                || player.hasPotionEffect(PotionEffectType.WATER_BREATHING)
                || player.hasPotionEffect(PotionEffectType.CONDUIT_POWER);
    }

    public void cleanup(UUID uuid) {
        damageTimers.remove(uuid);
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
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet == null) {
            return 0;
        }
        return helmet.getEnchantmentLevel(Enchantment.RESPIRATION);
    }

    private boolean shouldLoseAir(int respiration) {
        return ThreadLocalRandom.current().nextInt(respiration + 1) == 0;
    }
}
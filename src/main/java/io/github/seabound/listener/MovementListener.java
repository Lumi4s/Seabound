package io.github.seabound.listener;

import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.player.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MovementListener implements Listener {

    private final PlayerManager playerManager;
    private final SeaboundConfig config;
    private final Map<UUID, Long> lastBoostTime = new HashMap<>();

    public MovementListener(PlayerManager playerManager, SeaboundConfig config) {
        this.playerManager = playerManager;
        this.config = config;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!playerManager.contains(player.getUniqueId())) {
            return;
        }

        if (player.getGameMode() == GameMode.SPECTATOR || player.isDead() || player.isFlying()) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double horizontalDistSq = dx * dx + dz * dz;

        if (horizontalDistSq < 0.0004) {
            return;
        }

        long now = System.currentTimeMillis();
        long last = lastBoostTime.getOrDefault(player.getUniqueId(), 0L);
        // Limit physics velocity boosts to at most once per 45ms (approx 1 tick)
        if (now - last < 45) {
            return;
        }

        boolean inWater = player.isInWater() || player.isUnderWater();

        if (inWater) {
            handleWaterMovement(player, dx, dz);
            lastBoostTime.put(player.getUniqueId(), now);
        } else {
            handleLandMovement(player, dx, dz);
            lastBoostTime.put(player.getUniqueId(), now);
        }
    }

    @SuppressWarnings("deprecation")
    private void handleLandMovement(Player player, double dx, double dz) {
        if (!config.isLandSlidingEnabled()) {
            return;
        }

        if (!player.isOnGround() || player.isGliding()) {
            return;
        }

        Block blockBelow = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (isIceBlock(blockBelow.getType())) {
            return;
        }

        double multiplier = config.getLandSlidingMultiplier();
        double excess = multiplier - 1.0;
        if (excess <= 0.0) {
            return;
        }

        Vector moveDir = new Vector(dx, 0, dz);
        double dist = moveDir.length();
        if (dist < 0.02) {
            return;
        }

        Vector slideBoost = moveDir.normalize().multiply(dist * excess * 2.2);

        Vector currentVelocity = player.getVelocity();
        double newX = currentVelocity.getX() + slideBoost.getX();
        double newZ = currentVelocity.getZ() + slideBoost.getZ();

        double currentHorizSpeed = Math.hypot(newX, newZ);
        if (currentHorizSpeed > 0.65) {
            double scale = 0.65 / currentHorizSpeed;
            newX *= scale;
            newZ *= scale;
        }

        player.setVelocity(new Vector(newX, currentVelocity.getY(), newZ));
    }

    private void handleWaterMovement(Player player, double dx, double dz) {
        if (!config.isFastSwimmingEnabled()) {
            return;
        }

        double multiplier = config.getFastSwimmingMultiplier();
        double excess = multiplier - 1.0;
        if (excess <= 0.0) {
            return;
        }

        Vector currentVelocity = player.getVelocity();

        if (player.isSwimming()) {
            // Sprint swimming: boost in look direction
            Vector lookDir = player.getLocation().getDirection();
            Vector boost = lookDir.clone().multiply(excess * 0.12);

            Vector newVel = currentVelocity.add(boost);
            double speed = newVel.length();
            if (speed > 1.2) {
                newVel.multiply(1.2 / speed);
            }
            player.setVelocity(newVel);
        } else {
            // General underwater movement: boost along horizontal movement direction
            Vector moveDir = new Vector(dx, 0, dz);
            double dist = moveDir.length();
            if (dist > 0.02) {
                Vector boost = moveDir.normalize().multiply(dist * excess * 0.5);
                double newX = currentVelocity.getX() + boost.getX();
                double newZ = currentVelocity.getZ() + boost.getZ();

                double horizSpeed = Math.hypot(newX, newZ);
                if (horizSpeed > 0.8) {
                    double scale = 0.8 / horizSpeed;
                    newX *= scale;
                    newZ *= scale;
                }

                player.setVelocity(new Vector(newX, currentVelocity.getY(), newZ));
            }
        }
    }

    private boolean isIceBlock(Material material) {
        return material == Material.ICE
                || material == Material.PACKED_ICE
                || material == Material.BLUE_ICE
                || material == Material.FROSTED_ICE;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastBoostTime.remove(event.getPlayer().getUniqueId());
    }
}

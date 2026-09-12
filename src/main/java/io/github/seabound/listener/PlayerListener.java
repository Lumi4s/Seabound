package io.github.seabound.listener;

import io.github.seabound.config.SeaboundConfig;
import io.github.seabound.player.PlayerManager;
import io.github.seabound.service.SeaboundService;
import io.github.seabound.service.VisionService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffectType;

public final class PlayerListener implements Listener {

    private final PlayerManager playerManager;
    private final SeaboundService seaboundService;
    private final SeaboundConfig seaboundConfig;
    private final VisionService visionService;

    public PlayerListener(
            PlayerManager playerManager,
            SeaboundService seaboundService,
            SeaboundConfig seaboundConfig,
            VisionService visionService
    ) {
        this.playerManager = playerManager;
        this.seaboundService = seaboundService;
        this.seaboundConfig = seaboundConfig;
        this.visionService = visionService;
    }

    @EventHandler(ignoreCancelled = true)
    public void onAirChange(EntityAirChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!playerManager.contains(player.getUniqueId())) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (seaboundService.canBreathe(player)) {
            if (event.getAmount() < player.getRemainingAir()) {
                event.setCancelled(true);
                if (player.getRemainingAir() < player.getMaximumAir()) {
                    player.setRemainingAir(Math.min(player.getRemainingAir() + 4, player.getMaximumAir()));
                }
            }
        } else {
            if (event.getAmount() > player.getRemainingAir()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onGuardianTarget(EntityTargetLivingEntityEvent event) {
        if (!seaboundConfig.isGuardiansEnabled()) {
            return;
        }

        if (event.getEntity() instanceof Guardian) {
            if (event.getTarget() instanceof Player player && playerManager.contains(player.getUniqueId())) {
                // Neutral mode: if retaliate is true and target attacked the guardian, allow defense
                if (seaboundConfig.isGuardiansRetaliate() && event.getReason() == EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY) {
                    return;
                }

                event.setCancelled(true);
                event.setTarget(null);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (!seaboundConfig.isGuardiansEnabled() || !seaboundConfig.isGuardiansProtectFatigue()) {
            return;
        }

        if (event.getEntity() instanceof Player player && playerManager.contains(player.getUniqueId())) {
            if (event.getModifiedType() == PotionEffectType.MINING_FATIGUE) {
                // Block curse from Elder Guardians, but allow admin commands or custom plugins
                if (event.getCause() != EntityPotionEffectEvent.Cause.COMMAND
                        && event.getCause() != EntityPotionEffectEvent.Cause.PLUGIN) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if (!seaboundConfig.isVisionEnabled() || !seaboundConfig.isVisionHotkeyEnabled()) {
            return;
        }

        if (!playerManager.contains(player.getUniqueId())) {
            return;
        }

        if (player.isSneaking() && (player.isInWater() || player.isUnderWater())) {
            event.setCancelled(true);
            boolean newState = playerManager.toggleVision(player.getUniqueId(), seaboundConfig.isVisionDefaultEnabled());

            Component status = Component.text("Underwater Vision: ", NamedTextColor.AQUA)
                    .append(Component.text(newState ? "ON" : "OFF", newState ? NamedTextColor.GREEN : NamedTextColor.RED));
            player.sendActionBar(status);

            player.playSound(
                    player.getLocation(),
                    newState ? Sound.BLOCK_CONDUIT_ACTIVATE : Sound.BLOCK_CONDUIT_DEACTIVATE,
                    0.6f,
                    1.2f
            );
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        seaboundService.cleanup(event.getPlayer().getUniqueId());
        visionService.removeVisionEffect(event.getPlayer());
        visionService.cleanup(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        seaboundService.cleanup(event.getPlayer().getUniqueId());
        visionService.removeVisionEffect(event.getPlayer());
        visionService.cleanup(event.getPlayer().getUniqueId());
    }
}

package me.paulvogel.easyjetpack;

import me.paulvogel.easyjetpack.api.EasyJetpackAPI;
import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class JetpackListener implements Listener {
    @EventHandler
    public void onPlayerToggleCrouchEvent(PlayerToggleSneakEvent evt) {
        EasyJetpackAPI.getManager().setCrouching(evt.getPlayer(), evt.isSneaking());
        if (evt.isSneaking()) {
            JetpackEvent event = new JetpackEvent(evt.getPlayer(),
                    FlightTypes.CROUCH, null, evt);
            if (EasyJetpackAPI.getManager().onJetpackEvent(event) || event.isCancelled()) {
                evt.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        EasyJetpackAPI.getManager().setCrouching(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        EasyJetpackAPI.getManager().setCrouching(event.getEntity(), false);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        EasyJetpackAPI.getManager().setCrouching(event.getPlayer(), false);
    }

    @EventHandler
    public void onFlightToggleEvent(PlayerToggleFlightEvent evt) {
        if (evt.getPlayer().getGameMode() != GameMode.CREATIVE
                && EasyJetpack.getInstance().haveAllowedFlying(evt.getPlayer())) {
            if (!evt.isFlying()) {
                JetpackEvent event = new JetpackEvent(evt.getPlayer(),
                        FlightTypes.CROUCH, null, evt);
                EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent evt) {
        if (evt.getEntity() instanceof Player) {
            if (evt.getCause() == DamageCause.FALL) {
                ItemStack chestplate = ((Player) evt.getEntity()).getInventory().getChestplate();
                if (evt.getEntity().hasPermission("easyjetpack.nofall")
                        && EasyJetpackAPI.getManager().isJetpack(chestplate)) {
                    evt.setCancelled(true);
                    return;
                }
                JetpackEvent event = new JetpackEvent(
                        ((Player) evt.getEntity()), FlightTypes.FALLING, null, evt);
                EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
            }
            if (evt.getCause() == DamageCause.DROWNING) {
                JetpackEvent event = new JetpackEvent(
                        ((Player) evt.getEntity()), FlightTypes.DROWNING, null, evt);
                EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRenameItem(InventoryClickEvent evt) {
        if (evt.getView().getType() == InventoryType.ANVIL) {
            if (evt.getRawSlot() == 2) {
                if (EasyJetpackAPI.getManager().isJetpack(
                        evt.getView().getItem(0))
                        || EasyJetpackAPI.getManager().isJetpack(
                        evt.getView().getItem(1))) {
                    JetpackEvent event = new JetpackEvent(
                            (Player) evt.getWhoClicked(), FlightTypes.ANVIL,
                            evt.getView().getItem(0), evt);
                    EasyJetpackAPI.getManager().onJetpackEvent(event);
                    if (event.isCancelled()) {
                        evt.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        JetpackEvent event = new JetpackEvent(evt.getPlayer(),
                FlightTypes.INTERACT, null, evt);
        EasyJetpackAPI.getManager().onJetpackEvent(event);
        if (event.isCancelled()) {
            evt.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent evt) {
        if (evt.getEntity() instanceof Player) {
            if (evt.getDamager() instanceof Player) {
                JetpackEvent event = new JetpackEvent((Player) evt.getEntity(),
                        FlightTypes.DAMAGED_BY_PLAYER, null, evt);
                EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
            } else {
                JetpackEvent event = new JetpackEvent((Player) evt.getEntity(),
                        FlightTypes.DAMAGED_BY_MOB, null, evt);
                EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
            }
        }
    }
}

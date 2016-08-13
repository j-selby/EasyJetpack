package net.jselby.ej;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * The JetpackListener handles Bukkit events, and sends them
 *  to Jetpacks.
 */
public class JetpackListener implements Listener {
    private final JetpackManager manager;

    /**
     * Creates a new JetpackListener.
     *
     * @param manager The parent manager.
     */
    JetpackListener(JetpackManager manager) {
        this.manager = manager;
    }

    /**
     * Handles players crouching.
     * @param evt The Bukkit event to handle.
     */
    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent evt) {
        for (Jetpack foundJetpack : manager.getEquippedJetpacks(evt.getPlayer())) {
            foundJetpack.onCrouch(evt);
        }
    }

    /**
     * Handles players taking damage.
     * @param evt The Bukkit event to handle.
     */
    @EventHandler
    public void onDamage(EntityDamageEvent evt) {
        if (evt.getEntity() instanceof Player) {
            for (Jetpack foundJetpack : manager.getEquippedJetpacks((Player) evt.getEntity())) {
                foundJetpack.onDamage(evt);
            }
        }
    }

    @EventHandler
    public void onPlayerRenameItem(InventoryClickEvent evt) {
        if (evt.getView().getType() == InventoryType.ANVIL) {
            if (evt.getRawSlot() == 2) {
                Jetpack jetpack;
                if ((jetpack = manager.getJetpackFromItem(evt.getView().getItem(0))) != null
                        || (jetpack = manager.getJetpackFromItem(evt.getView().getItem(1))) != null) {
                    jetpack.onPlayerRenameItem(evt);
                }
            }
        }
    }
}

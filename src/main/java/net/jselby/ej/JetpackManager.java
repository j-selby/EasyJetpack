package net.jselby.ej;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The JetpackManager allows for the adding and handling of Jetpacks.
 */
public class JetpackManager implements Listener {
    private static JetpackManager instance;
    private final EasyJetpackPlugin plugin;

    private HashMap<String, Jetpack> jetpacks = new HashMap<>();

    /**
     * Creates a new instance of the JetpackManager.
     *
     * This should only be accessed by the plugin itself.
     */
    JetpackManager(EasyJetpackPlugin plugin) {
        instance = this;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Gets all Jetpacks currently attached to this JetpackManager.
     * @return An array of active Jetpacks.
     */
    public Jetpack[] getJetpacks() {
        return jetpacks.values().toArray(new Jetpack[jetpacks.size()]);
    }

    /**
     * Adds a Jetpack to this manager.
     *
     * @param jetpack The Jetpack to add.
     */
    public void addJetpack(Jetpack jetpack) {
        jetpacks.put(jetpack.getGiveName().toLowerCase(), jetpack);
    }

    /**
     * Removes all active Jetpacks from this manager.
     */
    public void removeAllJetpacks() {
        for (Jetpack jetpack : jetpacks.values()) {
            jetpack.onRemove();
        }
        jetpacks.clear();
    }

    /**
     * Searches for a Jetpack by its config name.
     * @param name The Jetpack name.
     * @return A Jetpack, or null if it wasn't found.
     */
    public Jetpack getJetpack(String name) {
        return jetpacks.get(name.toLowerCase());
    }

    /**
     * Returns the Jetpacks that the player currently has equipped.
     * @param player The player to search.
     * @return An array of Jetpacks. Empty if none found.
     */
    private Jetpack[] getEquippedJetpacks(Player player) {
        ArrayList<Jetpack> foundJetpacks = new ArrayList<>();
        for (Jetpack jetpack : jetpacks.values()) {
            int slot = jetpack.searchInventory(player);
            if (slot != -1) {
                foundJetpacks.add(jetpack);
            }
        }
        return foundJetpacks.toArray(new Jetpack[foundJetpacks.size()]);
    }

    /**
     * Handles players crouching.
     * @param evt The Bukkit event to handle.
     */
    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent evt) {
        for (Jetpack foundJetpack : getEquippedJetpacks(evt.getPlayer())) {
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
            for (Jetpack foundJetpack : getEquippedJetpacks((Player) evt.getEntity())) {
                foundJetpack.onDamage(evt);
            }
        }
    }

    /**
     * Returns the host plugin of this manager.
     * @return A EasyJetpackPlugin instance.
     */
    public EasyJetpackPlugin getPlugin() {
        return plugin;
    }

    /**
     * Returns the active instance of the Manager.
     *
     * Throws error if plugin not initialized.
     *
     * @return A JetpackManager instance.
     */
    public static JetpackManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("EasyJetpack has not been initialized yet!");
        }
        return instance;
    }
}

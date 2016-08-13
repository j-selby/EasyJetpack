package net.jselby.ej;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

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
     * Adds a jetpack to this manager.
     *
     * @param jetpack The jetpack to add.
     */
    public void addJetpack(Jetpack jetpack) {
        jetpacks.put(jetpack.getGiveName().toLowerCase(), jetpack);
    }

    /**
     * Removes all active jetpacks from this manager.
     */
    public void removeAllJetpacks() {
        jetpacks.clear();
    }

    /**
     * Searches for a jetpack by name.
     */
    public Jetpack getJetpack(String name) {
        return jetpacks.get(name.toLowerCase());
    }

    @EventHandler
    public void onCrouch(PlayerToggleSneakEvent evt) {
        Jetpack foundJetpack;
        if ((foundJetpack = isJetpackEquipped(evt)) != null) {
            foundJetpack.onCrouch(evt);
        }
    }

    private Jetpack isJetpackEquipped(PlayerEvent event) {
        for (Jetpack jetpack : jetpacks.values()) {
            int slot = jetpack.searchInventory(event.getPlayer());
            if (slot != -1) {
                return jetpack;
            }
        }
        return null;
    }

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

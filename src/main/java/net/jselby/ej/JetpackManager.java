package net.jselby.ej;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The JetpackManager allows for the adding and handling of Jetpacks.
 */
public class JetpackManager {
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
        plugin.getServer().getPluginManager().registerEvents(new JetpackListener(this), plugin);
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
    public Jetpack[] getEquippedJetpacks(Player player) {
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

    public Jetpack getJetpackFromItem(ItemStack item) {
        for (Jetpack jetpack : jetpacks.values()) {
            if (jetpack.isItemThisJetpack(item)) {
                return jetpack;
            }
        }
        return null;
    }
}

package me.paulvogel.easyjetpack.api;

import me.paulvogel.easyjetpack.EasyJetpack;
import me.paulvogel.easyjetpack.Utils;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A abstract Jetpack class, allowing other plugins to implement their own
 * Jetpacks, and also used internally for the default Jetpacks.
 *
 * @author James
 */
public abstract class Jetpack {
    /**
     * Defines what the name of the Jetpack should be. This has to be compatible
     * with the ItemStack.getItemMeta().setDisplayName() method!
     *
     * @return a name
     */
    public abstract String getName();

    /**
     * Defines what the name of the Jetpack should be when trying to spawn it
     * using the /ej give command. This is also used by the default
     * getPermission().
     *
     * @return a name, without spaces
     */
    public abstract String getGiveName();

    /**
     * Defines what the description of the Jetpack should be. This will be
     * converted into a List, and used as a argument to
     * ItemStack.getItemMeta().setLore() method.
     *
     * @return A string array
     */
    public abstract String[] getDescription();

    /**
     * Returns the material of the Jetpack, but it has to be wearable by the
     * user (either by inventory modification, or the player putting it on).
     *
     * @return A Bukkit material, which the Jetpack will be created with
     */
    public abstract Material getMaterial();

    /**
     * Returns the data value of the Jetpack. This doesn't need to be
     * overridden, but it can help with wool colors etc.
     *
     * @return A byte, which the Jetpack will be created with
     */
    public byte getData() {
        return 0;
    }

    /**
     * An event called when the control has been pressed or toggled, the player
     * has fuel, and fuel has been consumed. This should set player velocity,
     * but it shouldn't check if fuel is going to be used. This is handled by
     * the onFuelUsageEvent event. This should also damage the jetpack, if you
     * want that to happen.
     *
     * @param event The event
     */
    public abstract void onFlyEvent(JetpackEvent event);

    /**
     * An event called when the control button has been pressed or toggled, and
     * the player has adequate fuel. This should consume fuel, but it shouldn't
     * fly the player around. This is handled by the onFlyEvent event. This
     * should also check if the jetpack is usable (from a damage prospective),
     * and inform the user if they run out of fuel.
     *
     * @param event The event
     */
    public abstract void onFuelUsageEvent(JetpackEvent event);

    /**
     * This method should check if the player has adequate fuel, by checking
     * their inventory. Possible usages include economy integration, experience
     * integration, and so on. This shouldn't actually remove the fuel, or move
     * the player. These are handled by other events.
     * <p/>
     * This should also inform the user if they don't have any/not enough fuel.
     *
     * @param event The event
     * @return If the player has adequate fuel
     */
    public abstract boolean onFuelCheckEvent(JetpackEvent event);

    /**
     * The movement type of the Jetpack changes how the Jetpack is activated,
     * and how often.
     *
     * @return A type from the FlightTypes enum
     */
    public abstract FlightTypes getMovementType();

    /**
     * Generates a crafting recipe that this Jetpack is crafted using. This is
     * used by the JetpackManager to integrate the Jetpack with Bukkit.
     *
     * @return A crafting recipe
     */
    public abstract CraftingRecipe getCraftingRecipe();

    /**
     * Gets the slot that this item belongs to. The material should be able to
     * be put into this slot, but this won't be checked.
     *
     * @return A Slot from the Jetpack class, representing a slot
     */
    public abstract Jetpack.Slot getSlot();

    /**
     * A convenience method to create a ItemStack from the provided Material and
     * data values. This is used internally, but can also be used and overridden
     * by external plugins.
     *
     * @param amount How many Jetpacks should this return
     * @return A ItemStack, containing the Material, data, name and description
     * of the Jetpack
     */
    public ItemStack getItem(int amount) {
        ItemStack item = new ItemStack(getMaterial(), amount, getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName());
        meta.setLore(Utils.convertToList(getDescription()));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Refer to getItem(amount). This simply obtains 1 Jetpack.
     *
     * @return A ItemStack, containing the Material, data, name and description
     * of the Jetpack
     */
    public ItemStack getItem() {
        return getItem(1);
    }

    /**
     * Defines the permission required to use this Jetpack
     *
     * @return A permission path
     */
    public String getPermission() {
        return "easyjetpack." + getGiveName();
    }

    /**
     * A simple method to return the configuration from the main class
     *
     * @return The configuration FileConfigration
     */
    public FileConfiguration getConfig() {
        return EasyJetpack.getInstance().getConfig();
    }

    /**
     * Checks if repairing items is allowed
     */
    public abstract boolean isRepairingDisabled();

    /**
     * Keep calling this jetpack until it runs out of fuel.
     *
     * @return Should this jetpack be placed on a timer?
     */
    public boolean keepCalling() {
        return false;
    }

    /**
     * A representation of the slot's that the player can have
     *
     * @author James
     */
    public enum Slot {
        HELMET, CHESTPLATE, LEGGINGS, BOOTS, HELD_ITEM
    }
}

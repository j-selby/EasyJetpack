package net.jselby.ej.api;

import net.jselby.ej.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * A event thrown when a player attempts to activate a Jetpack
 *
 * @author James
 */
public class JetpackEvent implements Cancellable {
    private Player player;
    private FlightTypes type;
    private boolean cancelled;
    private ItemStack item;
    private Event baseEvent;

    /**
     * Creates a jetpack event, assuming that the provided player should be
     * thrown up
     *
     * @param player The player in question
     * @param type   The type of event
     */
    public JetpackEvent(Player player, FlightTypes type, ItemStack item, Event baseEvent) {
        this.player = player;
        this.type = type;
        this.item = item;
        this.baseEvent = baseEvent;
    }

    /**
     * Returns the player that belongs to this event
     *
     * @return A Bukkit player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the type of event that was called
     *
     * @return A FlightTypes enum type
     */
    public FlightTypes getType() {
        return type;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        cancelled = isCancelled;
    }

    /**
     * Checks if the player is wearing a certain
     * Jetpack.
     *
     * @param jetpackClass The Jetpack to check
     * @return If the player is wearing the Jetpack, or false if the Jetpack doesn't exist.
     */
    public boolean isPlayerWearing(Class<? extends Jetpack> jetpackClass) {
        Jetpack pack = EasyJetpackAPI.getManager().getJetpackByClass(jetpackClass);
        return pack != null && Utils.isItemStackEqual(getPlayer()
                .getInventory().getChestplate(), pack.getItem());
    }

    /**
     * Returns the item that this event belongs to
     *
     * @return The item associated with this event
     */
    public ItemStack getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "JetpackEvent{Item:" + getItem() +
                ", Cancelled:" + isCancelled() +
                ", FlightType:" + getType() +
                ",Player:" + getPlayer() + "}";
    }

    public Event getBaseEvent() {
        return baseEvent;
    }
}

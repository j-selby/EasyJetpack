package me.paulvogel.easyjetpack;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages Jetpacks, and calls the Jetpack events when they are
 * required. Plugins should register their custom jetpacks here.
 *
 * @author James
 */
public class JetpackManager {

    private EasyJetpack plugin;
    private ArrayList<Jetpack> jetpacks;
    private ArrayList<Jetpack> jetpacksTimered;
    private HashMap<Runnable, Integer> runnables;
    private ArrayList<String> crouchingArray;

    /**
     * A internally used constructor to create a JetpackManager instance.
     *
     * @param plugin The main Jetpack plugin
     */
    JetpackManager(EasyJetpack plugin) {
        this.plugin = plugin;

        jetpacks = new ArrayList<Jetpack>();
        runnables = new HashMap<Runnable, Integer>();
        crouchingArray = new ArrayList<String>();
        jetpacksTimered = new ArrayList<Jetpack>();

        this.plugin.getServer().getPluginManager()
                .registerEvents(new JetpackListener(), this.plugin);
    }

    /**
     * Registers a Jetpack. It can now be created
     *
     * @param jetpack The Jetpack to add
     */
    public void addJetpack(Jetpack jetpack) {
        jetpacks.add(jetpack);
        CraftingRecipe recipe = jetpack.getCraftingRecipe();
        if (recipe != null) {
            recipe.register();
        }
    }

    /**
     * De-registers a Jetpack. Its custom events will no longer be invoked.
     *
     * @param jetpack The Jetpack to remove
     */
    public void removeJetpack(Jetpack jetpack) {
        jetpacks.remove(jetpack);
    }

    /**
     * Method called either internally or externally that passes the event onto
     * the Jetpack which the player is wearing
     *
     * @param event A JetpackEvent containing a player who triggered the event
     * @return Whether the event succeeded
     */
    public boolean onJetpackEvent(final JetpackEvent event) {
        for (final Jetpack next : jetpacks) {
            // If this is a anvil event, disable it.
            if (event.getType() == FlightTypes.ANVIL
                    && Utils.isItemStackEqual(next.getItem(), event.getItem())
                    && next.isRepairingDisabled()) {
                event.setCancelled(true);
                return false;
            }

            // Check the movement types to see if they match up.
            if ((next.getMovementType() == event.getType() ||
                    (next.getMovementType() == FlightTypes.CROUCH_CONSTANT && event.getType() == FlightTypes.CROUCH) ||
                    (next.keepCalling() && event.getType() == FlightTypes.TIMER))
                    && Utils.isItemStackEqual(next.getItem(),
                    Utils.getSlot(event.getPlayer(), next.getSlot()))) {

                // If the player doesn't have permission to use the jetpack, don't let them.
                if (!event.getPlayer().hasPermission(next.getPermission())) {
                    event.getPlayer()
                            .sendMessage(
                                    ChatColor.RED
                                            + "You do not have permission to use this jetpack.");
                    return false;
                }

                // If this was triggered via a constant crouch event, call the normal event.
                if (next.getMovementType() == FlightTypes.CROUCH_CONSTANT
                        && event.getType() == FlightTypes.CROUCH && !jetpacksTimered.contains(next)) {
                    if (next.keepCalling()) {
                        jetpacksTimered.add(next);
                    }

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (isCrouching(event.getPlayer()) || next.keepCalling()) {
                                boolean success = onJetpackEvent(new JetpackEvent(
                                        event.getPlayer(),
                                        isCrouching(event.getPlayer()) ?
                                                FlightTypes.CROUCH_CONSTANT : FlightTypes.TIMER,
                                        event.getItem(), null));
                                if (!success) {
                                    Bukkit.getScheduler().cancelTask(
                                            runnables.get(this));
                                    if (jetpacksTimered.contains(next)) {
                                        jetpacksTimered.remove(next);
                                    }
                                }
                            } else {
                                Bukkit.getScheduler().cancelTask(
                                        runnables.get(this));

                                if (jetpacksTimered.contains(next)) {
                                    jetpacksTimered.remove(next);
                                }
                            }
                        }
                    };

                    int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(
                            plugin, runnable, 1, 1);
                    runnables.put(runnable, id);
                    return true;
                } else {
                    boolean hasFuel = next.onFuelCheckEvent(event);
                    if (hasFuel) {
                        next.onFuelUsageEvent(event);
                        next.onFlyEvent(event);
                    }
                    return hasFuel;
                }
            }
        }

        return false;
    }

    public boolean isCrouching(Player player) {
        return crouchingArray.contains(player.getName());
    }

    public void setCrouching(Player player, boolean crouching) {
        if (crouching) {
            crouchingArray.add(player.getName());
        } else {
            crouchingArray.remove(player.getName());
        }
    }

    /**
     * Finds a Jetpack by it's give name
     *
     * @param name The name of the Jetpack
     * @return A Jetpack, or null if one cannot be found
     */
    public Jetpack getJetpackByName(String name) {
        for (Jetpack next : jetpacks) {
            if (next.getGiveName().equalsIgnoreCase(name)) {
                return next;
            }
        }
        return null;
    }

    /**
     * Finds a Jetpack by it's class
     *
     * @param jetpackClass The class of the Jetpack
     * @return A Jetpack, or null if one cannot be found
     */
    public Jetpack getJetpackByClass(Class<? extends Jetpack> jetpackClass) {
        for (Jetpack next : jetpacks) {
            if (next.getClass().getName()
                    .equalsIgnoreCase(jetpackClass.getName())) {
                return next;
            }
        }
        return null;
    }

    /**
     * Obtains a array of all registered jetpacks
     *
     * @return A Jetpack array
     */
    public Jetpack[] getJetpacks() {
        return jetpacks.toArray(new Jetpack[jetpacks.size()]);
    }

    public boolean isJetpack(ItemStack item) {
        for (Jetpack next : jetpacks) {
            if (Utils.isItemStackEqual(next.getItem(), item)) {
                return true;
            }
        }
        return false;
    }
}

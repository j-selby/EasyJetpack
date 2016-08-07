package net.jselby.ej.impl;

import net.jselby.ej.Utils;
import net.jselby.ej.api.EasyJetpackAPI;
import net.jselby.ej.api.FlightTypes;
import net.jselby.ej.api.Jetpack;
import net.jselby.ej.api.JetpackEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The hover jetpack controller.
 *
 * @author j_Selby
 */
public class HoverJetpackController extends Jetpack {
    public static HashMap<String, Boolean> playersToTrigger = new HashMap<String, Boolean>();

    @Override
    public String getName() {
        return ChatColor.RESET + "" + ChatColor.BLUE + "Flight Controller";
    }

    @Override
    public String getGiveName() {
        return "hovercontroller";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                ChatColor.RESET + "Allows you to control movement while wearing",
                ChatColor.RESET + "a hover jetpack.",
                ChatColor.RESET + "Rightclick to go down, Shift to go up, left click",
                ChatColor.RESET + "to go forward."
        };
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public void onFlyEvent(JetpackEvent event) {
        Action action
                = ((PlayerInteractEvent) event.getBaseEvent()).getAction();
        boolean rightClick = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK);

        playersToTrigger.put(event.getPlayer().getName(), rightClick);
    }

    @Override
    public void onFuelUsageEvent(JetpackEvent event) {

    }

    @Override
    public boolean onFuelCheckEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)
                && !Utils.playerHasFuel(event.getPlayer())) {
            return false;
        }

        return event.isPlayerWearing(HoverJetpack.class);
    }

    @Override
    public FlightTypes getMovementType() {
        return FlightTypes.INTERACT;
    }

    @Override
    public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.hover.craftable", true)) {
            return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(1, Material.DIAMOND);
        recipe.setSlot(3, Material.ENDER_PEARL);
        recipe.setSlot(4, Material.BLAZE_ROD);
        recipe.setSlot(5, Material.ENDER_PEARL);
        return recipe;
    }

    @Override
    public Slot getSlot() {
        return Slot.HELD_ITEM;
    }

    @Override
    public boolean isRepairingDisabled() {
        return true;
    }
}

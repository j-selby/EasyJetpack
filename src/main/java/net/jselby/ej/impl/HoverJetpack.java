package net.jselby.ej.impl;

import net.jselby.ej.Utils;
import net.jselby.ej.VisualCandy;
import net.jselby.ej.api.EasyJetpackAPI;
import net.jselby.ej.api.FlightTypes;
import net.jselby.ej.api.Jetpack;
import net.jselby.ej.api.JetpackEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * A hovering jetpack - similar to the burst one, but gives
 * you alot more control.
 *
 * @author j_selby
 */
public class HoverJetpack extends Jetpack {
    @Override
    public String getName() {
        return ChatColor.RESET + "" + ChatColor.BLUE + "Hover Jetpack";
    }

    @Override
    public String getGiveName() {
        return "hover";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                ChatColor.RESET + "Are you a avid builder, or someone who",
                ChatColor.RESET + "likes suspending themselves in thin air?",
                ChatColor.RESET + "Then this jetpack is for you!",
                ChatColor.RESET + "Best of all, this jetpack "
                        + ChatColor.ITALIC + "doesn't" + ChatColor.RESET + " break!"
        };
    }

    @Override
    public Material getMaterial() {
        return Material.getMaterial(getConfig().getString(
                "jetpacks.teleport.material", "CHAINMAIL_CHESTPLATE"));
    }

    @Override
    public void onFlyEvent(JetpackEvent event) {
        Vector dir = event.getPlayer().getLocation().getDirection();
        double y = event.getPlayer().getVelocity().getY();
        if (y < 0.3D) {
            y = 0.3D;
        }
        y *= 1.3D;
        if (y > 10) {
            y = 10;
        }

        double verticalSpeed = 0.1;
        double horSpeed = 0.0;
        double multiplier = 0.5D;
        boolean fixFly = false;
        if (event.getType() == FlightTypes.TIMER) {
            verticalSpeed = 0;
            multiplier = 0;
            horSpeed = 0;
            fixFly = true;

            // Check if they are using the fly controller
            if (HoverJetpackController.playersToTrigger.containsKey(event.getPlayer().getName())) {
                fixFly = false;
                // They are!
                if (HoverJetpackController.playersToTrigger
                        .get(event.getPlayer().getName())) {
                    // Rightclick
                    verticalSpeed = 0.1;
                    y = -0.4;
                } else {
                    // Leftclick
                    multiplier = 0.5D;
                    horSpeed = 0.4;
                }

                HoverJetpackController.playersToTrigger.remove(event.getPlayer().getName());
            }
        }

        Vector vector = Utils.addVector(event.getPlayer(), new Vector(
                        dir.getX() * multiplier, y, dir.getZ() * multiplier),
                horSpeed, verticalSpeed,
                horSpeed);
        if (fixFly) {
            vector.setY(0);
        }
        event.getPlayer().setVelocity(
                vector);

        VisualCandy.jetpackEffect(event.getPlayer());
    }

    @Override
    public void onFuelUsageEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 24);
        }
    }

    @Override
    public boolean onFuelCheckEvent(JetpackEvent event) {
        // Make sure they aren't on the ground, if this is a timer event.
        if (event.getType() == FlightTypes.TIMER && event.getPlayer().isOnGround()) {
            return false;
        }

        // The timer event should be cancelled if the player is crouching
        if (event.getType() == FlightTypes.TIMER
                && EasyJetpackAPI.getManager().isCrouching(event.getPlayer())) {
            return false;
        }


        if (getConfig().getBoolean("fuel.enabled", true)) {

            boolean containsCoal = Utils.playerHasFuel(event.getPlayer());
            if (!containsCoal)
                event.getPlayer().sendMessage(
                        ChatColor.RED + "You don't have enough fuel!");
            return containsCoal;
        }
        return true;
    }

    @Override
    public FlightTypes getMovementType() {
        return FlightTypes.CROUCH_CONSTANT;
    }

    @Override
    public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.hover.craftable", true)) {
            return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(0, Material.REDSTONE);
        recipe.setSlot(3, Material.ENDER_PEARL);
        recipe.setSlot(6, Material.REDSTONE);

        recipe.setSlot(1, Material.DIAMOND);
        recipe.setSlot(4, Material.GOLD_CHESTPLATE);
        recipe.setSlot(7, Material.FURNACE);

        recipe.setSlot(2, Material.REDSTONE);
        recipe.setSlot(5, Material.REDSTONE);
        recipe.setSlot(8, Material.REDSTONE);
        return recipe;
    }

    @Override
    public Slot getSlot() {
        return Slot.CHESTPLATE;
    }

    @Override
    public boolean isRepairingDisabled() {
        return true;
    }

    @Override
    public boolean keepCalling() {
        // The hover effect still needs to work.
        return true;
    }
}

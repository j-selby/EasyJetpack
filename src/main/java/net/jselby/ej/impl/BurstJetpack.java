package net.jselby.ej.impl;

import net.jselby.ej.Utils;
import net.jselby.ej.VisualCandy;
import net.jselby.ej.api.FlightTypes;
import net.jselby.ej.api.Jetpack;
import net.jselby.ej.api.JetpackEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * A Jetpack built for speed. Multiple bursts are allowed by the Jetpack
 *
 * @author James
 */
public class BurstJetpack extends Jetpack {

    @Override
    public String getName() {
        return ChatColor.RESET + "" + ChatColor.BLUE + "Burst Jetpack";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                ChatColor.RESET + "Need to climb into the atmosphere",
                ChatColor.RESET + "at great speeds? The burst jetpack",
                ChatColor.RESET + "is for you!"};
    }

    @Override
    public Material getMaterial() {
        return Material.getMaterial(getConfig().getString(
                "jetpacks.burst.material", "GOLD_CHESTPLATE"));
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
        event.getPlayer().setVelocity(
                Utils.addVector(event.getPlayer(), new Vector(
                                dir.getX() * 0.5D, y, dir.getZ() * 0.5D), 0.7, 0.6,
                        0.7));

        VisualCandy.jetpackEffect(event.getPlayer());
    }

    @Override
    public void onFuelUsageEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 4);
        }
        if (getConfig().getBoolean("jetpacks.burst.durability", true))
            Utils.damage(event.getPlayer(), getSlot(), 250);
    }

    @Override
    public boolean onFuelCheckEvent(JetpackEvent event) {
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
        if (!getConfig().getBoolean("jetpacks.burst.craftable", true)) {
            return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(0, Material.REDSTONE);
        recipe.setSlot(3, Material.REDSTONE);
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
    public String getGiveName() {
        return "burst";
    }

    @Override
    public Slot getSlot() {
        return Slot.CHESTPLATE;
    }

    @Override
    public boolean isRepairingDisabled() {
        return getConfig().getBoolean("jetpacks.burst.antianvil", true);
    }
}

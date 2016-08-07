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
 * A legacy Jetpack from a older version of the plugin. Also maintains
 * compatibility with it.
 *
 * @author James
 */
public class TraditionalJetpack extends Jetpack {

    @Override
    public String getName() {
        return ChatColor.RESET + "" + ChatColor.DARK_RED + "Jetpack";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                ChatColor.RESET + "Fly away in the Jetpack 3000!",
                ChatColor.RESET + "Limited time only! Only $9.95",
                ChatColor.RESET + "at your local black market."};
    }

    @Override
    public Material getMaterial() {
        return Material.getMaterial(getConfig().getString(
                "jetpacks.traditional.material", "GOLD_CHESTPLATE"));
    }

    @Override
    public void onFlyEvent(JetpackEvent event) {
        Vector dir = event.getPlayer().getLocation().getDirection();
        event.getPlayer().setVelocity(
                Utils.addVector(event.getPlayer(), new Vector(
                        dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D), 0.45, 0.6, 0.45));

        VisualCandy.jetpackEffect(event.getPlayer());
    }

    @Override
    public void onFuelUsageEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 1);
        }
        if (getConfig().getBoolean("jetpacks.traditional.durability", true))
            Utils.damage(event.getPlayer(), getSlot(), 150);
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
        return FlightTypes.CROUCH;
    }

    @Override
    public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.traditional.craftable", true)) {
            return null;
        }

        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(1, Material.DIAMOND);
        recipe.setSlot(4, Material.GOLD_CHESTPLATE);
        recipe.setSlot(7, Material.FURNACE);
        return recipe;
    }

    @Override
    public String getGiveName() {
        return "standard";
    }

    @Override
    public Slot getSlot() {
        return Slot.CHESTPLATE;
    }

    @Override
    public boolean isRepairingDisabled() {
        return getConfig().getBoolean("jetpacks.traditional.antianvil", true);
    }
}

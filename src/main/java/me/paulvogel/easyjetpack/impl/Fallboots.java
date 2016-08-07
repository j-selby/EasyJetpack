package me.paulvogel.easyjetpack.impl;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import darknesgaming.jetpacks.EntryJetpack;
import darknesgaming.jetpacks.InfinityJetpack;

/**
 * Traditional Fall-Boots from plugin versions past. Fully compatible with them
 * as well.
 *
 * @author James
 */
public class Fallboots extends Jetpack {

    @Override
    public String getName() {
        return ChatColor.RESET + "" + ChatColor.DARK_RED + "Fall Boots";
    }

    @Override
    public String getGiveName() {
        return "fallboots";
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                ChatColor.RESET + "Direct from Aperture Science!",
                ChatColor.RESET + "Outlawed in 170 countries!"};
    }

    @Override
    public Material getMaterial() {
        return Material.getMaterial(getConfig().getString(
                "jetpacks.boots.material", "LEATHER_BOOTS"));
    }

    @Override
    public void onFlyEvent(JetpackEvent event) {
        if (getConfig().getBoolean("jetpacks.boots.jetpackrequired", false)) {
            if (!event.isPlayerWearing(TraditionalJetpack.class)
                    && !event.isPlayerWearing(TeleportJetpack.class)
                    && !event.isPlayerWearing(BurstJetpack.class)
                    && !event.isPlayerWearing(EntryJetpack.class)
                    && !event.isPlayerWearing(InfinityJetpack.class)) {
                return;
            }
        }
        event.setCancelled(true);
    }

    @Override
    public void onFuelUsageEvent(JetpackEvent event) {
    }

    @Override
    public boolean onFuelCheckEvent(JetpackEvent event) {
        return true;
    }

    @Override
    public FlightTypes getMovementType() {
        return FlightTypes.FALLING;
    }

    @Override
    public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.boots.craftable", true)) {
            return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(6, Material.FEATHER);
        recipe.setSlot(7, Material.LEATHER_BOOTS);
        recipe.setSlot(8, Material.FEATHER);
        return recipe;
    }

    @Override
    public Slot getSlot() {
        return Slot.BOOTS;
    }

    @Override
    public boolean isRepairingDisabled() {
        return getConfig().getBoolean("jetpacks.boots.antianvil", true);
    }
}

package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import me.paulvogel.easyjetpack.Utils;
import me.paulvogel.easyjetpack.VisualCandy;
import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

/**
 * The entry jetpack, built by Infinity Laboratories.
 * 
 * Sold by Epiccraft Industries.
 * 
 * @author DarknesGaming
 */
public class EntryJetpack extends Jetpack{

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.DARK_BLUE + "Entry Jetpack";
	}

	@Override
	public String getGiveName() {
		return "entry";
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				ChatColor.RESET + "Epiccraft Industries introduces",
				ChatColor.RESET + "the cheapest jetpack in the entire",
				ChatColor.RESET + "world! This should NOT be used for",
				ChatColor.RESET + "everyday use."};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.entry.material", "LEATHER_CHESTPLATE"));
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
            Utils.useFuel(event.getPlayer(), false, 0.2);
        }
        if (getConfig().getBoolean("jetpacks.entry.durability", true))
            Utils.damage(event.getPlayer(), getSlot(), 100);
		
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
        if (!getConfig().getBoolean("jetpacks.entry.craftable", true)) {
        	return null;
        }
        
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(1, Material.COBBLESTONE);
        recipe.setSlot(4, Material.LEATHER_CHESTPLATE);
        recipe.setSlot(7, Material.FURNACE);
		return recipe;
	}

	@Override
	public Slot getSlot() {
		return Slot.CHESTPLATE;
	}

	@Override
	public boolean isRepairingDisabled() {
		return getConfig().getBoolean("jetpacks.entry.antianvil", true);
	}

}

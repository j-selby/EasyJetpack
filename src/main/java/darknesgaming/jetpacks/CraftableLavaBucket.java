package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

/**
 * 
 * The Craftable Lava Bucket, brought to you by Infinity Laboratories.
 * 
 * @author DarknesGaming
 * 
 */
public class CraftableLavaBucket extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "Lava Bucket";
	}

	@Override
	public String getGiveName() {
		return "lavabucket";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.RESET + "The craftable lava bucket,",
				ChatColor.RESET + "created by Infinity Laboratories!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.lavabucket.material", "LAVA_BUCKET"));
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {

	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {

	}

	@Override
	public boolean onFuelCheckEvent(JetpackEvent event) {
		return false;
	}

	@Override
	public FlightTypes getMovementType() {
		return null;
	}

	@Override
	public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.lavabucket.craftable", true)) {
        	return null;
        };
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(1, Material.NETHERRACK);
        recipe.setSlot(4, Material.BUCKET);
        recipe.setSlot(7, Material.NETHERRACK);
        return recipe;
	}

	@Override
	public Slot getSlot() {
		return null;
	}

	@Override
	public boolean isRepairingDisabled() {
		return false;
	}
	public String getPermission() {
		return "easyjetpacks." + getGiveName();
	}

}

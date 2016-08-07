package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

/**
 * 
 * The secret emerald recipe, created by Infinity Laboratories.
 * 
 * @author DarknesGaming
 *
 */
public class SecretItem2 extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "Emerald";
	}

	@Override
	public String getGiveName() {
		return "secretitem2";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.RESET + "You just discovered the secret",
				ChatColor.RESET + "Emerald recipe, created by",
				ChatColor.RESET + "Infinity Laboratories!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.secretitem2.material", "EMERALD"));
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
		if (!getConfig().getBoolean("jetpacks.secretitem2.craftable", true)) {
			return null;
		}
		CraftingRecipe recipe = new CraftingRecipe(getItem(5));
		recipe.setSlot(1, Material.DIAMOND);
		recipe.setSlot(4, Material.DIAMOND);
		recipe.setSlot(7, Material.DIAMOND);
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

}

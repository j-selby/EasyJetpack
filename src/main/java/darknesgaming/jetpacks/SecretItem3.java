package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

public class SecretItem3 extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "Nether Star";
	}

	@Override
	public String getGiveName() {
		return "secretitem3";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.RESET + "You just discovered the secret",
				ChatColor.RESET + "nether star recipe, created by",
				ChatColor.RESET + "Infinity Laboratories!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.secretitem3.material", "NETHER_STAR"));
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
        if (!getConfig().getBoolean("jetpacks.secretitem3.craftable", true)) {
            return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(0, Material.IRON_INGOT);
        recipe.setSlot(3, Material.DIAMOND);
        recipe.setSlot(6, Material.EMERALD);
        
        recipe.setSlot(1, Material.DIAMOND);
        recipe.setSlot(4, Material.DIAMOND);
        recipe.setSlot(7, Material.DIAMOND);
        
        recipe.setSlot(2, Material.IRON_INGOT);
        recipe.setSlot(5, Material.DIAMOND);
        recipe.setSlot(8, Material.EMERALD);
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
		return "easyjetpack." + getGiveName();
	}

}

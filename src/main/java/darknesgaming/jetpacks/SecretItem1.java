package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

/**
 * 
 * The secret diamond block recipe, created by Infinity Laboratories.
 * 
 * Only to be used for jetpack construction.
 * 
 * @author DarknesGaming
 *
 */
public class SecretItem1 extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "Diamond Block";
	}

	@Override
	public String getGiveName() {
		return "secretitem1";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.RESET + "You just discovered a secret",
				ChatColor.RESET + "diamond block recipe, created",
				ChatColor.RESET + "by Infinity Laboratories!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.secretitem1.material", "DIAMOND_BLOCK"));
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
        if (!getConfig().getBoolean("jetpacks.secretitem1.craftable", true)) {
            return null;
        }
        
        CraftingRecipe recipe = new CraftingRecipe(getItem(10));
        recipe.setSlot(0, Material.COBBLESTONE);
        recipe.setSlot(1, Material.COBBLESTONE);
        recipe.setSlot(2, Material.COBBLESTONE);
        recipe.setSlot(3, Material.DIAMOND);
        recipe.setSlot(4, Material.NETHER_STAR);
        recipe.setSlot(5, Material.DIAMOND);
        recipe.setSlot(6, Material.COBBLESTONE);
        recipe.setSlot(7, Material.COBBLESTONE);
        recipe.setSlot(8, Material.COBBLESTONE);
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

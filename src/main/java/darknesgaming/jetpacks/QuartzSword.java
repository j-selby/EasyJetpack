package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

public class QuartzSword extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "Quartz Sword";
	}

	@Override
	public String getGiveName() {
		return "quartzsword";
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFuelCheckEvent(JetpackEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FlightTypes getMovementType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CraftingRecipe getCraftingRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Slot getSlot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRepairingDisabled() {
		// TODO Auto-generated method stub
		return false;
	}

}

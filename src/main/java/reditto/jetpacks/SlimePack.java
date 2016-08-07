package reditto.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import me.paulvogel.easyjetpack.Utils;
import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

public class SlimePack extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.DARK_GREEN + "Slime Jetpack";
	}

	@Override
	public String getGiveName() {
		return "slimepack";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.GREEN + "This is RedTVB's Slime Jetpack",
				ChatColor.GREEN + "a custom jetpack that he created.",
				ChatColor.GREEN + "Given this was just a prototype,",
				ChatColor.GREEN + "this jetpack breaks easily."
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.slimepack.material", "LEATHER_CHESTPLATE"));
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
        Vector dir = event.getPlayer().getLocation().getDirection();
        event.getPlayer().setVelocity(
                Utils.addVector(event.getPlayer(), new Vector(
                        dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D), 0.45, 0.6, 0.45));

        SlimeParticles.jetpackEffect(event.getPlayer());
	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 0.01);
        }
        if (getConfig().getBoolean("jetpacks.entry.durability", true))
            Utils.damage(event.getPlayer(), getSlot(), 30);
		

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
        if (!getConfig().getBoolean("jetpacks.slimepack.craftable", true)) {
        	return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(1, Material.SLIME_BALL);
        recipe.setSlot(3, Material.SLIME_BALL);
        recipe.setSlot(4, Material.LEATHER_CHESTPLATE);
        recipe.setSlot(5, Material.SLIME_BALL);
        recipe.setSlot(6, Material.FURNACE);
		return recipe;
	}

	@Override
	public Slot getSlot() {
		return Slot.CHESTPLATE;
	}

	@Override
	public boolean isRepairingDisabled() {
		return false;
	}

}

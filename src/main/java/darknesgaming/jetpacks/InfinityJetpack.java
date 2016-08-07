package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import me.paulvogel.easyjetpack.Utils;
import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

/**
 * 
 * The official jetpack from Infinity Laboratories.
 * 
 * This jetpack is worth $18000 in the game.
 * 
 * @author DarknesGaming
 *
 */
public class InfinityJetpack extends Jetpack{

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.GREEN + "Infinity Jetpack";
	}

	@Override
	public String getGiveName() {
		return "infinity";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.YELLOW + "The official Infinity Jetpack",
				ChatColor.YELLOW + "from Infinity Laboratories!",
				ChatColor.YELLOW + "This jetpack is indestructable",
				ChatColor.YELLOW + "under most circumstances!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.infinity.material", "DIAMOND_CHESTPLATE"));
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
        Vector dir = event.getPlayer().getLocation().getDirection();
        event.getPlayer().setVelocity(
                Utils.addVector(event.getPlayer(), new Vector(
                        dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D), 0.45, 0.6, 0.45));

        InfinityParticles.jetpackEffect(event.getPlayer());
		
	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 10);
        }
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
        if (!getConfig().getBoolean("jetpacks.infinity.craftable", true)) {
        	return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(0, Material.EMERALD);
        recipe.setSlot(3, Material.DIAMOND);
        recipe.setSlot(6, Material.REDSTONE);
        
        recipe.setSlot(1, Material.DIAMOND);
        recipe.setSlot(4, Material.NETHER_STAR);
        recipe.setSlot(7, Material.FURNACE);
        
        recipe.setSlot(2, Material.EMERALD);
        recipe.setSlot(5, Material.DIAMOND);
        recipe.setSlot(8, Material.REDSTONE);
        return recipe;
	}

	@Override
	public Slot getSlot() {
		return Slot.CHESTPLATE;
	}

	@Override
	public boolean isRepairingDisabled() {
		// TODO Auto-generated method stub
		return getConfig().getBoolean("jetpacks.infinity.antianvil", true);
	}
	public String getPermission() {
		return "easyjetpack." + getGiveName();
	}

}

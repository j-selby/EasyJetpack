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
import me.paulvogel.easyjetpack.impl.HoverJetpackController;

public class RocketBoots extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.GREEN + "Rocket Boots";
	}

	@Override
	public String getGiveName() {
		return "rocketboots";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.RED + "The world's first rocket boots are",
				ChatColor.RED + "here! Created by Infinity Laboratories,",
				ChatColor.RED + "this is a cheaper alternative to the",
				ChatColor.RED + "rocket jetpacks in the market!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.rocketboots.material", "GOLD_BOOTS"));
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
	       Vector dir = event.getPlayer().getLocation().getDirection();
	        double y = event.getPlayer().getVelocity().getY();
	        if (y < 0.3D) {
	            y = 0.3D;
	        }
	        y *= 1.3D;
	        if (y > 10) {
	            y = 10;
	        }

	        double verticalSpeed = 0.1;
	        double horSpeed = 0.0;
	        double multiplier = 0.5D;
	        boolean fixFly = false;
	        if (event.getType() == FlightTypes.TIMER) {
	            verticalSpeed = 0;
	            multiplier = 0;
	            horSpeed = 0;
	            fixFly = true;

	            // Check if they are using the fly controller
	            if (HoverJetpackController.playersToTrigger.containsKey(event.getPlayer().getName())) {
	                fixFly = false;
	                // They are!
	                if (HoverJetpackController.playersToTrigger
	                        .get(event.getPlayer().getName())) {
	                    // Right click
	                    verticalSpeed = 0.1;
	                    y = -0.4;
	                } else {
	                    // Left click
	                    multiplier = 0.5D;
	                    horSpeed = 0.4;
	                }

	                HoverJetpackController.playersToTrigger.remove(event.getPlayer().getName());
	            }
	        }

	        Vector vector = Utils.addVector(event.getPlayer(), new Vector(
	                        dir.getX() * multiplier, y, dir.getZ() * multiplier),
	                horSpeed, verticalSpeed,
	                horSpeed);
	        if (fixFly) {
	            vector.setY(0);
	        }
	        event.getPlayer().setVelocity(
	                vector);

	        VisualCandy.jetpackEffect(event.getPlayer());
	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 5);
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
        if (!getConfig().getBoolean("jetpacks.slimepack.craftable", true)) {
        	return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(1, Material.IRON_BOOTS);
        recipe.setSlot(4, Material.LEATHER_BOOTS);
        recipe.setSlot(7, Material.FURNACE);
        return null;
	}

	@Override
	public Slot getSlot() {
		return Slot.BOOTS;
	}

	@Override
	public boolean isRepairingDisabled() {
		return false;
	}

}

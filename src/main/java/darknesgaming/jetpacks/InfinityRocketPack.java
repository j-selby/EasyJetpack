package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import me.paulvogel.easyjetpack.Utils;
import me.paulvogel.easyjetpack.api.EasyJetpackAPI;
import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;
import me.paulvogel.easyjetpack.impl.HoverJetpackController;

/**
 * 
 * The Infinity Rocket Jetpack, the upgraded version of the Rocket Jetpack.
 * 
 * Created by Infinity Laboratories.
 * 
 * @author DarknesGaming
 *
 */
public class InfinityRocketPack extends Jetpack{
    @Override
    public String getName() {
        return ChatColor.RESET + "" + ChatColor.GREEN + "Infinity Rocket Pack";
    }

    @Override
    public String getGiveName() {
        return "infinityrocket";
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                ChatColor.GREEN + "Infinity Laboratories" + ChatColor.RESET + " decided to take the",
                ChatColor.RESET + "rocket jetpack and make it " + ChatColor.DARK_RED + "MORE" + ChatColor.RESET + " powerful,",
                ChatColor.RESET + "and more user-friendly! And yet,",
                ChatColor.RESET + "this jetpack still "
                        + ChatColor.ITALIC + "doesn't" + ChatColor.RESET + " break!"
        };
    }

    @Override
    public Material getMaterial() {
        return Material.getMaterial(getConfig().getString(
                "jetpacks.infinityrocket.material", "CHAINMAIL_CHESTPLATE"));
    }

    @Override
    public void onFlyEvent(JetpackEvent event) {
        Vector dir = event.getPlayer().getLocation().getDirection();
        double y = event.getPlayer().getVelocity().getY();
        if (y < 1.3D) {
            y = 1.3D;
        }
        y *= 1.3D;
        if (y > 40) {
            y = 40;
        }

        double verticalSpeed = 7.0;
        double horSpeed = 0.0;
        double multiplier = 1.5D;
        boolean fixFly = false;
        if (event.getType() == FlightTypes.TIMER) {
            verticalSpeed = 5;
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
                    verticalSpeed = 0.9;
                    y = -0.1;
                } else {
                    // Left click
                    multiplier = 1.0D;
                    horSpeed = 0.8;
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

        InfinityParticles.jetpackEffect(event.getPlayer());
    }

    @Override
    public void onFuelUsageEvent(JetpackEvent event) {
        if (getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 100);
        }
    }

    @SuppressWarnings("deprecation")
	@Override
    public boolean onFuelCheckEvent(JetpackEvent event) {
        // Make sure they aren't on the ground, if this is a timer event.
        if (event.getType() == FlightTypes.TIMER && event.getPlayer().isOnGround()) {
            return false;
        }

        // The timer event should be cancelled if the player is crouching
        if (event.getType() == FlightTypes.TIMER
                && EasyJetpackAPI.getManager().isCrouching(event.getPlayer())) {
            return false;
        }


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
        return FlightTypes.CROUCH_CONSTANT;
    }

    @Override
    public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.infinityrocket.craftable", true)) {
            return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(0, Material.EMERALD);
        recipe.setSlot(3, Material.ENDER_PEARL);
        recipe.setSlot(6, Material.EMERALD);

        recipe.setSlot(1, Material.DIAMOND);
        recipe.setSlot(4, Material.DIAMOND_CHESTPLATE);
        recipe.setSlot(7, Material.BEACON);

        recipe.setSlot(2, Material.EMERALD);
        recipe.setSlot(5, Material.ENDER_PEARL);
        recipe.setSlot(8, Material.EMERALD);
        return recipe;
    }

    @Override
    public Slot getSlot() {
        return Slot.CHESTPLATE;
    }

    @Override
    public boolean isRepairingDisabled() {
        return true;
    }

    @Override
    public boolean keepCalling() {
        // The hover effect still needs to work.
        return true;
    }
}

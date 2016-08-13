package net.jselby.ej.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Several utilities to help with Jetpacks and players.
 */
public class PlayerInteractionUtils {
    private PlayerInteractionUtils() {}

    /**
     * Moves a player around.
     *
     * @param p    The player to adjust
     * @param v    The vector to add
     * @param maxX Maximum speed on the X axis
     * @param maxY Maximum speed on the Y axis
     * @param maxZ Maximum speed on the Z axis
     * @return The new vector.
     */
    public static Vector addVector(Player p, Vector v, double maxX, double maxY, double maxZ) {
        Vector curr = p.getVelocity();
        Vector added = curr.add(v);
        if (added.getX() > maxX) {
            added.setX(maxX);
        }
        if (added.getX() < -maxX) {
            added.setX(-maxX);
        }
        if (added.getY() > maxY) {
            added.setY(maxY);
        }
        if (added.getZ() > maxZ) {
            added.setZ(maxZ);
        }
        if (added.getZ() < -maxZ) {
            added.setZ(-maxZ);
        }
        added = added.normalize();
        return added;
    }

    /**
     * A method to compare two items, and see if they are similar
     *
     * @param item  The first item
     * @param item2 The second item
     * @return If the items are equal
     */
    public static boolean isItemStackEqual(ItemStack item, ItemStack item2) {
        return item != null
                && item2 != null
                && item.hasItemMeta()
                && item2.hasItemMeta()
                && item.getType().equals(item2.getType())
                && item.getItemMeta().hasDisplayName()
                && item2.getItemMeta().hasDisplayName()
                && item.getItemMeta().hasLore()
                && item2.getItemMeta().hasLore()
                && item.getItemMeta().getDisplayName()
                .equalsIgnoreCase(item2.getItemMeta().getDisplayName())
                && isArrayEqual(item.getItemMeta().getLore(), item2
                .getItemMeta().getLore());

    }

    /**
     * Checks if two lists have the same contents
     *
     * @param list  The first list
     * @param list2 The second list
     * @return If the lists are the same
     */
    public static boolean isArrayEqual(List<String> list, List<String> list2) {
        if (list == null || list2 == null) {
            return false;
        }
        try {
            Iterator<String> it = list.iterator();
            int count = 0;
            while (it.hasNext()) {
                String next = it.next();
                if (!list2.get(count).equalsIgnoreCase(next)) {
                    return false;
                }
                count++;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Uses fuel from the players inventory.
     *
     * @param player        The player to obtain the item from
     * @param mustBeHolding Should the coal be used from the players hand?
     * @param factor        The factor to kill the coal by
     * @return If the operation was a success
     */
    public static boolean useFuel(Player player, Material fuel, int durability,
                                  boolean mustBeHolding,
                                  double factor) {
        PlayerInventory inventory = player.getInventory();

        boolean isBurning = false;
        boolean isOffhand = false;
        ItemStack foundFuel = null;
        int fuelSlot = -1;

        // Locate burning fuel first
        if (mustBeHolding) {
            // They need to be holding the fuel for it to burn;
            // check if they are holding it.
            foundFuel = inventory.getItemInMainHand();
            if (foundFuel != null && foundFuel.getType() == fuel
                    && (durability == -1 || foundFuel.getDurability() == durability)) {
                // This is a sample of fuel, so we can now toy with it.
                // Check its metadata for the "% left" name
                isBurning = (foundFuel.hasItemMeta() && foundFuel.getItemMeta().hasLore() &&
                        foundFuel.getItemMeta().getLore().get(0).contains("% left"));
                fuelSlot = inventory.getHeldItemSlot();
            } else {
                // What just happened here? This isn't fuel!
                foundFuel = inventory.getItemInOffHand();
                if (foundFuel != null && foundFuel.getType() == fuel
                        && (durability == -1 || foundFuel.getDurability() == durability)) {
                    // This is a sample of fuel, so we can now toy with it.
                    // Check its metadata for the "% left" name
                    isBurning = (foundFuel.hasItemMeta() && foundFuel.getItemMeta().hasLore() &&
                            foundFuel.getItemMeta().getLore().get(0).contains("% left"));
                    fuelSlot = inventory.getHeldItemSlot();
                    isOffhand = true;
                } else {
                    // What just happened here? This isn't fuel!
                    return false;
                }
            }
        } else {
            // Check for burning fuel everywhere.
            ItemStack normalFuel = null;
            int normalFuelIndex = -1;

            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack checkItem = inventory.getItem(i);
                // Check this item for metadata.
                if (checkItem != null && checkItem.getType() == fuel
                        && checkItem.hasItemMeta()
                        && checkItem.getItemMeta().hasLore()
                        && checkItem.getItemMeta().getLore().get(0).contains("% left")
                        && (durability == -1 || checkItem.getDurability() == durability)) {
                    foundFuel = checkItem;
                    fuelSlot = i;
                    isBurning = true;
                    break;
                } else if (normalFuel == null && checkItem != null && checkItem.getType() == fuel
                        && (durability == -1 || checkItem.getDurability() == durability)) {
                    normalFuel = checkItem;
                    normalFuelIndex = i;
                }
            }

            if (foundFuel == null) {
                // Looks like we didn't find anything. Search for normal fuel.

                if (normalFuel == null) {
                    // This shouldn't happen...
                    return false;
                }

                fuelSlot = normalFuelIndex;
                foundFuel = normalFuel;
            }
        }

        // Fuel for us to respawn in the inventory at the end of the event
        int fuelToGiveBack = 0;
        // The new burning coal instance to manipulate.
        ItemStack burningFuel;
        // Do we need to spawn this burningFuel when we are done with it? (If it already
        // exists, no)
        boolean spawnBurningFuel = true;

        if (!isBurning) {
            // Burning + Non-burning fuel doesn't stack. Store it for later.
            fuelToGiveBack = foundFuel.getAmount() - 1;
            player.getInventory().remove(foundFuel);

            // Give us a instance to modify.
            burningFuel = new ItemStack(fuel, 1, foundFuel.getDurability());
            spawnBurningFuel = true;
        } else {
            // We should modify this instance instead.
            burningFuel = foundFuel;
        }

        // Now we need to calculate the fuel usage
        short fuelUsage;

        // If this coal isn't burning, give it a full life.
        if (!isBurning) {
            fuelUsage = 100;
        } else {
            // We need to dissect this from the metadata.
            String line = foundFuel.getItemMeta().getLore().get(0);

            fuelUsage = Short.parseShort(ChatColor.stripColor(line.split("%")[0]));
        }

        fuelUsage -= (((double) 100) / ((double) 10) / factor);

        // Compile the new metadata up.
        List<String> newLore = new ArrayList<String>();
        newLore.add(ChatColor.RESET + "" + fuelUsage + "% left");

        // Replace the burning coals lore
        ItemMeta newMeta = burningFuel.getItemMeta();
        newMeta.setLore(newLore);
        newMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_RED + "Burning "
                + fuel.name().toLowerCase() + " - " + fuelUsage + "% left");
        burningFuel.setItemMeta(newMeta);

        // If the fuel usage is less then 1, this fuel is extinguished.
        if (fuelUsage < 1) {
            // Delete it.
            if (mustBeHolding) {
                if (isOffhand) {
                    inventory.setItemInOffHand(new ItemStack(Material.AIR, 1));
                } else {
                    inventory.setItemInMainHand(new ItemStack(Material.AIR, 1));
                }
            } else {
                inventory.setItem(fuelSlot, new ItemStack(Material.AIR, 1));
            }

            // Shuffle their inventory.
            shuffleCoal(player, fuel, durability, mustBeHolding, isOffhand);
        } else {
            // Do we need to spawn the burningFuel in their inventory?
            if (spawnBurningFuel) {
                // Spawn it in the respective place.
                if (mustBeHolding) {
                    if (isOffhand) {
                        inventory.setItemInOffHand(burningFuel);
                    } else {
                        inventory.setItemInMainHand(burningFuel);
                    }
                } else {
                    inventory.setItem(fuelSlot, burningFuel);
                }
            }
        }

        if (fuelToGiveBack > 0) {
            player.getInventory().addItem(
                    new ItemStack(fuel,
                            fuelToGiveBack, foundFuel.getDurability()));
        }

        return true;
    }

    /**
     * Shuffles fuel around in the players inventory.
     *
     * @param player        The player to obtain the item from
     * @param mustBeHolding Should the coal end up in the players hand?
     */
    public static void shuffleCoal(Player player, Material fuelMaterial,
                                   int durability, boolean mustBeHolding, boolean isOffhand) {
        // First, make sure they are holding fuel.
        if (player.getInventory().contains(fuelMaterial)) {
            int position = -1;
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                ItemStack test = player.getInventory().getItem(i);
                if (test != null && test.getType() == fuelMaterial
                        && (durability == -1 || test.getDurability() == durability)) {
                    position = i;
                }
            }

            if (position != -1) {
                ItemStack stack = player.getInventory().getItem(position);
                player.getInventory().removeItem(stack);

                if (mustBeHolding) {
                    if (isOffhand) {
                        player.getInventory().setItemInOffHand(stack);
                    } else {
                        player.getInventory().setItemInMainHand(stack);
                    }
                } else {
                    player.getInventory().addItem(stack);
                }

                return;
            }
        }

        player.sendMessage(ChatColor.RED
                + "You have run out of "
                + fuelMaterial.name().toLowerCase() + "!");
    }
}

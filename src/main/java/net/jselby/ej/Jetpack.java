package net.jselby.ej;

import net.jselby.ej.data.ActionTypes;
import net.jselby.ej.data.CraftingRecipe;
import net.jselby.ej.data.JetpackTypes;
import net.jselby.ej.utils.PlayerInteractionUtils;
import net.jselby.ej.utils.VisualCandyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Jetpack is a singleton used to represent a modified item in the world.
 */
public class Jetpack {
    private static Pattern colorFilter = Pattern.compile("\\$(?<color>([A-Za-z_]*))\\$");

    private final HashMap<UUID, Integer> burstTimers = new HashMap<>();

    private final String name;
    private final String giveName;
    private final List<String> description;
    private final Material material;

    private final CraftingRecipe recipe;

    private final JetpackTypes itemType;
    private final ActionTypes actionType;
    private final int slot;
    private final float[] velocity;

    private final boolean jetpackEffect;
    private final boolean repairable;

    private final Material[] fuelTypes;

    /**
     * Creates a new Jetpack based upon a configuration file.
     * @param section The section of a configuration file to search.
     */
    public Jetpack(ConfigurationSection section) {
        // Parse configuration
        giveName = section.getName();

        // Name has special $COLOR$ params
        String rawName = ChatColor.RESET + section.getString("itemName", "Unnamed Jetpack");
        Matcher nameMatcher;
        while((nameMatcher = colorFilter.matcher(rawName)).find()) {
            rawName = nameMatcher.replaceFirst(ChatColor.valueOf(nameMatcher.group("color").toUpperCase()).toString());
        }
        name = rawName;

        // List has multiple entries, and can return null
        String rawDescription = section.getString("description", "");
        while((nameMatcher = colorFilter.matcher(rawDescription)).find()) {
            rawDescription = nameMatcher.replaceFirst(ChatColor.valueOf(nameMatcher.group("color").toUpperCase()).toString());
        }
        description = new ArrayList<>();
        Collections.addAll(description, rawDescription.split("\n"));
        description.replaceAll(s -> ChatColor.RESET + s);

        material = Material.valueOf(section.getString("material").toUpperCase().replace(" ", "_"));

        // Build recipe
        String rawRecipe = section.getString("recipe", "").trim();
        if (rawRecipe.length() != 0) {
            String[] recipeArgs = rawRecipe.replaceAll("  ", " ").split(" ");
            recipe = new CraftingRecipe(getItem());
            for (int i = 0; i < recipeArgs.length; i++) {
                String recipeArg = recipeArgs[i];
                if (!recipeArg.equalsIgnoreCase("empty")) {
                    recipe.setSlot(i, Material.getMaterial(recipeArg.toUpperCase()));
                }
            }

            // TODO: Craft multiples?
            recipe.register();
        } else {
            recipe = null;
        }

        itemType = JetpackTypes.valueOf(section.getString("type", "armor").toUpperCase());
        actionType = ActionTypes.valueOf(section.getString("actionType", "NONE"));

        if (itemType == JetpackTypes.ARMOR) {
            String slotName = section.getString("slot", "chestplate").toUpperCase();
            switch(slotName) {
                case "HELMET":
                    slot = 0;
                    break;
                case "CHESTPLATE":
                    slot = 1;
                    break;
                case "LEGGINGS":
                    slot = 2;
                    break;
                case "BOOTS":
                    slot = 3;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid slot type: " + slotName);
            }
        } else {
            slot = -1;
        }

        // Read fuel
        String fuelTypesString = section.getString("fuel", "").trim();
        if (!fuelTypesString.isEmpty()) {
            String[] fuelTypesRaw = fuelTypesString.split(" ");
            fuelTypes = new Material[fuelTypesRaw.length];
            for (int i = 0; i < fuelTypesRaw.length; i++) {
                fuelTypes[i] = Material.getMaterial(fuelTypesRaw[i].toUpperCase());
            }
        } else {
            fuelTypes = new Material[0];
        }

        repairable = section.getBoolean("repairable", false);
        jetpackEffect = section.getBoolean("useJetpackEffect", false);

        // Parse velocity
        String[] velocityString = section.getString("velocity", "0.45 0.6 0.45")
                .trim().split(" ");
        velocity = new float[3];
        for (int i = 0; i < velocity.length; i++) {
            velocity[i] = Float.parseFloat(velocityString[i]);
        }
    }

    /**
     * Returns the 'give' name of this Jetpack.
     *
     * It is sourced from the configuration section name, and is used within commands.
     *
     * @return A String containing this Jetpacks give name.
     */
    public String getGiveName() {
        return giveName;
    }

    /**
     * Returns the name of this Jetpack. This may contain ChatColors.
     *
     * @return A Jetpack name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a instance of this Jetpack as a item.
     *
     * @return A ItemStack.
     */
    public ItemStack getItem() {
        ItemStack items = new ItemStack(material, 1);
        ItemMeta meta = items.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(description);
        items.setItemMeta(meta);
        return items;
    }

    /**
     * Searches a player for this Jetpack.
     *
     * @param player The player to search.
     * @return A slot, or -1 if not found.
     */
    public int searchInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        if (itemType == JetpackTypes.ARMOR
                && isItemThisJetpack(
                    inventory.getItem(inventory.getSize() - (2 + slot)))) {
                return inventory.getSize() - (2 + slot);
        } else if (itemType == JetpackTypes.TOOL) {
            // TODO: Tools
        }
        return -1;
    }

    /**
     * Checks if a specified item is this Jetpack.
     *
     * @param stack The item to check.
     * @return If this item is this Jetpack.
     */
    public boolean isItemThisJetpack(ItemStack stack) {
        return PlayerInteractionUtils.isItemStackEqual(getItem(), stack);
    }

    /**
     * Internal method to handle players crouching.
     *
     * @param event The event to handle.
     */
    void onCrouch(PlayerToggleSneakEvent event) {
        if (!hasPermission(event.getPlayer())) {
            return;
        }

        // TODO: Handle item damaging

        if (actionType == ActionTypes.BOOST && event.isSneaking()) {
            if (!checkFuel(event.getPlayer())) {
                return;
            }

            Vector dir = event.getPlayer().getLocation().getDirection();
            event.getPlayer().setVelocity(
                    PlayerInteractionUtils.addVector(event.getPlayer(), new Vector(
                            dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D),
                            velocity[0], velocity[1], velocity[2]));

            if (jetpackEffect) {
                VisualCandyUtils.jetpackEffect(event.getPlayer());
            }
        } else if (actionType == ActionTypes.BURST && event.isSneaking()) {
            int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(JetpackManager.getInstance().getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (!event.getPlayer().isOnline()
                            || !checkFuel(event.getPlayer())) {
                        Bukkit.getScheduler().cancelTask(burstTimers.remove(event.getPlayer().getUniqueId()));
                        return;
                    }

                    Vector dir = event.getPlayer().getLocation().getDirection();
                    double y = event.getPlayer().getVelocity().getY();
                    if (y < 0.3D) {
                        y = 0.3D;
                    }
                    y *= 1.3D;
                    if (y > 10) {
                        y = 10;
                    }
                    event.getPlayer().setVelocity(
                            PlayerInteractionUtils.addVector(event.getPlayer(), new Vector(
                                            dir.getX() * 0.5D, y, dir.getZ() * 0.5D), velocity[0], velocity[1],
                                    velocity[2]));

                    VisualCandyUtils.jetpackEffect(event.getPlayer());
                }
            }, 1L, 1L);
            burstTimers.put(event.getPlayer().getUniqueId(), id);
        } else if (actionType == ActionTypes.BURST && !event.isSneaking()) {
            if (burstTimers.containsKey(event.getPlayer().getUniqueId())) {
                Bukkit.getScheduler().cancelTask(burstTimers.remove(event.getPlayer().getUniqueId()));
            }
        } else if (actionType == ActionTypes.TELEPORT && event.isSneaking()) {
            if (!checkFuel(event.getPlayer())) {
                return;
            }

            HashSet<Material> ignore = new HashSet<>();
            ignore.add(Material.AIR);
            Block block = event.getPlayer().getTargetBlock(ignore, 30);
            if (block != null) {
                Vector oldVelocity = event.getPlayer().getVelocity();
                Location loc = block.getLocation().add(0.5, 1, 0.5);
                loc.setYaw(event.getPlayer().getLocation().getYaw());
                loc.setPitch(event.getPlayer().getLocation().getPitch());
                event.getPlayer().teleport(loc);
                event.getPlayer().setVelocity(oldVelocity);
            }
        }
    }

    /**
     * Internal method to handle players getting damaged.
     *
     * @param event The event to handle. Must be for a Player.
     */
    void onDamage(EntityDamageEvent event) {
        // TODO: Handle item damaging

        if (event.getCause() == EntityDamageEvent.DamageCause.FALL
                && (actionType == ActionTypes.NO_FALL_DAMAGE
                || event.getEntity().hasPermission("easyjetpack.nofall"))) {
            if (!hasPermission((Player) event.getEntity())) {
                return;
            }
            if (!checkFuel((Player) event.getEntity())) {
                return;
            }
            event.setCancelled(true);
        }
    }

    /**
     * Internal method to handle players using anvils to repair jetpacks.
     *
     * @param event The event to check.
     */
    void onPlayerRenameItem(InventoryClickEvent event) {
        if (!repairable) {
            event.setCancelled(true);
        }
    }

    /**
     * Internal event used when this Jetpack is removed from a Manager.
     */
    void onRemove() {
        for (Map.Entry<UUID, Integer> id : burstTimers.entrySet()) {
            Bukkit.getScheduler().cancelTask(id.getValue());
            Player player = Bukkit.getPlayer(id.getKey());
            if (player != null) {
                player.sendMessage(EasyJetpackPlugin.CHAT_PREFIX
                        + "Plugin has been reloaded. Reactivate burst tool to continue flying.");
            }
        }
    }

    /**
     * If the player has permissions to use this Jetpack.
     *
     * @param player The player to check.
     * @return If the player has perms.
     */
    public boolean hasPermission(Player player) {
        return player.hasPermission("easyjetpack.useAllJetpacks")
                || player.hasPermission("easyjetpack." + getGiveName());
    }

    /**
     * Checks the fuel of this Jetpack.
     *
     * @param player The player to check.
     * @return If fuel was successfully used.
     */
    private boolean checkFuel(Player player) {
        // Check if fuel is enabled
        if (fuelTypes.length != 0) {
            for (Material fuelType : fuelTypes) {
                // TODO: Fuel with durability value
                // TODO: better params
                if (PlayerInteractionUtils.useFuel(player, fuelType, 0, true, 1f)) {
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "You have no fuel!");
            return false;
        } else {
            return true;
        }
    }
}

package net.jselby.ej;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.jselby.ej.EasyJetpackPlugin.CHAT_PREFIX;

/**
 * A Jetpack is a singleton used to represent a modified item in the world.
 */
public class Jetpack {
    private static Pattern colorFilter = Pattern.compile("\\$(?<color>([A-Za-z]*))\\$");

    private final String name;
    private final String giveName;
    private final List<String> description;
    private final Material material;

    private final CraftingRecipe recipe;

    private final JetpackTypes type;
    private final int slot;

    private final boolean jetpackEffect;
    private final boolean repairable;

    private final Material[] fuelTypes;

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

            recipe.register();
        } else {
            recipe = null;
        }

        type = JetpackTypes.valueOf(section.getString("type", "armor").toUpperCase());

        if (type == JetpackTypes.ARMOR) {
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
        String[] fuelTypesRaw = section.getString("fuel", "").trim().split(" ");
        fuelTypes = new Material[fuelTypesRaw.length];
        for (int i = 0; i < fuelTypesRaw.length; i++) {
            fuelTypes[i] = Material.getMaterial(fuelTypesRaw[i].toUpperCase());
        }

        // TODO: Implement repairability
        repairable = section.getBoolean("repairable", false);
        jetpackEffect = section.getBoolean("useJetpackEffect", false);
    }

    public String getGiveName() {
        return giveName;
    }

    public final String getName() {
        return name;
    }

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
        ItemStack me = getItem();
        if (type == JetpackTypes.ARMOR
                && Utils.isItemStackEqual(me,
                    inventory.getItem(inventory.getSize() - (2 + slot)))) {
                return inventory.getSize() - (2 + slot);
        } else if (type == JetpackTypes.TOOL) {
            // TODO: Tools
        }
        return -1;
    }

    public void onCrouch(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            if (!checkFuel(event.getPlayer())) {
                return;
            }

            // TODO: Custom movement types, velocity
            Vector dir = event.getPlayer().getLocation().getDirection();
            event.getPlayer().setVelocity(
                    Utils.addVector(event.getPlayer(), new Vector(
                            dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D), 0.45, 0.6, 0.45));

            if (jetpackEffect) {
                VisualCandy.jetpackEffect(event.getPlayer());
            }
        }
    }

    private boolean checkFuel(Player player) {
        // Check if fuel is enabled
        if (fuelTypes.length != 0) {
            for (Material fuelType : fuelTypes) {
                // TODO: better params
                if (Utils.useFuel(player, fuelType, 0, true, 1f)) {
                    return true;
                }
            }
            player.sendMessage(CHAT_PREFIX + ChatColor.RED + "You have no fuel!");
            return false;
        } else {
            return true;
        }
    }
}

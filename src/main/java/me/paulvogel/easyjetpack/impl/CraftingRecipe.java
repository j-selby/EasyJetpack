package me.paulvogel.easyjetpack.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * A wrapper for the Bukkit crafting recipes, to make it simpler
 *
 * @author James
 */
public class CraftingRecipe {
    private static final char[] chars = new char[]{'A', 'B', 'C', 'D', 'E',
            'F', 'G', 'H', 'I'};
    private Material[] materials;
    private ItemStack result;

    public CraftingRecipe(ItemStack result) {
        materials = new Material[3 * 3];
        this.result = result;
    }

    /**
     * Sets the Material in a slot
     *
     * @param slot The slot to set
     * @param mat  The material
     */
    public void setSlot(int slot, Material mat) {
        materials[slot] = mat;
    }

    /**
     * Internally used to register this recipe with Bukkit.
     */
    public void register() {
        ShapedRecipe recipe = new ShapedRecipe(result);
        String[] lines = new String[]{"", "", ""};

        for (int i = 0; i < materials.length; i++) {
            if (materials[i] != null) {
                lines[(int) Math.floor(i / 3)] = lines[(int) Math.floor(i / 3)] + chars[i];
            } else {
                lines[(int) Math.floor(i / 3)] = lines[(int) Math.floor(i / 3)] + " ";
            }
        }

        recipe.shape(lines[0], lines[1], lines[2]);

        for (int i = 0; i < materials.length; i++) {
            if (materials[i] != null) {
                recipe.setIngredient(chars[i], materials[i]);
            }
        }

        Bukkit.getServer().addRecipe(recipe);
    }
}

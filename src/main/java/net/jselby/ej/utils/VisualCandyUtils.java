package net.jselby.ej.utils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Visual Candy relates to sound and visual effects for Jetpacks.
 */
public class VisualCandyUtils {
    private VisualCandyUtils() {}

    /**
     * Creates a 'Jetpack' effect at the player's location
     *
     * @param player The player to launch the effect from
     */
    public static void jetpackEffect(Player player) {
        playEffect(Effect.SMOKE, player.getLocation(), 256);
        playEffect(Effect.SMOKE, player.getLocation(), 256);
        playEffect(Effect.GHAST_SHOOT, player.getLocation(), 1);
    }

    /**
     * Plays the respective effect at the respective location
     *
     * @param e   The effect to play
     * @param l   The location of the effect
     * @param num How many effects should be emitted
     */
    public static void playEffect(Effect e, Location l, int num) {
        l.getWorld().playEffect(l, e, num);
    }
}

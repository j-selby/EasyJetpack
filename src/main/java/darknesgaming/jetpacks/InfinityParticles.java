package darknesgaming.jetpacks;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class InfinityParticles {
    /**
     * The effects for the Infinity Jetpack
     *
     * @param player The player to launch the effect from
     */
    public static void jetpackEffect(Player player) {
        playEffect(Effect.COLOURED_DUST, player.getLocation(), 256);
        playEffect(Effect.COLOURED_DUST, player.getLocation(), 256);
        playEffect(Effect.GHAST_SHOOT, player.getLocation(), 1);
        playEffect(Effect.COLOURED_DUST, player.getLocation(), 256);
        playEffect(Effect.COLOURED_DUST, player.getLocation(), 256);
        playEffect(Effect.COLOURED_DUST, player.getLocation(), 256);
        playEffect(Effect.COLOURED_DUST, player.getLocation(), 256);
        playEffect(Effect.COLOURED_DUST, player.getLocation(), 256);
        playEffect(Effect.PARTICLE_SMOKE, player.getLocation(), 256);
        playEffect(Effect.PARTICLE_SMOKE, player.getLocation(), 256);
        playEffect(Effect.PARTICLE_SMOKE, player.getLocation(), 256);
        playEffect(Effect.CRIT, player.getLocation(), 256);
    }

    /**
     * Plays the respective effect at the respective location
     *
     * @param e   The effect to play
     * @param l   The location of the effect
     * @param num How many effects should be emitted
     */
    @SuppressWarnings("deprecation")
    public static void playEffect(Effect e, Location l, int num) {
        for (final Player player : Bukkit.getOnlinePlayers())
            player.playEffect(l, e, num);
    }
}

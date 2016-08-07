package me.paulvogel.easyjetpack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.paulvogel.easyjetpack.impl.AntiCheatImpl;

import java.util.ArrayList;

public abstract class CheatPluginAdapter {
    private static ArrayList<CheatPluginAdapter> adapters = new ArrayList<CheatPluginAdapter>();

    static void run() {
        if (Bukkit.getServer().getPluginManager().getPlugin("AntiCheatReloaded") != null) {
            System.out.println("AntiCheatReloaded found! Hooking API...");
            adapters.add(new AntiCheatImpl());
        }
    }

    /**
     * Calls all CheatPluginAdapters with these applicable arguments. For example, this
     * could invoke all anti-cheat plugins to allow flying.
     *
     * @param player The player to add the exemption to
     * @param type   The type of exemption
     */
    public static void addException(Player player, CheatPluginAdapter.Type type) {
        for (CheatPluginAdapter adp : adapters.toArray(new CheatPluginAdapter[adapters.size()])) {
            adp.exemptPlayer(player, type);
        }
    }

    /**
     * This revokes a players exception from a type of cheating.
     *
     * @param player The player to add the exemption to
     * @param type   The type of exemption
     */
    public static void removeException(Player player,
                                       CheatPluginAdapter.Type type) {
        for (CheatPluginAdapter adp : adapters.toArray(new CheatPluginAdapter[adapters.size()])) {
            adp.unexemptPlayer(player, type);
        }
    }

    public static boolean exempted(Player player, CheatPluginAdapter.Type type) {
        for (CheatPluginAdapter adp : adapters.toArray(new CheatPluginAdapter[adapters.size()])) {
            if (!adp.isExempted(player, type)) {
                return false;
            }
        }
        return true;
    }

    public abstract void exemptPlayer(Player player, CheatPluginAdapter.Type type);

    public abstract void unexemptPlayer(Player player, CheatPluginAdapter.Type type);

    public abstract boolean isExempted(Player player, CheatPluginAdapter.Type type);

    public abstract boolean isPluginEnabled();

    public enum Type {
        FLY, NOCLIP
    }

}

package net.jselby.ej;

import net.jselby.ej.api.EasyJetpackAPI;
import net.jselby.ej.impl.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

import java.io.File;
import java.io.IOException;

/**
 * The main EasyJetpack class. This registers the default Jetpacks, as well as
 * creates the JetpackManager.
 *
 * @author James
 */
public class EasyJetpack extends JavaPlugin {
    private static EasyJetpack instance;

    @Override
    public void onEnable() {
        EasyJetpack.instance = this;

        if (!getDataFolder().exists()) {
            boolean success = getDataFolder().mkdir();
            if (!success) {
                getLogger().warning("Failed to create plugin directory!");
            }
        }

        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
        }

        // Alert the server owner to allow-flight being false
        if (!Bukkit.getAllowFlight()) {
            getLogger().warning("==========");
            getLogger().warning("allow-flight is set to false in the");
            getLogger().warning("server.properties configuration file!");
            getLogger().warning("This will mean that players will be kicked");
            getLogger().warning(
                    "when using Jetpacks for a extended time period");
            getLogger().warning(
                    "Consider enabling allow-flight to prevent this");
            getLogger().warning("==========");
        }

        // Register the command listener
        getServer().getPluginCommand("ej").setExecutor(new CommandListener());

        // Creates a manager, which internal/external code will use
        JetpackManager manager = new JetpackManager(this);

        // Register our default Jetpacks
        if (getConfig().getBoolean("jetpacks.traditional.enabled", true))
            manager.addJetpack(new TraditionalJetpack());
        if (getConfig().getBoolean("jetpacks.burst.enabled", true))
            manager.addJetpack(new BurstJetpack());
        if (getConfig().getBoolean("jetpacks.teleport.enabled", true))
            manager.addJetpack(new TeleportJetpack());
        if (getConfig().getBoolean("jetpacks.hover.enabled", true)) {
            manager.addJetpack(new HoverJetpack());
            manager.addJetpack(new HoverJetpackController());
        }
        if (getConfig().getBoolean("jetpacks.boots.enabled", true))
            manager.addJetpack(new Fallboots());

        // Make the API ready for use
        new EasyJetpackAPI(manager);

        // Load Metrics
        try {
            Metrics metrics = new Metrics(this);
            Graph graph = metrics.createGraph("Jetpack count");
            for (int i = 0; i <= 10; i++) {
                final int count = i;
                graph.addPlotter(new Plotter("" + count) {
                    @Override
                    public int getValue() {
                        if (EasyJetpackAPI.getManager().getJetpacks().length == count) {
                            return 1;
                        }
                        return 0;
                    }
                });
            }
            graph.addPlotter(new Plotter("More then 10") {
                @Override
                public int getValue() {
                    if (EasyJetpackAPI.getManager().getJetpacks().length > 10) {
                        return 1;
                    }
                    return 0;
                }
            });
            metrics.start();
        } catch (IOException e) {
            getLogger().warning(
                    "Metrics failed to start: " + e.getClass().getName() + ": "
                            + e.getMessage());
        }

        // Hook anticheat plugins here (AntiCheat, NCP...)
        try {
            CheatPluginAdapter.run();
        } catch (Exception ignored) {
        }

        getLogger().info(
                "EasyJetpack (v" + getDescription().getVersion()
                        + ") has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().resetRecipes();
    }

    /**
     * Obtains a instance of the plugin
     *
     * @return A EasyJetpack instance
     */
    public static EasyJetpack getInstance() {
        return instance;
    }

    /**
     * Checks if EasyJetpack has allowed a player to fly
     *
     * @param player The player to check
     * @return If the player has been allowed to fly
     */
    public boolean haveAllowedFlying(Player player) {
        // TODO: Add compatability with Essentials and other /fly plugins
        return true;
    }
}

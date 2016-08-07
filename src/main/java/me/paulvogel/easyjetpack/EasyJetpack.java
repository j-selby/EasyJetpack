package me.paulvogel.easyjetpack;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import darknesgaming.jetpacks.CraftableLavaBucket;
import darknesgaming.jetpacks.EntryJetpack;
import darknesgaming.jetpacks.InfinityJetpack;
import darknesgaming.jetpacks.InfinityRocketPack;
import darknesgaming.jetpacks.RocketBoots;
import darknesgaming.jetpacks.SecretItem1;
import darknesgaming.jetpacks.SecretItem2;
import darknesgaming.jetpacks.SecretItem3;
import me.paulvogel.easyjetpack.api.EasyJetpackAPI;
import me.paulvogel.easyjetpack.impl.BurstJetpack;
import me.paulvogel.easyjetpack.impl.Fallboots;
import me.paulvogel.easyjetpack.impl.HoverJetpack;
import me.paulvogel.easyjetpack.impl.HoverJetpackController;
import me.paulvogel.easyjetpack.impl.TeleportJetpack;
import me.paulvogel.easyjetpack.impl.TraditionalJetpack;
import reditto.jetpacks.SlimePack;

/**
 * The main EasyJetpack class. This registers the default Jetpacks, as well as
 * creates the JetpackManager.
 *
 * @author James
 */
public class EasyJetpack extends JavaPlugin {
    private static EasyJetpack instance;

    /**
     * Obtains a instance of the plugin
     *
     * @return A EasyJetpack instance
     */
    public static EasyJetpack getInstance() {
        return instance;
    }

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
            getLogger().warning("when using Jetpacks for a extended time period");
            getLogger().warning("Consider enabling allow-flight to prevent this");
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
        if (getConfig().getBoolean("jetpacks.rocket.enabled", true)) {
            manager.addJetpack(new HoverJetpack());
            manager.addJetpack(new HoverJetpackController());
        }
        if (getConfig().getBoolean("jetpacks.boots.enabled", true))
            manager.addJetpack(new Fallboots());
        // Everything created by DarknesGaming, leader of the Infinity Laboratories
        if (getConfig().getBoolean("jetpacks.entry.enabled", true))
        	manager.addJetpack(new EntryJetpack());
        if (getConfig().getBoolean("jetpacks.secretitem1.enabled", true))
        	manager.addJetpack(new SecretItem1());
        if (getConfig().getBoolean("jetpacks.infinity.enabled", true))
        	manager.addJetpack(new InfinityJetpack());
        if (getConfig().getBoolean("jetpacks.secretitem2.enabled", true))
        	manager.addJetpack(new SecretItem2());
        if (getConfig().getBoolean("jetpacks.lavabucket.enabled", true))
        	manager.addJetpack(new CraftableLavaBucket());
        if (getConfig().getBoolean("jetpacks.secretitem3.enabled", true))
        	manager.addJetpack(new SecretItem3());
        if (getConfig().getBoolean("jetpacks.infinitypack.enabled", true))
        	manager.addJetpack(new InfinityRocketPack());
        if (getConfig().getBoolean("jetpacks.slimepack.enabled", true))
        	manager.addJetpack(new SlimePack());
        if (getConfig().getBoolean("jetpacks.rocketboots.enabled", true))
        	manager.addJetpack(new RocketBoots());
        
        // Everything created by other companies.

        // Make the API ready for use
        new EasyJetpackAPI(manager);

        // Load Metrics

        // TODO Add new metrics here

        // Hook anticheat plugins here (AntiCheat, NCP...)
        try {
            CheatPluginAdapter.run();
        } catch (Exception ignored) {
        }

        getLogger().info("InfinityJetpacks (v" + getDescription().getVersion() + ") has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().resetRecipes();
    }

    /**
     * Checks if EasyJetpack has allowed a player to fly
     *
     * @param player The player to check
     * @return If the player has been allowed to fly
     */
    public boolean haveAllowedFlying(Player player) {
        // TODO: Add compatibility with Essentials and other /fly plugins
        return true;
    }
}

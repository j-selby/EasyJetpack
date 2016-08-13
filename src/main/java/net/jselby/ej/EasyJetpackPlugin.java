package net.jselby.ej;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;

/**
 * The main entry point for the Bukkit plugin. Handles hooks and config reading.
 */
public class EasyJetpackPlugin extends JavaPlugin {
    final static String CHAT_PREFIX = ChatColor.WHITE + "["
            + ChatColor.BLUE + "EasyJetpack" + ChatColor.WHITE + "] ";

    private JetpackManager manager;

    /**
     * Called when the plugin is first started.
     */
    public void onEnable() {
        // Give warning about allowing flight
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

        // Save the default configuration files
        if (!new File(getDataFolder(), "jetpacks.yml").exists()) {
            saveResource("jetpacks.yml", false);
            if (new File(getDataFolder(), "config.yml").exists()) {
                getLogger().info("Hi! Thanks for upgrading to EasyJetpack " + getDescription().getVersion() + "!");
                getLogger().info("EasyJetpack's configuration has been moved into a file named 'jetpacks.yml' in");
                getLogger().info("the plugin directory. Check your configurations, as modifications will not have");
                getLogger().info("been transferred.");
            } else {
                getLogger().info("This is my first start. I have copied a configuration file named 'jetpacks.yml'");
                getLogger().info("into the plugin directory. Check this file to make sure it is correct.");
            }
        }

        manager = new JetpackManager(this);

        // Get our resources ready
        reloadConfig();

        // We did it!
        getLogger().info(
                "EasyJetpack (v" + getDescription().getVersion()
                        + ") has been successfully enabled!");
    }

    /**
     * Reloads the plugin's configuration, and resets all Jetpacks.
     */
    @Override
    public void reloadConfig() {
        // Parse our jetpack configuration
        File jetpackConfig = new File(getDataFolder(), "jetpacks.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(jetpackConfig);

        // Reset the jetpacks currently installed
        manager.removeAllJetpacks();

        // Import the configuration
        Set<String> jetpackRoots = config.getValues(false).keySet();

        for (String jetpackRoot : jetpackRoots) {
            ConfigurationSection section = config.getConfigurationSection(jetpackRoot);
            manager.addJetpack(new Jetpack(section));
        }
    }

    /**
     * Handles a command sent by the console or a Player.
     *
     * @param sender The source of the command.
     * @param command The command itself.
     * @param label The command name.
     * @param args Command arguments.
     * @return If the command was successful. (Always true).
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            // Give the user help, if they haven't specified a command.
            sender.sendMessage(CHAT_PREFIX + "Version " + getDescription().getVersion());
            sender.sendMessage(CHAT_PREFIX + manager.getJetpacks().length + " jetpacks loaded.");

            // Print out our commands
            sender.sendMessage(CHAT_PREFIX + "Commands:");
            sender.sendMessage(CHAT_PREFIX + ChatColor.ITALIC + "/ej help"
                    + ChatColor.RESET + ": Print out this help.");
            sender.sendMessage(CHAT_PREFIX + ChatColor.ITALIC + "/ej list"
                    + ChatColor.RESET + ": Lists available jetpacks.");
            sender.sendMessage(CHAT_PREFIX + ChatColor.ITALIC + "/ej give [player] <jetpack>"
                    + ChatColor.RESET + ": Gives a jetpack.");
            sender.sendMessage(CHAT_PREFIX + ChatColor.ITALIC + "/ej reload"
                    + ChatColor.RESET + ": Reloads the plugin.");
        } else if (args[0].equalsIgnoreCase("reload")) {
            // Reloads the plugin's configuration
            sender.sendMessage(CHAT_PREFIX + "Reloading plugin...");
            getLogger().info("Reload triggered by " + sender.getName());
            try {
                reloadConfig();
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(CHAT_PREFIX + ChatColor.RED + "An error occurred while reloading the plugin. " +
                        "Check the server logs for more information.");
                return true;
            }
            sender.sendMessage(CHAT_PREFIX + ChatColor.GREEN + "Reload successful.");
        } else if (args[0].equalsIgnoreCase("list")) {
            // List jetpacks
            sender.sendMessage(CHAT_PREFIX + "Jetpacks:");
            for (Jetpack jetpack : manager.getJetpacks()) {
                sender.sendMessage(CHAT_PREFIX + " - (" + jetpack.getGiveName() + ") " + jetpack.getName());
            }
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 3) {
                // Named give
                Jetpack jetpack = manager.getJetpack(args[2]);
                Player player = Bukkit.getPlayer(args[1]);
                if (jetpack != null && player != null) {
                    player.getInventory().addItem(jetpack.getItem());
                    sender.sendMessage(CHAT_PREFIX + "Given " + jetpack.getName());
                } else if (player == null) {
                    sender.sendMessage(CHAT_PREFIX + ChatColor.RED + "Player not found.");
                } else {
                    sender.sendMessage(CHAT_PREFIX + ChatColor.RED + "Jetpack not found.");
                }
            } else if (args.length == 2) {
                // Self give
                if (sender instanceof Player) {
                    Jetpack jetpack = manager.getJetpack(args[1]);
                    if (jetpack != null) {
                        ((Player) sender).getInventory().addItem(jetpack.getItem());
                        sender.sendMessage(CHAT_PREFIX + "Given " + jetpack.getName());
                    } else {
                        sender.sendMessage(CHAT_PREFIX + ChatColor.RED + "Jetpack not found.");
                    }
                } else {
                    sender.sendMessage(CHAT_PREFIX + ChatColor.RED + "You must be a player to run this command.");
                }
            } else {
                sender.sendMessage(CHAT_PREFIX + ChatColor.RED + "Unknown syntax.");
            }
        } else {
            // We couldn't find what they were looking for.
            sender.sendMessage(CHAT_PREFIX + ChatColor.RED + "Unknown command.");
        }

        return true;
    }
}

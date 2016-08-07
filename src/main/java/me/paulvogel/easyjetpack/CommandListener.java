package me.paulvogel.easyjetpack;

import me.paulvogel.easyjetpack.api.EasyJetpackAPI;
import me.paulvogel.easyjetpack.api.Jetpack;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
    private final static String PREFIX = ChatColor.BLUE + "[" + ChatColor.LIGHT_PURPLE
            + "IJ" + ChatColor.BLUE + "]" + ChatColor.RESET + " ";

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String name, String[] args) {
        if (!sender.hasPermission("easyjetpack.admin")) {
            sender.sendMessage(ChatColor.RED
                    + "You do not have permission to run this command!");
            return true;
        }
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            Jetpack jetpack = EasyJetpackAPI.getManager().getJetpackByName(
                    args[1].toLowerCase());
            if (jetpack == null) {
                sender.sendMessage(PREFIX + ChatColor.RED
                        + "No jetpack was found by that name. "
                        + "Try /ej list to find available jetpacks.");
            } else if (sender instanceof Player) {
                ((Player) sender).getInventory().addItem(jetpack.getItem());
                sender.sendMessage(PREFIX + ChatColor.GREEN + "Obtained a "
                        + ChatColor.RESET + jetpack.getName());
            } else {
                sender.sendMessage(PREFIX + ChatColor.RED
                        + "You must be a player to run that command.");
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            Jetpack jetpack = EasyJetpackAPI.getManager().getJetpackByName(
                    args[2].toLowerCase());
            if (jetpack == null) {
                sender.sendMessage(PREFIX + ChatColor.RED
                        + "No jetpack was found by that name. "
                        + "Try /ej list to find available jetpacks.");
            } else {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(PREFIX + ChatColor.RED
                            + "No player was found by that name.");
                } else {
                    player.getInventory().addItem(jetpack.getItem());
                }
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            Jetpack[] jetpacks = EasyJetpackAPI.getManager().getJetpacks();
            sender.sendMessage(PREFIX + "Jetpacks:");
            for (Jetpack jetpack : jetpacks) {
                sender.sendMessage(PREFIX + "   " + jetpack.getName());
                sender.sendMessage(PREFIX + "         Give name: "
                        + jetpack.getGiveName());
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            EasyJetpack.getInstance().reloadConfig();
            sender.sendMessage(PREFIX + ChatColor.GREEN
                    + "Reloaded configuration!");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("authors")) {
        	showAuthors(sender);
        }
        else {
            sender.sendMessage(PREFIX + ChatColor.RED
                    + "Unknown command. Try /ej help for more information.");
        }
        return true;
    }

    /**
     * Shows a small help page to a user, or the console
     *
     * @param sender The player/console to send the help to
     */
    private void showHelp(CommandSender sender) {
        sender.sendMessage(PREFIX + "InfinityJetpack commands:");
        sender.sendMessage(PREFIX + "   /ej give <player name> [jetpack name]");
        sender.sendMessage(PREFIX + "         Gives a jetpack by it's name.");
        sender.sendMessage(PREFIX + "   /ej list");
        sender.sendMessage(PREFIX + "         Lists available jetpacks.");
        sender.sendMessage(PREFIX + "   /ej reload");
        sender.sendMessage(PREFIX + "         Reloads the configuration.");
        sender.sendMessage(PREFIX + "   /ej help");
        sender.sendMessage(PREFIX + "         Shows this help message.");
        sender.sendMessage(PREFIX + "   /ej authors");
        sender.sendMessage(PREFIX + "         Displays the authors.");
    }
    /**
     * The author message when /ej authors is run.
     * 
     * @param sender
     */
    private void showAuthors(CommandSender sender) {
    	sender.sendMessage(PREFIX + "InfinityJetpack authors:");
    	sender.sendMessage(PREFIX + "   Creator of IJ: DarknesGaming");
    	sender.sendMessage(PREFIX + "   Based on EasyJetpack by: j_selby");
    	sender.sendMessage(PREFIX + "   1.9.4 compatibility: PaulVogel");
    }

}

package me.arman.warnings.commands;

import me.arman.warnings.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarningsCommand implements CommandExecutor {
	static Main plugin;

	public WarningsCommand(Main instance) {
		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("warnings")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Usage: /warnings <player>");
					return true;
				}
				if (args.length >= 1) {
					OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
					if (!(plugin.getConfig().getInt("maxWarnings") == -1)) {
						sender.sendMessage(ChatColor.RED + target.getName() + "\'s warnings: " + ChatColor.DARK_RED
								+ plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings") + "/"
								+ ChatColor.DARK_RED + plugin.getConfig().getInt("maxWarnings"));
					} else {
						sender.sendMessage(ChatColor.RED + target.getName() + "\'s warnings: " + ChatColor.DARK_RED
								+ plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings"));
					}
				}
			}
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!p.hasPermission("warnings.view")) {
					p.sendMessage(ChatColor.RED + "You can not check your warnings!");
					return true;
				}
				if (args.length == 0) {
					if (!(plugin.getConfig().getInt("maxWarnings") == -1)) {
						p.sendMessage(ChatColor.RED + "Times Warned: " + ChatColor.DARK_RED
								+ plugin.dFile.getConfig().getInt(p.getUniqueId().toString() + ".warnings")
								+ ChatColor.RED + "/" + ChatColor.DARK_RED + plugin.getConfig().getInt("maxWarnings"));
					} else {
						p.sendMessage(ChatColor.RED + "Times Warned: " + ChatColor.DARK_RED
								+ plugin.dFile.getConfig().getInt(p.getUniqueId().toString() + ".warnings"));
					}
				} else if (args.length >= 1) {
					OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
					if (!p.hasPermission("warnings.view.others")) {
						p.sendMessage(ChatColor.RED + "You can not check other player's warnings!");
						return true;
					}
					if (!(plugin.getConfig().getInt("maxWarnings") == -1)) {
						p.sendMessage(ChatColor.RED + target.getName() + "\'s Times Warned: " + ChatColor.DARK_RED
								+ plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings")
								+ ChatColor.RED + "/" + ChatColor.DARK_RED + plugin.getConfig().getInt("maxWarnings"));
					} else {
						p.sendMessage(ChatColor.RED + target.getName() + "\'s Times Warned: " + ChatColor.DARK_RED
								+ plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings"));
					}
				}
			}
		}
		return false;
	}
}

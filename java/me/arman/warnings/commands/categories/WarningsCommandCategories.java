package me.arman.warnings.commands.categories;

import me.arman.warnings.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class WarningsCommandCategories implements CommandExecutor {
	static Main plugin;

	public WarningsCommandCategories(Main instance) {
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
					ConfigurationSection info = plugin.dFile.getConfig()
							.getConfigurationSection(target.getUniqueId().toString() + ".warnings.");
					if (info == null) {
						sender.sendMessage(ChatColor.GREEN + target.getName() + " has no warnings!");
						return true;
					}
					sender.sendMessage(ChatColor.RED + target.getName() + "\'s warnings: \n" + ChatColor.DARK_RED
							+ plugin.dFile.getConfig()
									.getConfigurationSection(target.getUniqueId().toString() + ".warnings")
									.getValues(true).toString().replace("{", "").replace("}", "").replace(",", "\n")
									.replace("=", ": ").replace(" ", ""));
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
				ConfigurationSection info = plugin.dFile.getConfig()
						.getConfigurationSection(p.getUniqueId().toString() + ".warnings.");
				if (info == null) {
					p.sendMessage(ChatColor.GREEN + "You have no warnings!");
					return true;
				}
				p.sendMessage(ChatColor.RED + "Warnings: \n" + ChatColor.DARK_RED
						+ plugin.dFile.getConfig().getConfigurationSection(p.getUniqueId().toString() + ".warnings")
								.getValues(true).toString().replace("{", "").replace("}", "").replace(",", "\n")
								.replace("=", ":").replace(" ", ""));
				return true;

			} else if (args.length >= 1) {
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (!p.hasPermission("warnings.view.others")) {
					p.sendMessage(ChatColor.RED + "You can not check other player's warnings!");
					return true;
				}
				ConfigurationSection info = plugin.dFile.getConfig()
						.getConfigurationSection(target.getUniqueId().toString() + ".warnings.");
				if (info == null) {
					p.sendMessage(ChatColor.GREEN + target.getName() + " has no warnings!");
					return true;
				}
				p.sendMessage(ChatColor.RED + target.getName() + "\'s warnings: " + ChatColor.DARK_RED
						+ plugin.dFile.getConfig()
								.getConfigurationSection(target.getUniqueId().toString() + ".warnings").getValues(true)
								.toString().replace("{", "").replace("}", "").replace(",", "\n").replace("=", ": ")
								.replace(" ", ""));
			}
		}

		return false;
	}
}

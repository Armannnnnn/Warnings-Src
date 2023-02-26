package me.arman.warnings.commands.categories;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.arman.warnings.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ClearWarningsCommandCategories implements CommandExecutor {

	static Main plugin;

	public ClearWarningsCommandCategories(Main instance) {
		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("clearwarnings")) {
			if (!sender.hasPermission("warnings.clear")) {
				sender.sendMessage(ChatColor.RED + "You can not clear warnings!");
				return true;
			}
			if (args.length == 0 || args.length == 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /clearwarnings <player> <category>");
				return true;
			} else if (args.length >= 2) {
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (!(plugin.getConfig().getConfigurationSection("categories")
						.contains(args[1].replace(" ", "") + "."))) {
					sender.sendMessage(ChatColor.RED
							+ "That is an invalid warning category! Type /categories to view the valid reasons!");
					return true;
				}
				int amount = plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings." + args[1]);
				if (amount == 0) {
					sender.sendMessage(ChatColor.GREEN + target.getName() + " has no warnings in category " + args[1]);
					return true;
				}
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".username", target.getName());
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings." + args[1], 0);
				if (plugin.getConfig().getBoolean("logSpecificInfo")) {
					List<String> l = plugin.dFile.getConfig().getStringList(target.getUniqueId().toString() + ".info");
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("dateFormat"));
					l.add("Warnings cleared by " + "(" + sender.getName() + ")" + " in category " + "(" + args[1] + ")"
							+ " at " + "(" + format.format(now) + ")");
					plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".info", l);
				}
				plugin.dFile.saveConfig();
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings." + args[1], 0);
				plugin.dFile.saveConfig();
				sender.sendMessage(
						ChatColor.GREEN + "Cleared " + target.getName() + "\'s warnings in category " + args[1]);
			}

		}
		return false;
	}
}

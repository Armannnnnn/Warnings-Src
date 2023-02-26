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

public class TakeWarningsCommandCategories implements CommandExecutor {

	static Main plugin;

	public TakeWarningsCommandCategories(Main instance) {
		plugin = instance;

	}

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("takewarnings")) {
			if (!sender.hasPermission("warnings.take")) {
				sender.sendMessage(ChatColor.RED + "You can not take warnings!");
				return true;
			}
			if (args.length <= 2) {
				sender.sendMessage(ChatColor.RED + "Usage: /takewarnings <player> <amount> <category>");
				return true;
			} else if (args.length >= 3) {
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (!isInt(args[1])) {
					sender.sendMessage(ChatColor.RED + "Invalid warning number!");
					return true;
				}
				if (!(plugin.getConfig().getConfigurationSection("categories")
						.contains(args[2].replace(" ", "") + "."))) {
					sender.sendMessage(ChatColor.RED
							+ "That is an invalid warning category! Type /categories to view the valid reasons!");
					return true;
				}
				int current = plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings." + args[2],
						0);
				if (current <= 0) {
					sender.sendMessage(
							ChatColor.RED + target.getName() + " already has 0 warnings in category " + args[2]);
					return true;
				}
				int num = Integer.parseInt(args[1]);
				if (current - num < 0) {
					sender.sendMessage(ChatColor.RED + target.getName() + " can't go below " + current
							+ " warnings in category " + args[2]);
					return true;
				}
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".username", target.getName());
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings." + args[2], current - num);
				if (plugin.getConfig().getBoolean("logSpecificInfo")) {
					List<String> l = plugin.dFile.getConfig().getStringList(target.getUniqueId().toString() + ".info");
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("dateFormat"));
					l.add("(" + num + ")" + " warnings taken by " + "(" + sender.getName() + ")" + " in category " + "("
							+ args[2] + ")" + " at " + "(" + format.format(now) + ")");
					plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".info", l);
				}
				plugin.dFile.saveConfig();
				sender.sendMessage(ChatColor.GREEN + "Removed " + num + " warnings from " + target.getName()
						+ " in category " + args[2]);
			}

		}
		return false;
	}
}

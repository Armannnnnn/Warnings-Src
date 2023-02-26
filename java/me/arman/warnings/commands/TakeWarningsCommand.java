package me.arman.warnings.commands;

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

public class TakeWarningsCommand implements CommandExecutor {

	static Main plugin;

	public TakeWarningsCommand(Main instance) {
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
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /takewarnings <player> <amount>");
				return true;
			} else if (args.length >= 2) {
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (!isInt(args[1])) {
					sender.sendMessage(ChatColor.RED + "Invalid warning number!");
					return true;
				}
				int current = plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings", 0);
				if (current <= 0) {
					sender.sendMessage(ChatColor.RED + target.getName() + " already has 0 warnings!");
					return true;
				}
				int num = Integer.parseInt(args[1]);
				if (current - num < 0) {
					sender.sendMessage(ChatColor.RED + target.getName() + " only has " + current + " warnings!");
					return true;
				}
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".username", target.getName());
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings", current - num);
				if (plugin.getConfig().getBoolean("logSpecificInfo")) {
					List<String> l = plugin.dFile.getConfig().getStringList(target.getUniqueId().toString() + ".info");
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("dateFormat"));
					l.add("(" + num + ")" + " warnings taken by " + "(" + sender.getName() + ")" + " at " + "("
							+ format.format(now) + ")");
					plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".info", l);
				}
				plugin.dFile.saveConfig();
				sender.sendMessage(ChatColor.GREEN + "Removed " + num + " warnings from " + target.getName() + "!");
			}

		}
		return false;
	}
}

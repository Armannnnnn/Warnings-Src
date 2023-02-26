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

public class SetWarningsCommand implements CommandExecutor {

	static Main plugin;

	public SetWarningsCommand(Main instance) {
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
		if (cmd.getName().equalsIgnoreCase("setwarnings")) {
			if (!sender.hasPermission("warnings.set")) {
				sender.sendMessage(ChatColor.RED + "You can not set warnings!");
				return true;
			}
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /setwarnings <player> <amount>");
				return true;
			} else if (args.length >= 2) {
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (!isInt(args[1])) {
					sender.sendMessage(ChatColor.RED + "Invalid warning number!");
					return true;
				}
				int current = plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings", 0);
				int num = Integer.parseInt(args[1]);
				if (num > plugin.getConfig().getInt("maxWarnings") && !(plugin.getConfig().getInt("maxWarnings") == -1)
						|| current + num > plugin.getConfig().getInt("maxWarnings")
								&& !(plugin.getConfig().getInt("maxWarnings") == -1)) {
					sender.sendMessage(ChatColor.RED + target.getName() + " can't go over "
							+ plugin.getConfig().getInt("maxWarnings") + " warning(s)!");
					return true;
				}
				if (num < 0) {
					sender.sendMessage(ChatColor.RED + "You can not set negative warnings!");
					return true;
				}
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".username", target.getName());
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings", num);
				if (plugin.getConfig().getBoolean("logSpecificInfo")) {
					List<String> l = plugin.dFile.getConfig().getStringList(target.getUniqueId().toString() + ".info");
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("dateFormat"));
					l.add("Warnings set to " + "(" + num + ")" + " by " + "(" + sender.getName() + ")" + " at " + "("
							+ format.format(now) + ")");
					plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".info", l);
				}
				plugin.dFile.saveConfig();
				sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "\'s warnings to " + num);
			}

		}
		return false;
	}
}

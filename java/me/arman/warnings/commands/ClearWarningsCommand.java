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

public class ClearWarningsCommand implements CommandExecutor {

	static Main plugin;

	public ClearWarningsCommand(Main instance) {
		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("clearwarnings")) {
			if (!sender.hasPermission("warnings.clear")) {
				sender.sendMessage(ChatColor.RED + "You can not clear warnings!");
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Usage: /clearwarnings <player>");
				return true;
			} else if (args.length >= 1) {
				OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".username", target.getName());
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings", 0);
				if (plugin.getConfig().getBoolean("logSpecificInfo")) {
					List<String> l = plugin.dFile.getConfig().getStringList(target.getUniqueId().toString() + ".info");
					Date now = new Date();
					SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("dateFormat"));
					l.add("Warnings cleared by " + "(" + sender.getName() + ")" + " at " + "(" + format.format(now)
							+ ")");
					plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".info", l);
				}
				plugin.dFile.saveConfig();
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings", 0);
				plugin.dFile.saveConfig();
				sender.sendMessage(ChatColor.GREEN + "Cleared " + target.getName() + "\'s warnings!");
			}

		}
		return false;
	}
}

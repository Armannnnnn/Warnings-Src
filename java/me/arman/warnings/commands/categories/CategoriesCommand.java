package me.arman.warnings.commands.categories;

import me.arman.warnings.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CategoriesCommand implements CommandExecutor {

	static Main plugin;

	public CategoriesCommand(Main instance) {
		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("categories")) {
			if (!(plugin.getConfig().getBoolean("categories.use"))) {
				sender.sendMessage(ChatColor.RED + "Warning categories are disabled!");
				return true;
			}
			if (!sender.hasPermission("warnings.categories")) {
				sender.sendMessage(ChatColor.RED + "You can not view the warning categories!");
				return true;
			}
			sender.sendMessage(ChatColor.DARK_RED + "Warning Categories:");
			for (String c : plugin.getConfig().getStringList("categories.message")) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', c));

			}
		}
		return false;

	}
}

package me.arman.warnings.commands;

import me.arman.warnings.Main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ResetWarningsCommand implements CommandExecutor {

	static Main plugin;

	public ResetWarningsCommand(Main instance) {
		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("resetwarnings")) {
			if (!sender.hasPermission("warnings.reset")) {
				sender.sendMessage(ChatColor.RED + "You can not reset all warnings!");
				return true;
			}
			for (String key : plugin.dFile.getConfig().getKeys(false)) {
				plugin.dFile.getConfig().set(key, null);
			}
			plugin.dFile.saveConfig();
			sender.sendMessage(ChatColor.GREEN + "You have reset ALL warnings!");
			plugin.getLogger().info("ALL warnings have been manually reset by " + sender.getName());

		}
		return false;
	}
}

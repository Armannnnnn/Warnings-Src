package me.arman.warnings.commands;

import java.util.List;

import me.arman.warnings.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class WarningInfoCommand implements CommandExecutor, Listener {
	static Main plugin;

	public WarningInfoCommand(Main instance) {
		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("warninginfo")) {
			if (!plugin.getConfig().getBoolean("logSpecificInfo")) {
				sender.sendMessage(ChatColor.RED + "Advanced information logging has been disabled!");
				return true;
			}
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.RED + "Usage: /warninginfo <player>");
					return true;
				}
				if (args.length >= 1) {
					OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
					for (String i : plugin.getConfig().getStringList("warningInfoMessage")) {
						List<String> l = plugin.dFile.getConfig()
								.getStringList(target.getUniqueId().toString() + ".info");
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', i)
								.replace("%info%", l.toString()).replace("%player%", target.getName()));
					}
				}
			}
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!p.hasPermission("warnings.info")) {
					p.sendMessage(ChatColor.RED + "You can not check your warning info!");
					return true;
				}
				if (args.length == 0) {
					for (String i : plugin.getConfig().getStringList("warningInfoMessage")) {
						List<String> l = plugin.dFile.getConfig().getStringList(p.getUniqueId().toString() + ".info");
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', i)
								.replace("%info%", l.toString().replace("[", "").replace("]", "").replace(",", "\n"))
								.replace("%player%", p.getName()).replace("%warnings%", Integer.toString(
										plugin.dFile.getConfig().getInt(p.getUniqueId().toString() + ".warnings"))));
					}
				} else if (args.length >= 1) {
					OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
					if (!p.hasPermission("warnings.info.others")) {
						p.sendMessage(ChatColor.RED + "You can not check other player's warning information!");
						return true;
					}
					for (String i : plugin.getConfig().getStringList("warningInfoMessage")) {
						List<String> l = plugin.dFile.getConfig()
								.getStringList(target.getUniqueId().toString() + ".info");
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', i)
								.replace("%info%", l.toString().replace("[", "").replace("]", "").replace(",", "\n"))
								.replace("%player%", target.getName())
								.replace("%warnings%", Integer.toString(plugin.dFile.getConfig()
										.getInt(target.getUniqueId().toString() + ".warnings"))));
					}
				}
			}
		}

		return false;
	}
}

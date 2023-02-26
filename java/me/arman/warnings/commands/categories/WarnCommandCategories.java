package me.arman.warnings.commands.categories;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.arman.warnings.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommandCategories implements CommandExecutor {

	static Main plugin;

	public WarnCommandCategories(Main instance) {
		plugin = instance;

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("warn")) {
			if (!sender.hasPermission("warnings.warn")) {
				sender.sendMessage(ChatColor.RED + "You can not warn players!");
				return true;
			}
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /warn <player> <category> <reason>");
				return true;
			}

			if (args.length >= 3) {
				Player target = Bukkit.getServer().getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
					return true;
				}
				if (args[0].equalsIgnoreCase(sender.getName())) {
					sender.sendMessage(ChatColor.RED + "You can not warn yourself!");
					return true;
				}
				if (target.hasPermission("warnings.immune")) {
					sender.sendMessage(ChatColor.RED + target.getName() + " is immune to warning!");
					return true;
				}
				if (!(plugin.getConfig().getConfigurationSection("categories")
						.contains(args[1].replace(" ", "") + "."))) {
					sender.sendMessage(ChatColor.RED
							+ "That is an invalid warning category! Type /categories to view the valid reasons!");
					return true;
				}
				int current = plugin.dFile.getConfig().getInt(target.getUniqueId().toString() + ".warnings." + args[1],
						0);
				List<String> commandList = plugin.getConfig()
						.getConfigurationSection("categories." + args[1].replace(" ", "") + ".")
						.getStringList("punishments");
				if (current >= commandList.size()) {
					sender.sendMessage(ChatColor.RED + "There are no more than " + commandList.size()
							+ " punishments configured for category \"" + args[1] + "\"!");
					return true;
				}

				StringBuilder b = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					b.append(args[i] + " ");
				}
				String warnmessage = b.toString().trim();
				Date now = new Date();
				SimpleDateFormat format = new SimpleDateFormat(plugin.getConfig().getString("dateFormat"));
				for (String bmsg : plugin.getConfig().getStringList("broadcastMessage")) {
					for (Player all : Bukkit.getServer().getOnlinePlayers()) {
						if (all.hasPermission("warnings.receivebroadcast")) {
							all.sendMessage(ChatColor.translateAlternateColorCodes('&', bmsg)
									.replace("%warner%", sender.getName()).replace("%player%", target.getName())
									.replace("%reason%", warnmessage).replace("%time%", format.format(now))
									.replace("%category%", args[1])
									.replace("%warnings%", Integer.toString(plugin.dFile.getConfig()
											.getInt(target.getUniqueId().toString() + ".warnings." + args[1]))));
						}
					}
				}
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".username", target.getName());
				plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".warnings." + args[1], current + 1);
				plugin.dFile.saveConfig();
				if (plugin.getConfig().getBoolean("logSpecificInfo")) {
					List<String> l = plugin.dFile.getConfig().getStringList(target.getUniqueId().toString() + ".info");
					l.add("Warned by " + "(" + sender.getName() + ")" + " for " + "(" + warnmessage + ")"
							+ " in category " + "(" + args[1] + ")" + " at " + "(" + format.format(now) + ")");
					plugin.dFile.getConfig().set(target.getUniqueId().toString() + ".info", l);
					plugin.dFile.saveConfig();
				}
				if (current > -1) {
					String command = plugin.getConfig()
							.getConfigurationSection("categories." + args[1].replace(" ", "") + ".")
							.getStringList("punishments".replace("|", "\n")).get(current++)
							.replace("%player%", target.getName());
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
				}

				for (String msg : plugin.getConfig().getStringList("warningMessage")) {
					target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
							.replace("%warner%", sender.getName()).replace("%reason%", warnmessage)
							.replace("%time%", format.format(now)).replace("%category%", args[1])
							.replace("%warnings%", Integer.toString(plugin.dFile.getConfig()
									.getInt(target.getUniqueId().toString() + ".warnings." + args[1]))));

				}
			}

		}

		return false;

	}
}

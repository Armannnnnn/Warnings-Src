package me.arman.warnings;

import java.io.File;

import me.arman.warnings.commands.AddWarningsCommand;
import me.arman.warnings.commands.ClearWarningsCommand;
import me.arman.warnings.commands.ResetWarningsCommand;
import me.arman.warnings.commands.SetWarningsCommand;
import me.arman.warnings.commands.TakeWarningsCommand;
import me.arman.warnings.commands.WarnCommand;
import me.arman.warnings.commands.WarningInfoCommand;
import me.arman.warnings.commands.WarningsCommand;
import me.arman.warnings.commands.categories.AddWarningsCommandCategories;
import me.arman.warnings.commands.categories.CategoriesCommand;
import me.arman.warnings.commands.categories.ClearWarningsCommandCategories;
import me.arman.warnings.commands.categories.SetWarningsCommandCategories;
import me.arman.warnings.commands.categories.TakeWarningsCommandCategories;
import me.arman.warnings.commands.categories.WarnCommandCategories;
import me.arman.warnings.commands.categories.WarningsCommandCategories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public DataFile dFile;
	Main plugin;

	@Override
	public void onEnable() {
		this.dFile = new DataFile(this);
		defaultConfigSetup();
		registerCommands();
	}

	@Override
	public void onDisable() {

	}

	private void defaultConfigSetup() {
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			this.saveResource("config.yml", false);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ReloadWarnings")) {
			if (!sender.hasPermission("warnings.reload")) {
				sender.sendMessage(ChatColor.RED + "You can not reload the configuration!");
				return true;
			}
			reloadConfig();
			registerCommands();
			sender.sendMessage(ChatColor.GREEN + "Reloaded the configuration!");

		}
		return false;
	}

	private void registerCommands() {
		if (!(getConfig().getBoolean("categories.use"))) {
			getCommand("Warn").setExecutor(new WarnCommand(this));
		} else {
			getCommand("Warn").setExecutor(new WarnCommandCategories(this));
		}
		if (!(getConfig().getBoolean("categories.use"))) {
			getCommand("Warnings").setExecutor(new WarningsCommand(this));
		} else {
			getCommand("Warnings").setExecutor(new WarningsCommandCategories(this));
		}
		if (!(getConfig().getBoolean("categories.use"))) {
			getCommand("ClearWarnings").setExecutor(new ClearWarningsCommand(this));
		} else {
			getCommand("ClearWarnings").setExecutor(new ClearWarningsCommandCategories(this));
		}
		if (!(getConfig().getBoolean("categories.use"))) {
			getCommand("SetWarnings").setExecutor(new SetWarningsCommand(this));
		} else {
			getCommand("SetWarnings").setExecutor(new SetWarningsCommandCategories(this));
		}
		if (!(getConfig().getBoolean("categories.use"))) {
			getCommand("AddWarnings").setExecutor(new AddWarningsCommand(this));
		} else {
			getCommand("AddWarnings").setExecutor(new AddWarningsCommandCategories(this));
		}
		if (!(getConfig().getBoolean("categories.use"))) {
			getCommand("TakeWarnings").setExecutor(new TakeWarningsCommand(this));
		} else {
			getCommand("TakeWarnings").setExecutor(new TakeWarningsCommandCategories(this));
		}
		getCommand("WarningInfo").setExecutor(new WarningInfoCommand(this));
		getCommand("Categories").setExecutor(new CategoriesCommand(this));
		getCommand("ResetWarnings").setExecutor(new ResetWarningsCommand(this));

		if (getConfig().getBoolean("resetEnabled")) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
				public void run() {
					for (String key : dFile.getConfig().getKeys(false)) {
						dFile.getConfig().set(key, null);
					}
					dFile.saveConfig();
					getLogger().info("Warnings have been automatically reset!");
				}
			}, getConfig().getLong("resetTime") * 1200, getConfig().getLong("resetTime") * 1200);

		}
	}

}

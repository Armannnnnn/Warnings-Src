package me.arman.warnings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataFile {
	private Main plugin;
	private FileConfiguration dataConfig = null;
	private File dataConfigFile = null;

	public DataFile(Main plugin) {
		this.plugin = plugin;
		saveDefaultConfig();
	}

	public void reloadConfig() {
		if (this.dataConfigFile == null) {
			this.dataConfigFile = new File(this.plugin.getDataFolder(), "data.yml");
		}
		this.dataConfig = YamlConfiguration.loadConfiguration(this.dataConfigFile);

		InputStream defConfigStream = this.plugin.getResource("data.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.dataConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getConfig() {
		if (this.dataConfig == null) {
			reloadConfig();
		}
		return this.dataConfig;
	}

	public void saveConfig() {
		if ((this.dataConfig == null) || (this.dataConfigFile == null)) {
			return;
		}
		try {
			getConfig().save(this.dataConfigFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.dataConfigFile, ex);
		}
	}

	public void saveDefaultConfig() {
		if (this.dataConfigFile == null) {
			this.dataConfigFile = new File(this.plugin.getDataFolder(), "data.yml");
		}
		if (!this.dataConfigFile.exists()) {
			this.plugin.saveResource("data.yml", false);
		}
	}

}

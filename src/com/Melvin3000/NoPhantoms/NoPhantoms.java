package com.Melvin3000.NoPhantoms;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

public class NoPhantoms extends JavaPlugin {

	public static JavaPlugin instance;

	@Override
	public void onEnable() {
		instance = this;

		File dir = new File("plugins/NoPhantoms");
		if (!dir.exists()) {
			dir.mkdir();
		}

		getCommand("phantoms").setExecutor(new PhantomsCommand());
		getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuit(), this);

		SQLite.connect();
		Phantoms.beginRestInterval();
	}

	@Override
	public void onDisable() {
		SQLite.close();
	}

}

package com.Melvin3000.NoPhantoms;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();
		if (SQLite.containsUUID(uuid)) {
			Phantoms.noPhantomPlayers.add(uuid);
			Phantoms.rest(event.getPlayer());
		}
	}
}

package com.Melvin3000.NoPhantoms;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

	@EventHandler
	public void onplayerQuit(PlayerQuitEvent event) {
		Phantoms.noPhantomPlayers.remove(event.getPlayer().getUniqueId());
	}
}

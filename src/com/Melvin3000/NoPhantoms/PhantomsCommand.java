package com.Melvin3000.NoPhantoms;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PhantomsCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player player = (sender instanceof Player) ? (Player) sender : null;
		if (player == null) {
			NoPhantoms.instance.getLogger().info("Only players can toggle phantoms.");
			return true;
		}

		Phantoms.toggle(player);
		return true;
	}
}

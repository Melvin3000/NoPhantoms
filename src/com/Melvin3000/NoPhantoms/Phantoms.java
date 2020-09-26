package com.Melvin3000.NoPhantoms;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.Statistic;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TranslatableComponent;

public class Phantoms {

	public static HashSet<UUID> noPhantomPlayers = new HashSet<UUID>();

	/* Refresh last rest every 10 minutes */
	private static final int REPEAT_INTERVAL = 10 * 60 * 20;

	/**
	 * Toggle phantom spawning for given player if they meet the requirements
	 * @param player Player to toggle
	 */
	public static void toggle(Player player) {
		UUID uuid = player.getUniqueId();

		/* Disable phantoms for player */
		if (!noPhantomPlayers.contains(uuid)) {

			if (!canDisable(player)) {
				errorMessage(player);
				return;
			}

			noPhantomPlayers.add(uuid);
			SQLite.insertUUID(uuid);
			rest(player);
			player.sendMessage(ChatColor.GREEN + "Disabled phantom spawning.");
			NoPhantoms.instance.getLogger().info(player.getName() + " disabled phantoms");
		}

		/* Enable Phantoms for player once again */
		else {
			noPhantomPlayers.remove(uuid);
			SQLite.deleteUUID(uuid);
			player.sendMessage(ChatColor.GREEN + "Enabled phantom spawning.");
			NoPhantoms.instance.getLogger().info(player.getName() + " enabled phantoms");
		}
	}

	/**
	 * Reset the players sleep timer so phantoms can not spawn from them.
	 * This is called for individual players when they first disable phantoms and log in,
	 * and then on an interval for all online players with phantoms disabled to keep them rested.
	 * @param player Player to simulate sleeping for
	 */
	public static void rest(Player player) {
		int prev = player.getStatistic(Statistic.TIME_SINCE_REST);
		NoPhantoms.instance.getLogger().info("Reset sleep timer for " + player.getName() +
				" from " + prev);
		player.setStatistic(Statistic.TIME_SINCE_REST, 0);
	}

	/**
	 * Make all players online with phantoms disabled rest
	 */
	public static void restAll() {
		noPhantomPlayers.stream()
			.map(NoPhantoms.instance.getServer()::getPlayer)
			.forEach(Phantoms::rest);
	}

	/**
	 * Schedule a repeating event to rest all players
	 */
	public static void beginRestInterval() {
		NoPhantoms.instance.getServer().getScheduler().runTaskTimer(
				NoPhantoms.instance,
				new Runnable() {
					@Override
					public void run() {
						restAll();
					}
				},
				0,
				REPEAT_INTERVAL);
	}

	/**
	 * Check if the player is allowed to disable phantom spawning.
	 * Currently defined as having the 'two birds one stone' advancement (Kill 2 phantoms with a
	 * single crossbow bolt), but this could be changed to something else such as sleeping in a bed
	 * Note: players can always turn phantom spawning back on in case these requirements are ever
	 * changed to prevent them from being stuck without phantoms.
	 * @param player Player to check for advancement
	 */
	private static boolean canDisable(Player player) {
		Advancement advancement = NoPhantoms.instance.getServer()
				.getAdvancement(NamespacedKey.minecraft("adventure/two_birds_one_arrow"));

		return player.getAdvancementProgress(advancement).isDone();
	}

	/**
	 * Tell the player that they haven't completed requirements to toggle phantoms
	 * @param player Player to message
	 */
	private static void errorMessage(Player player) {
		String title = "advancements.adventure.two_birds_one_arrow.title";
		String description = "advancements.adventure.two_birds_one_arrow.description";

		player.spigot().sendMessage(new ComponentBuilder("You must complete ")
				/* Hoverable box displaying localized version of advancement */
				.append("[")
				.color(ChatColor.DARK_PURPLE)
				.append(new TranslatableComponent(title))
				.event(new HoverEvent(
						HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder()
								.append(new TranslatableComponent(title))
								.color(ChatColor.DARK_PURPLE)
								.append("\n")
								.append(new TranslatableComponent(description))
								.create()))
				.append("]")
				.reset()
				.color(ChatColor.DARK_PURPLE)

				.append(" to disable phantoms")
				.reset()
				.create());
	}
}

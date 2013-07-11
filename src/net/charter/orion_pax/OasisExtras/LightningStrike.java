package net.charter.orion_pax.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LightningStrike implements Runnable{
	private int taskId;
	private int repeats;
	private Location loc;

	@SuppressWarnings("unused")
	private OasisExtras plugin;
	
	public LightningStrike(OasisExtras plugin, Location loc, long delay, int repeats)
	{
		this.plugin = plugin;
		this.loc = loc;
		this.repeats = repeats;

		taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 1L, delay);
	}
	public void cancel() {
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}

	public void run() {
		if (repeats-- <= 0) {
			cancel();
		} else {
			loc.getWorld().strikeLightningEffect(loc);
		}
	}
}

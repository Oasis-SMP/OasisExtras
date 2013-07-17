package net.charter.orion_pax.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TreeTask implements Runnable{
	private int taskId;
	private Location loc;
	private ItemStack apple = new ItemStack(260,1);
	private int lucky;
	private String mytree;

	private OasisExtras plugin;

	public TreeTask(OasisExtras plugin, Location loc, int delay, String mytree)
	{
		this.plugin = plugin;
		this.loc = loc;
		this.lucky = plugin.extras.randomNum(1, 20);
		this.mytree = mytree;

		taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, delay,20);
	}

	public void cancel() {
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}

	public String mytree(){
		return this.mytree;
	}

	public void run() {
		int chance = plugin.extras.randomNum(1, 100 * plugin.percent);
		if (chance == lucky) {
			if (hasNearbyPlayers(loc, 15D)) {
				int x = loc.getBlockX() + plugin.extras.randomNum(-4, 4);
				int z = loc.getBlockZ() + plugin.extras.randomNum(-4, 4);
				Location newloc = new Location(loc.getWorld(), x, loc.getBlockY(), z);
				loc.getWorld().dropItemNaturally(newloc, apple);
			}
		}
	}

	boolean hasNearbyPlayers(Location loc, double radius) {
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.getWorld().equals(loc.getWorld())) {
				if (p.getLocation().distanceSquared(loc) <= radius * radius) {
					return true;
				}
			}
		}
		return false;
	}
}

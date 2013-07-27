package net.charter.orion_pax.OasisExtras;

import java.util.List;

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
	private Double percent;
	private int AppleDelay;
	private Double maxdistance;



	private final OasisExtras plugin;

	public TreeTask(OasisExtras plugin, Location loc, String mytree)
	{
		this.plugin = plugin;
		this.loc = loc;
		this.lucky = randomNum(1, 20);
		this.mytree = mytree;

		percent = plugin.getConfig().getDouble("Percent")/100;
		AppleDelay = plugin.getConfig().getInt("AppleProduceDelay");
		maxdistance = plugin.getConfig().getDouble("maxdistance");

		taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, AppleDelay,20);
	}

	public void cancel() {
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}

	public String mytree(){
		return this.mytree;
	}

	public void run() {
		int chance = randomNum(1, 100 * percent);
		if (chance == lucky) {
			if (hasNearbyPlayers(loc, maxdistance)) {
				int x = loc.getBlockX() + randomNum(-4, 4);
				int z = loc.getBlockZ() + randomNum(-4, 4);
				Location newloc = new Location(loc.getWorld(), x, loc.getBlockY(), z);
				loc.getWorld().dropItemNaturally(newloc, apple);
				//plugin.getServer().broadcast(newloc.toString(), "debug");
				//plugin.getServer().broadcast(loc.toString(), "debug");
			}
		}
	}

	boolean hasNearbyPlayers(Location loc, double radius) {
		for(Player player : loc.getWorld().getPlayers()) {
			if (player.getLocation().distanceSquared(loc) <= radius * radius) {
				return true;
			}
		}
		return false;
	}

	public int randomNum(Integer lownum, double d) {
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}
}

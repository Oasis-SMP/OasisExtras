package net.charter.orion_pax.OasisExtras;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TreeTask implements Runnable{
	private int taskId;
	public Location loc;
	private ItemStack apple = new ItemStack(260,1);
	private int lucky;
	public String mytree;
	private Integer percent;
	private int AppleDelay;
	private Double maxdistance;



	private final OasisExtras plugin;

	public TreeTask(OasisExtras plugin, Location loc, String mytree)
	{
		percent = plugin.getConfig().getInt("Percent");
		AppleDelay = plugin.getConfig().getInt("AppleProduceDelay");
		maxdistance = plugin.getConfig().getDouble("maxdistance");
		
		this.plugin = plugin;
		this.loc = loc;
		this.lucky = randomNum(percent, 100);
		this.mytree = mytree;

		taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, AppleDelay,20);
	}

	public void cancel() {
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}

	public String mytree(){
		return this.mytree;
	}

	public void run() {
		int chance = randomNum(percent, 100);
		if (chance == lucky) {
			if (hasNearbyPlayers(loc, maxdistance)) {
				int x = loc.getBlockX() + randomNum(-4, 4);
				int z = loc.getBlockZ() + randomNum(-4, 4);
				Location newloc = new Location(loc.getWorld(), x, loc.getBlockY(), z);
				loc.getWorld().dropItemNaturally(newloc, apple);
				plugin.getServer().broadcast(mytree + " - " + newloc.getBlockX() + "," + newloc.getBlockY() + "," + newloc.getBlockZ(), "debug");
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

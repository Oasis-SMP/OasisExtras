package net.charter.orion_pax.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class spawnRandomMob implements Runnable{
	private int taskId;
	private int count;
	private Location loc;
	private Player player;
	int min, max;
	World world;
	EntityType entitytype;
	PotionEffect potionEffect;

	@SuppressWarnings("unused")
	private OasisExtras plugin;
	
	public spawnRandomMob(OasisExtras plugin, int count, Player player, int min, int max, EntityType entitytype, PotionEffect potionEffect)
	{
		this.plugin = plugin;
		this.count = count;
		this.player = player;
		this.loc = player.getLocation();
		this.world = loc.getWorld();
		this.min = min;
		this.max = max;
		this.entitytype = entitytype;
		this.potionEffect = potionEffect;

		taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1);
	}
	public void cancel() {
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}

	public void run() {
		if (count <= 0) {
			cancel();
		} else {
			int x = randomNum(min, max);
			int y = player.getLocation().getBlockY()+randomNum(-10,10);
			int z = randomNum(min, max);
			Location newloc = new Location(world, loc.getBlockX()+x, y, loc.getBlockZ()+z);//Location to tp to, and players bottom half
			Location block1 = new Location(world, newloc.getBlockX(), (y-1), newloc.getBlockZ());//Block under player
			Location block2 = new Location(world, newloc.getBlockX(), (y + 1), newloc.getBlockZ());//player location top
			if (newloc.getBlock().isEmpty() && block2.getBlock().isEmpty() && !block1.getBlock().isLiquid() && !block1.getBlock().isEmpty()){
				Entity creature = player.getWorld().spawnEntity(newloc, entitytype);
				((Creature)creature).setTarget(player);
				if(potionEffect!=null){
					((LivingEntity) creature).addPotionEffect(potionEffect);
				}
				count--;
			}
		}
	}
	
	public int randomNum(Integer lownum, double d) {
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}
}

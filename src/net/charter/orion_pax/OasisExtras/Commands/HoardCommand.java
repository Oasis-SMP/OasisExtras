package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.spawnRandomMob;

public class HoardCommand implements CommandExecutor {

	private OasisExtras plugin;

	public HoardCommand(OasisExtras plugin){
		this.plugin=plugin;
	}
	
	//BukkitTask randomTask;
	Location newloc;
	int radius;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		radius = plugin.getConfig().getInt("radius");
		if (args.length==0){
			sender.sendMessage(ChatColor.RED + "Usage: /hoard lvl player");
			return true;
		}
		if (args.length==2){
			World gameworld = plugin.getServer().getWorld(plugin.getConfig().getString("gameworld"));
			Player player = plugin.getServer().getPlayer(args[1]);
			if (!player.getWorld().equals(gameworld)){
				radius = radius /3;
			}
			if (args[0].equals("1")){
				for (int i=1;i<11;i++){
					new spawnRandomMob(plugin, 3, player, -radius, radius, getRandomEntity(randomNum(1, 3)), getRandomPotionEffect());
				}
				return true;
			}

			if (args[0].equals("2")){
				for(int i=1;i<11;i++){
					new spawnRandomMob(plugin, 3, player, -radius, radius, getRandomEntity(randomNum(4, 6)), getRandomPotionEffect());
				}
				return true;
			}

			if (args[0].equals("3")){
				for(int i=1;i<11;i++){
					new spawnRandomMob(plugin, 3, player, -radius, radius, getRandomEntity(randomNum(7, 9)), getRandomPotionEffect());
				}
				return true;
			}

			if (args[0].equals("4")){
				new spawnRandomMob(plugin, 1, player, -radius, radius, EntityType.GIANT,getRandomPotionEffect());
				new spawnRandomMob(plugin, 1, player, -radius, radius, EntityType.GIANT,getRandomPotionEffect());
				new spawnRandomMob(plugin, 1, player, -radius, radius, EntityType.GIANT,getRandomPotionEffect());
				return true;
			}

			if (args[0].equals("5")){
				new spawnRandomMob(plugin, 1, player, -radius, radius, EntityType.WITHER, getRandomPotionEffect());
				new spawnRandomMob(plugin, 1, player, -radius, radius, EntityType.WITHER, getRandomPotionEffect());
				new spawnRandomMob(plugin, 1, player, -radius, radius, EntityType.WITHER, getRandomPotionEffect());
				return true;
			}

		}
		return false;
	}

	public EntityType getRandomEntity(int i){
		switch(i){
		case 1:
			return EntityType.CREEPER;

		case 2:
			return EntityType.ZOMBIE;

		case 3:
			return EntityType.SKELETON;

		case 4:
			return EntityType.BLAZE;

		case 5:
			return EntityType.SPIDER;

		case 6:
			return EntityType.WITCH;

		case 7:
			return EntityType.SILVERFISH;

		case 8:
			return EntityType.CAVE_SPIDER;

		case 9:
			return EntityType.PIG_ZOMBIE;

		default:
			return EntityType.WITHER;
		}
	}
	
	public PotionEffect getRandomPotionEffect(){
		int i = randomNum(1, 10);
		switch(i){
		case 1:
			return new PotionEffect(PotionEffectType.INVISIBILITY,70000, 2);
		default:
			return null;
		}
	}

	public int randomNum(Integer lownum, double d) {
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}
}

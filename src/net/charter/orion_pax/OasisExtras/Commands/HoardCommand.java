package net.charter.orion_pax.OasisExtras.Commands;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class HoardCommand implements CommandExecutor {

	private OasisExtras plugin;
	
	public HoardCommand(OasisExtras plugin){
		this.plugin=plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length==0){
			sender.sendMessage(ChatColor.RED + "Usage: /hoard lvl player");
			return true;
		}
		if (args.length==2){
			World gameworld = plugin.getServer().getWorld(plugin.getConfig().getString("gameworld"));
			int radius = plugin.getConfig().getInt("radius");
			Player player = plugin.getServer().getPlayer(args[1]);
			if (!player.getWorld().equals(gameworld)){
				radius = radius /3;
			}
			if (args[0].equals("1")){
				for(int i=1;i==10;i++){
					plugin.getLogger().info("IM SPAWNING");
					Entity creature1 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(1, 3)));
					Entity creature2 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(1, 3)));
					Entity creature3 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(1, 3)));
					((Creature)creature1).setTarget(player);
					((Creature)creature2).setTarget(player);
					((Creature)creature3).setTarget(player);
				}
				return true;
			}
			
			if (args[0].equals("2")){
				for(int i=1;i==10;i++){
					plugin.getLogger().info("IM SPAWNING");
					Entity creature1 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(4, 6)));
					Entity creature2 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(4, 6)));
					Entity creature3 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(4, 6)));
					((Creature)creature1).setTarget(player);
					((Creature)creature2).setTarget(player);
					((Creature)creature3).setTarget(player);
				}
				return true;
			}
			
			if (args[0].equals("3")){
				for(int i=1;i==10;i++){
					plugin.getLogger().info("IM SPAWNING");
					Entity creature1 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(7, 9)));
					Entity creature2 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(7, 9)));
					Entity creature3 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), getRandomEntity(plugin.extras.randomNum(7, 9)));
					((Creature)creature1).setTarget(player);
					((LivingEntity) creature1).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,70000, 2));
					((Creature)creature2).setTarget(player);
					((Creature)creature3).setTarget(player);
				}
				return true;
			}
			
			if (args[0].equals("4")){
				Entity creature1 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), EntityType.GIANT);
				Entity creature2 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), EntityType.GIANT);
				Entity creature3 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), EntityType.GIANT);
				((Creature)creature1).setTarget(player);
				((Creature)creature2).setTarget(player);
				((Creature)creature3).setTarget(player);
				((LivingEntity) creature1).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,70000, 2));
				((LivingEntity) creature2).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,70000, 2));
				((LivingEntity) creature3).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,70000, 2));
				return true;
			}
			
			if (args[0].equals("5")){
				Entity creature1 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), EntityType.WITHER);
				Entity creature2 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), EntityType.WITHER);
				Entity creature3 = player.getWorld().spawnEntity(plugin.extras.getRandomMobLoc(player, 1, radius), EntityType.WITHER);
				((Creature)creature1).setTarget(player);
				((Creature)creature2).setTarget(player);
				((Creature)creature3).setTarget(player);
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
}

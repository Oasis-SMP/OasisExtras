package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class HordeCommand implements CommandExecutor {
	
	private OasisExtras plugin;
	private World world;
	private int x;
	private int y;
	private int z;
	private Location loc;
	private int round = 1;
	private int maxrounds = 5;
	private BukkitTask task;
	private Player hordeplayer;
	private int amount = 30;
	
	public HordeCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		world = plugin.getServer().getWorld(plugin.getConfig().getString("horde.world","ownergames"));
		x = plugin.getConfig().getInt("horde.x",872);
		y = plugin.getConfig().getInt("horde.y",17);
		z = plugin.getConfig().getInt("horde.z",-513);
		loc = new Location(world,x,y,z);
		
		if(!plugin.getConfig().contains("horde")){
			plugin.getConfig().set("horde.world", world.getName());
			plugin.getConfig().set("horde.x", x);
			plugin.getConfig().set("horde.y", y);
			plugin.getConfig().set("horde.z", z);
			plugin.saveConfig();
		}
		
		if(label.equalsIgnoreCase("hordegame")){
			if(args.length>0){
				try {
					maxrounds=Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					player.sendMessage(ChatColor.RED + args[0] + " is not a number!");
					player.sendMessage(ChatColor.GOLD + "Usage:/hordegame {rounds} {player}");
					return true;
				}
			}
			
			if(args.length>1){
				hordeplayer = plugin.getServer().getPlayer(args[1]);
				if(hordeplayer==null){
					player.sendMessage(ChatColor.RED + args[0] + " is not online!");
					player.sendMessage(ChatColor.GOLD + "Usage:/hordegame {rounds} {player}");
					return true;
				}
			}
			if(args.length>2){
				player.sendMessage(ChatColor.RED + "Too many arguements!");
				player.sendMessage(ChatColor.GOLD + "Usage:/hordegame {rounds} {player}");
				return true;
			}
			if (hasNearbyPlayers(loc,50)) {
				task=plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {

					@Override
					public void run() {
						msgPlayers(loc,50,"&cround " + round + "!");
						for(int i=1;i<round*10;i++){
							Location newloc = loc;
							Creature creature = (Creature) world.spawnEntity(newloc.add(randomNum(-48,48), -5, randomNum(-48,48)), Mobs());
							creature.setRemoveWhenFarAway(false);
							if(hordeplayer!=null){
								creature.setTarget(hordeplayer);
							}
						}
						round++;
						if(round>maxrounds){
							task.cancel();
						}
					}

				}, 0, 1200L);
			}
		}
		
		if(label.equalsIgnoreCase("hordep")){
			if(args.length>0){
				hordeplayer = plugin.getServer().getPlayer(args[0]);
				if(hordeplayer==null){
					player.sendMessage(ChatColor.RED + args[0] + " is not online!");
					player.sendMessage(ChatColor.RED + "Usage:/hordep [player] {amount}");
					return true;
				}
			}
			
			if(args.length>1){
				try {
					amount=Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					player.sendMessage(ChatColor.RED + args[0] + " is not a number");
					player.sendMessage(ChatColor.RED + "Usage:/hordep [player] {amount}");
					return true;
				}
			}
			if (hordeplayer!=null) {
				for (int i = 1; i < amount; i++) {
					Creature creature = (Creature) hordeplayer.getWorld().spawnEntity(hordeplayer.getLocation().add(randomNum(-10, 10), 0, randomNum(-10, 10)), Mobs());
					creature.setTarget(hordeplayer);
					creature.setRemoveWhenFarAway(false);
				}
				return true;
			}
			return false;
		}
		
		if(args.length==1){
			try {
				amount=Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				player.sendMessage(ChatColor.RED + args[0] + " is not a number");
				player.sendMessage(ChatColor.RED + "Usage:/hordep [player] {amount}");
				return true;
			}
			return true;
		}
		for(int i=1;i<amount;i++){
			int x = randomNum(-10,10);
			int z = randomNum(-10,10);
			Location newloc = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation();
			Creature creature = (Creature) player.getWorld().spawnEntity(newloc.add(x, 0, z), Mobs());
			creature.setRemoveWhenFarAway(false);
		}
		
		
		return true;
	}
	
	private void msgPlayers(Location loc, double radius, String msg){
		for(Player player:loc.getWorld().getPlayers()){
			if(player.getLocation().distanceSquared(loc) <= radius * radius){
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
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
	
	private EntityType Mobs(){
		switch (randomNum(1,9)){
		case 1:return EntityType.CAVE_SPIDER;
		case 2:return EntityType.CREEPER;
		case 3:return EntityType.ENDERMAN;
		case 4:return EntityType.PIG_ZOMBIE;
		case 5:return EntityType.SILVERFISH;
		case 6:return EntityType.SKELETON;
		case 7:return EntityType.SPIDER;
		case 8:return EntityType.WITCH;
		case 9:return EntityType.ZOMBIE;
		default:return EntityType.SKELETON;
		}
	}
	
	public int randomNum(Integer lownum, double d) {
		return lownum + (int)(Math.random() * ((d - lownum) + 1));
	}

}

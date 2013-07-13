package net.charter.orion_pax.OasisExtras;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OasisExtrasCommand implements CommandExecutor{

	private OasisExtras plugin; // pointer to your main class, unrequired if you don't need methods from the main class

	public OasisExtrasCommand(OasisExtras plugin){
		this.plugin = plugin;
	}

	Entity vehicle;
	Entity passenger;
	int time;

	enum Commands {
		SLAP, FREEZE, DRUNK, SPOOK, ENABLEME, MOUNT, UNMOUNT, TIMER, ANIMALREGEN,
		DISABLEME, BROCAST, RANDOM, OASISEXTRAS, CHANT, THUNDERSTRUCK
	}

	enum SubCommands {
		RELOAD, CANCEL, SAVEALL, BCAST, START, CONFIG, SET, CLEAR, LIST, ADD, REMOVE, THAW
	}

	String[] oasisextrassub = {
			ChatColor.GOLD + "Usage: /oasisextras subcommand subcommand"
			,ChatColor.GOLD + "SubCommands:"
			,ChatColor.GOLD + "THAW - Clears Frozen player list in cfg"
			,ChatColor.GOLD + "RELOAD - Reloads config"
			,ChatColor.GOLD + "CANCEL SAVEALL/BCAST/CONFIG"
			,ChatColor.GOLD + "START SAVEALL/BCAST/CONFIG"
			,ChatColor.GOLD + "BCAST LIST/ADD/REMOVE"
			,ChatColor.GOLD + "Do /oasisextras [subcommand] for more info"
	}; 

	String[] oasisextrassub2 = {
			ChatColor.GOLD + "Usage as follows...."
			,ChatColor.GOLD + "/oasisextras CANCEL BCAST - Cancels auto broadcast"
			,ChatColor.GOLD + "/oasisextras CANCEL SAVEALL - Cancels auto saveall"
			,ChatColor.GOLD + "/oasisextras CANCEL CONFIG - Cancels auto save config"
			,ChatColor.GOLD + "/oasisextras START BCAST - Starts auto broadcast"
			,ChatColor.GOLD + "/oasisextras START SAVEALL - Starts auto saveall"
			,ChatColor.GOLD + "/oasisextras START CONFIG - Starts auto save config"
			,ChatColor.GOLD + "/oasisextras BCAST LIST - List auto bcast msgs"
			,ChatColor.GOLD + "/oasisextras BCAST ADD - Adds a msg to the auto bcast list"
			,ChatColor.GOLD + "/oasisextras BCAST REMOVE - Removes a msg from the auto bcast list"
	};

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Commands mycommand = Commands.valueOf(cmd.getName().toUpperCase());

		Player player = null;
		if (sender instanceof Player){
			player = (Player) sender;
		}

		switch (mycommand) {
		case ANIMALREGEN:
			if (args.length==2){
				try { 
					Integer.parseInt(args[0]); 
				} catch(NumberFormatException e) { 
					sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
					return true; 
				}
				try { 
					Integer.parseInt(args[1]); 
				} catch(NumberFormatException e) { 
					sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
					return true; 
				}
				spawnme(player,Integer.parseInt(args[0]),Integer.parseInt(args[1]));
				
			}
		case TIMER:
			if (args.length>1){
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[1]);
				for (int i = 2; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				String message = buffer.toString();
				int length = args[0].length();
				if (args[0].endsWith("h")){
					time=Integer.parseInt(args[0].substring(0, args[0].length()-1))*3600;
					timer(player.getWorld(),message);
				}
				if (args[0].endsWith("m")){
					time=Integer.parseInt(args[0].substring(0, args[0].length()-1))*60;
					timer(player.getWorld(),message);
				}
				if (args[0].endsWith("s")){
					time=Integer.parseInt(args[0].substring(0, args[0].length()-1));
					timer(player.getWorld(),message);
				}
			}
		case THUNDERSTRUCK:
			if (args.length==1){
				Location loc = player.getLocation();
				try {
					if (Integer.parseInt(args[0])>30){
						sender.sendMessage(ChatColor.RED + "Must be 30 or less!");
						return true;
					}
					LightningStrike ls = new LightningStrike(plugin, loc, 5L, Integer.parseInt(args[0]));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					sender.sendMessage(ChatColor.RED + "Must be an integer!");
					return true;
				}
				return true;
			}

		case CHANT:
			if (args.length>0){
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[0]);
				for (int i = 1; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				String message = buffer.toString();
				plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
				return true;
			} else {
				sender.sendMessage(ChatColor.GOLD + "Usage: /CHANT msg");
				return true;
			}

		case MOUNT:
			if (args.length==1){
				vehicle=Bukkit.getPlayer(args[0]);
				passenger = (Entity) sender;
				if ((vehicle!=null) && (sender instanceof Player)){
					vehicle.setPassenger(passenger);
					sender.sendMessage(ChatColor.GOLD + "You have mounted " + Bukkit.getPlayer(args[0]).getName());
					if (!plugin.mounted.containsKey(sender.getName())){
						plugin.mounted.put(sender.getName(),null);
					}
					plugin.mounted.get(Bukkit.getPlayer(args[0])).add(sender.getName());
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "Usage: /mount playername");
				return true;
			}

		case UNMOUNT:
			if (player.isInsideVehicle()) {
				player.leaveVehicle();
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Not riding a player!");
			return true;
			
		case OASISEXTRAS:
			if (args.length==0){
				sender.sendMessage(oasisextrassub);
				return true;
			}
			SubCommands subcommand = SubCommands.valueOf(args[0].toUpperCase());
			switch (subcommand){
			case THAW:
				plugin.getConfig().set("Frozen", null);
				if (!plugin.getConfig().contains("Frozen")){
					plugin.getConfig().createSection("Frozen");
				}
				return true;

			case RELOAD:
				plugin.getServer().getScheduler().cancelTasks(plugin);
				plugin.getServer().getPluginManager().disablePlugin(plugin);
				plugin.reloadConfig();
				plugin.getServer().getPluginManager().enablePlugin(plugin);
				plugin.setup();
				sender.sendMessage(ChatColor.GOLD + "Config reloaded!");
				return true;

			case CANCEL:
				if (args.length==1){
					sender.sendMessage(oasisextrassub2);
					return true;
				}
				SubCommands subcommand2 = SubCommands.valueOf(args[1].toUpperCase());
				switch (subcommand2){
				case BCAST:
					plugin.task.bcasttask.cancel();
					sender.sendMessage(ChatColor.GOLD + "Auto broadcast task is " + ChatColor.RED + "DISABLED!");
					return true;
				case SAVEALL:
					plugin.task.savethisworld.cancel();
					sender.sendMessage(ChatColor.GOLD + "Auto Save-All task is " + ChatColor.RED + "DISABLED!");
					plugin.task.remindmetask.cancel();
					return true;
				case CONFIG:
					plugin.task.savethistask.cancel();
					sender.sendMessage(ChatColor.GOLD + "Auto save Config task is " + ChatColor.RED + "DISABLED!");
					return true;
				default:
					sender.sendMessage(oasisextrassub2);
					return true;
				}
			case START:
				if (args.length==1){
					sender.sendMessage(oasisextrassub2);
					return true;
				}
				SubCommands subcommand3 = SubCommands.valueOf(args[1].toUpperCase());
				switch (subcommand3){
				case BCAST:
					if (plugin.task.bcasttask==null){
						plugin.task.bcasttask.runTaskTimer(plugin, plugin.extras.randomNum(0, 18000), plugin.bcasttimer);
						sender.sendMessage(ChatColor.GOLD + "Auto broadcast task is " + ChatColor.GREEN + "ENABLED!");
					}
					return true;
				case SAVEALL:
					if (plugin.task.savethisworld==null){
						plugin.task.savethisworld.runTaskTimer(plugin, plugin.savealltimer, plugin.savealltimer);
						plugin.task.remindmetask.runTaskTimer(plugin, plugin.savealltimer-plugin.warningtime, plugin.savealltimer);
						sender.sendMessage(ChatColor.GOLD + "Auto Save-All task is " + ChatColor.GREEN + "ENABLED!");
					}
					return true;
				case CONFIG:
					if (plugin.task.savethistask==null){
						plugin.task.savethistask.runTaskTimer(plugin, 10, 12000);
						sender.sendMessage(ChatColor.GOLD + "Auto save Config task is " + ChatColor.GREEN + "ENABLED!");
					}
					return true;
				default:
					sender.sendMessage(oasisextrassub2);
					return true;
				}
			default:
				sender.sendMessage(oasisextrassub2);
				return true;
			}

		case RANDOM:
			if (!(sender instanceof Player)) {
				if (args.length==0) {
					sender.sendMessage("This command cannot be used from the console.");
					return true;
				} else if (args.length==1){
					Player rplayer = plugin.getServer().getPlayer(args[0]);
					if (rplayer==null){
						sender.sendMessage(ChatColor.RED + "Player not online!");
						return true;
					}
					World rplayerworld=rplayer.getWorld();
					rplayer.setNoDamageTicks(plugin.ndt);
					if (rplayer.isInsideVehicle() && rplayer.getVehicle() instanceof Horse) {
						Entity horse = rplayer.getVehicle();
						horse.eject();
						rplayer.teleport(plugin.extras.getRandomLoc(rplayer.getLocation(), plugin.default_min, plugin.default_max, rplayerworld));
						horse.teleport(rplayer.getLocation());
						horse.setPassenger(rplayer);
					} else {
						rplayer.teleport(plugin.extras.getRandomLoc(rplayer.getLocation(), plugin.default_min, plugin.default_max, rplayerworld));
					}
					rplayer.sendMessage(ChatColor.GOLD + "You have been randomly teleported!");
					return true;
				} else {
					sender.sendMessage("Too many arguments!");
					return true;
				}
			}
			if (args.length == 0) {
				World default_world = player.getWorld();
				player.setNoDamageTicks(plugin.ndt);
				if (player.isInsideVehicle() && player.getVehicle() instanceof Horse) {
					Entity horse = player.getVehicle();
					horse.eject();
					player.teleport(plugin.extras.getRandomLoc(player.getLocation(), plugin.default_min, plugin.default_max, default_world));
					horse.teleport(player.getLocation());
					horse.setPassenger(player);
				} else {
					player.teleport(plugin.extras.getRandomLoc(player.getLocation(), plugin.default_min, plugin.default_max, default_world));
				}
				player.sendMessage(ChatColor.GOLD+"You have been randomly teleported!");
				return true;
			} else if(args.length==1){
				if (sender instanceof BlockCommandSender){
					Player bcsplayer = plugin.getServer().getPlayer(args[0]);
					World bcsplayerworld = bcsplayer.getWorld();
					bcsplayer.setNoDamageTicks(plugin.ndt);
					if (bcsplayer.isInsideVehicle() && bcsplayer.getVehicle() instanceof Horse) {
						Entity horse = bcsplayer.getVehicle();
						horse.eject();
						bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
						horse.teleport(bcsplayer.getLocation());
						horse.setPassenger(bcsplayer);
					} else {
						bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
					}
					player.sendMessage(ChatColor.GOLD + "You have been randomly teleported!");
					return true;
				} else if(sender.hasPermission("oasischat.staff.a")){
					Player bcsplayer = plugin.getServer().getPlayer(args[0]);
					if (bcsplayer==null){
						sender.sendMessage(ChatColor.RED + "That player is not online!");
						return true;
					}
					if (!bcsplayer.isOp()) {
						World bcsplayerworld = bcsplayer.getWorld();
						bcsplayer.setNoDamageTicks(plugin.ndt);
						if (bcsplayer.isInsideVehicle() && bcsplayer.getVehicle() instanceof Horse) {
							Entity horse = bcsplayer.getVehicle();
							horse.eject();
							bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
							horse.teleport(bcsplayer.getLocation());
							horse.setPassenger(bcsplayer);
						} else {
							bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
						}
						bcsplayer.sendMessage(ChatColor.GOLD
								+ "You have been randomly teleported!");
						sender.sendMessage(ChatColor.GOLD + bcsplayer.getName()
								+ " has been randomly teleported!");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "You cannot perform this command on that player!");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.GOLD + "Usage: /random");
					return true;
				}
			} else {
				sender.sendMessage("/random teleports you to a random location.");
				return false;
			}
		case DRUNK:
			if (args.length > 0) {
				Player target = sender.getServer().getPlayer(args[0]);
				if (target == null){
					sender.sendMessage(ChatColor.RED + args[0] + ChatColor.GOLD + " is not online!");
					return true;
				}
				int duration = 600;
				if (args.length == 2) {
					duration = Integer.parseInt(args[1]);
					duration = duration*20;
				}
				target.getPlayer().addPotionEffect(
						new PotionEffect(PotionEffectType.CONFUSION, duration,10));
				sender.sendMessage(ChatColor.GOLD + target.getName() + " is now DRUNK!");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				return false;
			}
		case FREEZE:
			if (args.length > 0) {
				if (sender.getServer().getPlayer(args[0]) != null) {
					Player target = sender.getServer().getPlayer(args[0]);
					if (target.hasPermission("OasisChat.staff.a")) {
						sender.sendMessage("Can not freeze staff");
					} else {
						if (plugin.frozen.containsKey(target.getName())) {
							plugin.frozen.remove(target.getName());
							sender.sendMessage(ChatColor.RED + target.getName() + ChatColor.BLUE + " is now THAWED!");
							target.sendMessage(ChatColor.GOLD + "You are now " + ChatColor.BLUE + "THAWED!");
							plugin.removefrozen(target);
							return true;
						} else {
							plugin.frozen.put(target.getName(),target.getLocation());
							sender.sendMessage(ChatColor.RED + target.getName() + ChatColor.AQUA + " is now FROZEN!");
							target.sendMessage(ChatColor.GOLD + "You are now " + ChatColor.AQUA + "FROZEN!");
							plugin.savefrozen(target);
							return true;
						}
					}
				} else {
					sender.sendMessage(ChatColor.GOLD + args[0] + " is not online!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				return false;
			}
			return true;

		case BROCAST:
			if (args.length > 0) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[0]);
				for (int i = 1; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				String message = buffer.toString();
				plugin.getServer().broadcastMessage(
						ChatColor.RED + "[" + ChatColor.DARK_RED + "Brocast" + ChatColor.RED + "] " + ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', message));
				return true;
			} else {
				return false;
			}

		case SPOOK:
			if (args.length > 0) {
				Player target = sender.getServer().getPlayer(args[0]);
				if (target == null){
					sender.sendMessage(ChatColor.RED + args[0] + ChatColor.GOLD + " is not online!");
					return false;
				}
				int soundtoplay = 0;
				try { 
					Integer.parseInt(args[1]); 
				} catch(NumberFormatException e) { 
					sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
					return false; 
				}
				if (args.length == 2) {
					soundtoplay = Integer.parseInt(args[1]);
				}
				switch (soundtoplay) {
				case 1:
					target.playSound(target.getLocation(), Sound.GHAST_MOAN, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Moan on " + ChatColor.RED + target.getName());
					return true;
				case 2:
					target.playSound(target.getLocation(), Sound.GHAST_SCREAM, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Scream 1 on " + ChatColor.RED + target.getName());
					return true;
				case 3:
					target.playSound(target.getLocation(), Sound.GHAST_SCREAM2, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Scream 2 on " + ChatColor.RED + target.getName());
					return true;
				case 4:
					target.playSound(target.getLocation(), Sound.CREEPER_HISS, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Creeper Hiss on " + ChatColor.RED + target.getName());
					return true;
				case 5:
					target.playSound(target.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Enderdragon Growl on " + ChatColor.RED + target.getName());
					return true;
				case 6:
					target.playSound(target.getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Enderman Scream on " + ChatColor.RED + target.getName());
					return true;
				case 7:
					target.playSound(target.getLocation(), Sound.EXPLODE, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing TNT Explosion on " + ChatColor.RED + target.getName());
					return true;
				case 8:
					target.playSound(target.getLocation(), Sound.WITHER_SPAWN, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Wither Spawn on " + ChatColor.RED + target.getName());
					return true;
				case 9:
					target.playSound(target.getLocation(), Sound.ANVIL_LAND, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Anvil Land on " + ChatColor.RED + target.getName());
					return true;
				case 10:
					target.playSound(target.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Angry Zombie Pigman on " + ChatColor.RED + target.getName());
					return true;
				default:
					target.playSound(target.getLocation(), Sound.GHAST_MOAN, 1, 1);
					sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Moan on " + ChatColor.RED + target.getName());
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Too few arguments!");
				return false;
			}

		case SLAP:
			if (!(sender instanceof Player)){
				plugin.getServer().broadcastMessage(ChatColor.RED + "CONSOLE has slapped " + args[0]);
			}
			if (args.length == 0){
				return false;
			}

			if (args[0].equalsIgnoreCase("all")){
				String msg;
				if (args.length > 1){
					StringBuffer buffer = new StringBuffer();
					for (int i = 1; i < args.length; i++) {
						buffer.append(" ");
						buffer.append(args[i]);
					}
					msg = buffer.toString();
				} else {
					msg = "none";
				}
				Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
				for (Player oplayer : onlinePlayers){
					plugin.extras.slap(oplayer.getName(),sender,msg);
				}
				return true;

			} else {
				String msg;
				if (args.length > 1){
					StringBuffer buffer = new StringBuffer();
					for (int i = 1; i < args.length; i++) {
						buffer.append(" ");
						buffer.append(args[i]);
					}
					msg = buffer.toString();
				} else {
					msg = "none";
				}
				plugin.extras.slap(args[0],sender,msg);
				return true;
			}
		case ENABLEME:
			if (args.length == 0) {
				plugin.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " is " + ChatColor.GREEN + "ENABLED!");
				return true;
			} else if (args.length == 1) {

				plugin.getServer().broadcastMessage(ChatColor.GOLD + args[0] + " is " + ChatColor.GREEN + "ENABLED!");
				return true;
			}
			return true;

		case DISABLEME:
			if (args.length == 0) {
				plugin.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " is " + ChatColor.RED + "DISABLED!");
				return true;
			} else if (args.length == 1) {
				plugin.getServer().broadcastMessage(ChatColor.GOLD + args[0] + " is " + ChatColor.RED + "DISABLED!");
				return true;
			}
			return true;
		}
		return false;
	}

	public void timer(final World world, final String msg){
		int tasktimer = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				final int minutes = time / 60;
				Player[] players = plugin.getServer().getOnlinePlayers();
				if (time % 60 == 0 && time > 60 && Integer.toString(minutes).endsWith("0")){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " MINUTES!");
						}
					}
				}
				else if (time == 60){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " MINUTE!");
						}
					}
				}
				else if (time == 30){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " SECONDS!");
						}
					}
				}
				else if (time == 15){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " SECONDS!");
						}
					}
				}
				else if (time < 11 && time > 0){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " SECONDS!");
						}
					}
				}
				else if (time == 0){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + msg);
						}
					}
				}
				time--;
			}
		},0L, 20L);
	}

	public void spawnme(final Player player, final int density, final int radius){
		plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
			public void run() {
				for (int i=1; i == density; i++){
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.COW);
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.PIG);
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.CHICKEN);
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.HORSE);
				}
			}
		});
	}

}

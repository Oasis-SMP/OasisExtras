package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class RandomCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public RandomCommand (OasisExtras plugin){
		this.plugin = plugin;
	}
	//BLARGH
	BukkitTask randomTask;
	Location newloc;
	Horse horse;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
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
					getRandomLoc(rplayer, plugin.default_min, plugin.default_max,true);
				} else {
					getRandomLoc(rplayer, plugin.default_min, plugin.default_max,false);
				}
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
				getRandomLoc(player, plugin.default_min, plugin.default_max, true);
			} else {
				getRandomLoc(player, plugin.default_min, plugin.default_max, false);
			}
			return true;
		} else if(args.length==1){
			if (sender instanceof BlockCommandSender){
				Player bcsplayer = plugin.getServer().getPlayer(args[0]);
				World bcsplayerworld = bcsplayer.getWorld();
				bcsplayer.setNoDamageTicks(plugin.ndt);
				if (bcsplayer.isInsideVehicle() && bcsplayer.getVehicle() instanceof Horse) {
					getRandomLoc(bcsplayer, plugin.default_min, plugin.default_max, true);
				} else {
					getRandomLoc(bcsplayer, plugin.default_min, plugin.default_max,false);
				}
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
						getRandomLoc(bcsplayer, plugin.default_min, plugin.default_max,true);
					} else {
						getRandomLoc(bcsplayer, plugin.default_min, plugin.default_max,false);
					}
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
	}
	
	public int randomNum(Integer lownum, double d) { //DO NOT CHANGE THIS FUNCTION AT ALL
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}
	
	
	//public void runBlockCheck()
	//check if player is in vehicle
		// if yes, cancel event competely.
		//else proceed down.
	//check world that player will be TP'd INTO
	//maybe echo world and/or coords?
	//maybe a yes/no option (debuggin only)
	//check for blocks that are NOT safe to be TP'd to
	//then check 
	
	public void getRandomLoc(final Player player, final int min, final int max, final boolean vehicle){
		final Location loc = player.getLocation();
		final World world = player.getWorld();
		
		randomTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){
			@Override
			public void run(){
				double x = loc.getBlockX() + randomNum(min, max)+0.5;
				double z = loc.getBlockZ() + randomNum(min, max)+0.5;
				double y;
				if (world.getEnvironment().equals(World.Environment.NETHER)){
					y = loc.getBlockY() + randomNum(-100, 100);
				} else {
					y = loc.getWorld().getHighestBlockYAt((int)x,(int)z);
				}
				newloc = new Location(world, x, y, z);//Location to tp to, and players bottom half
				 Block newblock=newloc.getBlock();
				 Block underplayer = newblock.getRelative(0,-1,0);//Block under player
				 Block topblock = newblock.getRelative(0,1,0);//player location top
				 
				 if (world.getEnvironment().equals(World.Environment.NETHER)){
					 if (y>0 && y<128) {
							if (newblock.isEmpty()) {
								if (topblock.isEmpty()) {
									if (!underplayer.isLiquid()) {
										if (underplayer.isEmpty()==false) {
											if (!newloc.equals(loc)) {
												if (!underplayer.getType().equals(Material.CACTUS)) {
													player.sendMessage(ChatColor.GOLD + "Prepare for teleport!");
													newloc.getChunk().load();
													plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
														@Override
														public void run(){
															if (vehicle) {
																horse = (Horse) player.getVehicle();
																horse.eject();
															}
															player.teleport(newloc);
															if (vehicle) {
																horse.teleport(player);
																horse.setPassenger(player);
															}
															player.sendMessage(ChatColor.GOLD + "You have been randomly teleported!");
														}
													}, 40L);
													randomTask.cancel();
												}
											}
										}
									}
								}
							}
						}
					} else {
						if (y>0 && y<world.getMaxHeight()) {
							if (newblock.isEmpty()) {
								if (topblock.isEmpty()) {
									if (!underplayer.isLiquid()) {
										if (underplayer.isEmpty()==false) {
											if (!newloc.equals(loc)) {
												if (!underplayer.getType().equals(Material.CACTUS)) {
													player.sendMessage(ChatColor.GOLD + "Prepare for teleport!");
													newloc.getChunk().load();
													plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
														@Override
														public void run(){
															if (vehicle) {
																horse = (Horse) player.getVehicle();
																horse.eject();
															}
															player.teleport(newloc);
															if (vehicle) {
																horse.teleport(player);
																horse.setPassenger(player);
															}
															player.sendMessage(ChatColor.GOLD + "You have been randomly teleported!");
														}
													}, 40L);
													randomTask.cancel();
												}
											}
										}
									}
								}
							}
						}
					}
				 

			}
		},0L, 1L);
	}

}

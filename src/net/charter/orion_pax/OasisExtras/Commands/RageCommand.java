package net.charter.orion_pax.OasisExtras.Commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.charter.orion_pax.OasisExtras.LightningStrike;
import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class RageCommand implements CommandExecutor {
	private OasisExtras plugin;
	BukkitTask task;
	public RageCommand(OasisExtras plugin){
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int count;
		if(args.length==0){
			count = 3;
		} else {
			try {
				count = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Must be a number!");
				return false;
			}
		}
		Player player = (Player) sender;
		HashMap<Location, Material> blocks = new HashMap<Location, Material>();
		for(int x = -count ; x<count+1 ; x++){
			for(int z = -count ; z<count+1 ; z++){
				blocks.put(player.getLocation().add(x, -1, z), player.getLocation().add(x, -1, z).getBlock().getType());
			}
		}
		plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "RUN!!! " + player.getName() + " is RAGING!");
		rage(blocks,player.getLocation());
		return true;
		
		
	}
	
	public void rage(final HashMap<Location, Material> blocks, final Location ploc){
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){
			Iterator it = blocks.entrySet().iterator();
			Location loc;
			Location uloc = ploc.add(0, -1, 0);
			FallingBlock block;
			@Override
			public void run() {
				if (it.hasNext()) {
					Entry entry = (Entry) it.next();
					loc = (Location) entry.getKey();
					block = loc.getWorld().spawnFallingBlock(new Location(loc.getWorld(),loc.getBlockX(),loc.getY()+1,loc.getBlockZ()),(Material) entry.getValue(), (byte) 0);
					block.setVelocity(new Vector(0,2,0));
					LightningStrike ls = new LightningStrike(plugin, ploc, 5L, 1);
					loc.getBlock().setType(Material.AIR);
					uloc.getBlock().setType(Material.COAL_BLOCK);
					removeblock(block,loc,uloc);
				} else {
					restoreblocks(blocks);
					cancel();
				}
			}
			
			public void cancel(){
				task.cancel();
			}
			
		}, 0,5L);
	}
	
	public void restoreblocks(final HashMap<Location,Material> blocks){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				Location loc;
				Iterator restoreIt = blocks.entrySet().iterator();
				while(restoreIt.hasNext()){
					Entry entry = (Entry) restoreIt.next();
					loc = (Location) entry.getKey();
					loc.getBlock().setType((Material) entry.getValue());
				}
				
			}
			
		}, 100L);
	}
	
	public void removeblock(final FallingBlock block,final Location loc, final Location uloc){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				block.remove();
				loc.getBlock().setType(Material.LAVA);
				uloc.getBlock().setType(Material.COAL_BLOCK);
			}
			
		}, 20L);
	}
}

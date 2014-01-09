package net.charter.orion_pax.OasisExtras.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.charter.orion_pax.OasisExtras.LightningStrike;
import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
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
		List<BlockState> bslist = Util.region(player.getLocation().add(count, -1, count),player.getLocation().add(-count, -1, -count));
		List<BlockState> restorelist = Util.region(player.getLocation().add(count+3, 5, count+3),player.getLocation().add(-count-3, -5, -count-3));
		for(BlockState block:restorelist){
			if(block.getBlock().getType().equals(Material.CHEST) || block.getBlock().getType().equals(Material.SIGN_POST) || block.getBlock().getType().equals(Material.SIGN)){
				player.sendMessage(ChatColor.RED + "Unable to rage, near a chest or a sign!");
				return true;
			}
		}
		plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "RUN!!! " + player.getName() + " is RAGING!");
		rage(bslist,player.getLocation(),restorelist);
		return true;
		
		
	}
	
	public void rage(final List<BlockState> state, final Location ploc, final List<BlockState> restorelist){
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

			Iterator<BlockState> it = state.iterator();
			FallingBlock fblock;
			@Override
			public void run() {
				if(it.hasNext()){
					BlockState bstate = it.next();
					fblock = bstate.getWorld().spawnFallingBlock(bstate.getLocation().clone().add(0, 1, 0), bstate.getType(), (byte) 0);
					fblock.setVelocity(new Vector(0,2,0));
					new LightningStrike(plugin, bstate.getLocation(), 5L, 1);
					bstate.getBlock().setType(Material.AIR);
					ploc.clone().add(0, -1, 0).getBlock().setType(Material.COAL_BLOCK);
					removeblock(fblock,bstate,ploc);
				} else {
					restoreblocks(restorelist);
					task.cancel();
				}
				
			}
			
		}, 0, 5L);
	}
	
	public void restoreblocks(final List<BlockState> state){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				for(BlockState block:state){
					block.update(true);
				}
				
			}
			
		}, 100L);
	}
	
	public void removeblock(final FallingBlock block,final BlockState bstate, final Location uloc){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				block.remove();
				bstate.getBlock().setType(randomMat());
				uloc.clone().add(0, -1, 0).getBlock().setType(Material.COAL_BLOCK);
			}
			
		}, 20L);
	}
	
	public Material randomMat(){
		switch(Util.randomNum(1,5)){
		case 1:return Material.AIR;
		case 2:return Material.LAVA;
		case 3:return Material.OBSIDIAN;
		case 4:return Material.COAL_BLOCK;
		case 5:return Material.FIRE;
		default:return Material.LAVA;
		}
	}
}

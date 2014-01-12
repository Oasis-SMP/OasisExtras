package net.charter.orion_pax.OasisExtras.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShitCommand implements CommandExecutor{

	private OasisExtras plugin;

	public ShitCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length>0){
			Player player = plugin.getServer().getPlayer(args[0].toString());
			if(player.equals(null)){
				sender.sendMessage(ChatColor.RED + args[0].toString() + " is not online!");
				return false;
			}
			World world = player.getWorld();
			List<BlockState> state = new ArrayList<BlockState>();
			state.add(world.getBlockAt(player.getLocation().add(0, 2, 0)).getState());
			state.add(world.getBlockAt(player.getLocation().add(1, 0, 0)).getState());
			state.add(world.getBlockAt(player.getLocation().add(1, 1, 0)).getState());
			state.add(world.getBlockAt(player.getLocation().add(-1, 0, 0)).getState());
			state.add(world.getBlockAt(player.getLocation().add(-1, 1, 0)).getState());
			state.add(world.getBlockAt(player.getLocation().add(0, 0, 1)).getState());
			state.add(world.getBlockAt(player.getLocation().add(0, 1, 1)).getState());
			state.add(world.getBlockAt(player.getLocation().add(0, 0, -1)).getState());
			state.add(world.getBlockAt(player.getLocation().add(0, 1, -1)).getState());
			state.add(world.getBlockAt(player.getLocation().add(0, -1, 0)).getState());
			for(BlockState block:state){
				if(block.getBlock().getType().equals(Material.CHEST) || block.getBlock().getType().equals(Material.SIGN_POST) || block.getBlock().getType().equals(Material.SIGN)){
					sender.sendMessage(ChatColor.RED + "Experiencing constipation....aka...player is near a chest or sign!");
					return true;
				}
			}
			restore(state);
			for(BlockState block:state){
				if (block.getBlock().getType().equals(Material.AIR)) {
					block.getBlock().setType(Material.SOUL_SAND);
					state.get(0).getBlock().setType(Material.GLOWSTONE);
				}
			}
			player.sendMessage(ChatColor.GREEN + sender.getName() + " has shat on you!");
			sender.sendMessage(ChatColor.GREEN + "You just shat on " + player.getName() + "!");
			return true;
		}
		return false;

	}

	public void restore(final List<BlockState> state){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(BlockState block:state){
					block.update(true);
				}
			}

		}
		, 100L);
	}
}

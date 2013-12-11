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
			HashMap<Location,Material> blocks = new HashMap<Location,Material>();
			blocks.put(player.getLocation().add(0, 2, 0), world.getBlockAt(player.getLocation().add(0, 2, 0)).getType());
			blocks.put(player.getLocation().add(1, 0, 0), world.getBlockAt(player.getLocation().add(1, 0, 0)).getType());
			blocks.put(player.getLocation().add(1, 1, 0), world.getBlockAt(player.getLocation().add(1, 1, 0)).getType());
			blocks.put(player.getLocation().add(-1, 0, 0), world.getBlockAt(player.getLocation().add(-1, 0, 0)).getType());
			blocks.put(player.getLocation().add(-1, 1, 0), world.getBlockAt(player.getLocation().add(-1, 1, 0)).getType());
			blocks.put(player.getLocation().add(0, 0, 1), world.getBlockAt(player.getLocation().add(0, 0, 1)).getType());
			blocks.put(player.getLocation().add(0, 1, 1), world.getBlockAt(player.getLocation().add(0, 1, 1)).getType());
			blocks.put(player.getLocation().add(0, 0, -1), world.getBlockAt(player.getLocation().add(0, 0, -1)).getType());
			blocks.put(player.getLocation().add(0, 1, -1), world.getBlockAt(player.getLocation().add(0, 1, -1)).getType());
			blocks.put(player.getLocation().add(0, -1, 0), world.getBlockAt(player.getLocation().add(0, -1, 0)).getType());
			restore(blocks);
			Iterator it = blocks.entrySet().iterator();
			while (it.hasNext()){
				Entry entry = (Entry) it.next();
				Location loc = (Location) entry.getKey();
				if(it.hasNext()){
					loc.getBlock().setType(Material.SOUL_SAND);
				} else {
					loc.getBlock().setType(Material.GLOWSTONE);
				}
			}
			player.sendMessage(ChatColor.GREEN + sender.getName() + " has shat on you!");
			sender.sendMessage(ChatColor.GREEN + "You just shat on " + player.getName() + "!");
			return true;
		}
		return false;

	}

	public void restore(final HashMap<Location, Material> blocks){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Iterator it = blocks.entrySet().iterator();
				while (it.hasNext()){
					Entry entry = (Entry) it.next();
					Location loc = (Location) entry.getKey();
					loc.getBlock().setType((Material) entry.getValue());
				}
			}

		}
		, 100L);
	}
}

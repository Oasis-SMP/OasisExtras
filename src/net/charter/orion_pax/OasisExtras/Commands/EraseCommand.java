package net.charter.orion_pax.OasisExtras.Commands;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EraseCommand implements CommandExecutor {

	int radius,x,y,z;
	private Location loc;
	private World world;
	private Vector origin;
	private OasisExtras plugin;
	private List<Entity> list;
	private Iterator<Entity> it;
	public EraseCommand(OasisExtras plugin) {
		this.plugin=plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==4){
			final Player player = (Player) sender;
			try {
				x=Integer.parseInt(args[0]);
				y=Integer.parseInt(args[1]);
				z=Integer.parseInt(args[2]);
				radius=Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Check your arguments! One of them is not a number!");
				return false;
			}
			loc=new Location(player.getWorld(),x,y,z);
			world=loc.getWorld();
			plugin.eList.add(world.getChunkAt(x, z));
			world.loadChunk(x, z);
			for(int x=-radius;x<radius; x = x + 16){
				for(int z=-radius;z<radius;z=z+16){
					plugin.eList.add(world.getChunkAt(this.x + x, this.z + z));
					world.loadChunk(this.x + x, this.z + z);
				}
			}
			plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

				@Override
				public void run() {
					player.sendMessage(ChatColor.RED + Integer.toString(removeEntities(loc.toVector(),radius)) + " items removed!");
					plugin.eList.clear();
				}
				
			}, 100L);
			return true;
			

		}
		sender.sendMessage(ChatColor.RED + "Check your arguments!");
		return false;
	}
	
	private int removeEntities(Vector origin, int radius){
		int num = 0;
		double radiusSq = Math.pow(radius, 2);
		for(Entity ent : world.getEntities()){
			if(origin.distanceSquared(ent.getLocation().toVector()) > radiusSq){
				continue;
			}
			if(ent instanceof Item){
				ent.remove();
				num++;
			}
		}
		return num;
	}
}

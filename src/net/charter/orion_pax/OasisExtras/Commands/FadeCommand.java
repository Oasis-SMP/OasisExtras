package net.charter.orion_pax.OasisExtras.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class FadeCommand implements CommandExecutor {

	private OasisExtras plugin;
	public FadeCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}
	private Iterator<BlockState> it;
	private List<BlockState> list = new ArrayList<BlockState>();
	private BukkitTask task,task2;

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
		if(args.length==1){
			final Player player = plugin.getServer().getPlayer(args[0]);
			if(player==null){
				sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
				return true;
			} else {
				list=region(player.getLocation().clone().add(10, 10, 10),player.getLocation().clone().add(-10, -10, -10));
				it = list.iterator();
				task=plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

					@Override
					public void run() {
						for (int i = 1; i < 10; i++) {
							if (it.hasNext()) {
								BlockState block = it.next();
								if (isGood(block.getBlock().getType())) {
									player.sendBlockChange(block.getBlock().getLocation(), Material.AIR, (byte) 0);
								}
							} else {
								sender.sendMessage(ChatColor.RED + "Finished faking the block changes on " + player.getName());
								task.cancel();
							}
						}
					}
					
				}, 0, 1L);
				return true;
			}
		}
		return false;
	}
	
	public boolean isGood(Material material){
		if(material.equals(Material.DIRT)){
			return false;
		}
		if(material.equals(Material.GRASS)){
			return false;
		}
		if(material.equals(Material.STONE)){
			return false;
		}
		if(material.equals(Material.GRAVEL)){
			return false;
		}
		if(material.equals(Material.SAND)){
			return false;
		}
		if(material.equals(Material.AIR)){
			return false;
		}
		if(material.equals(Material.CROPS)){
			return false;
		}
		if(material.equals(Material.DOUBLE_PLANT)){
			return false;
		}
		if(material.equals(Material.LONG_GRASS)){
			return false;
		}
		if(material.equals(Material.LEAVES)){
			return false;
		}
		if(material.equals(Material.LEAVES_2)){
			return false;
		}
		if(material.equals(Material.BED_BLOCK)){
			return false;
		}
		return true;
	}
	
	public static List<BlockState> region(Location loc1, Location loc2)
    {
        List<BlockState> blocks = new ArrayList<BlockState>();
 
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
 
        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
 
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
 
        for(int x = bottomBlockX; x <= topBlockX; x++)
        {
            for(int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for(int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);
                   
                    blocks.add(block.getState());
                }
            }
        }
       
        return blocks;
    }

}

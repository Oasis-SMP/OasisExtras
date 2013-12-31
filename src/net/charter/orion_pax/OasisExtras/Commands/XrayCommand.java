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

public class XrayCommand implements CommandExecutor {

	private OasisExtras plugin;
	public XrayCommand(OasisExtras plugin) {
		this.plugin=plugin;
	}
	private int radius = 10;
	
	private List<BlockState> list = new ArrayList<BlockState>();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==1){
			try {
				radius=Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + args[0] + " is not a number!");
				return true;
			}
		}
		list = region(((Player)sender).getLocation().clone().add(radius, radius, radius),((Player)sender).getLocation().clone().add(-radius, -radius, -radius));
		Iterator<BlockState> it = list.iterator();
		while(it.hasNext()){
			BlockState block = it.next();
			if (isGood(block.getType())) {
				((Player) sender).sendBlockChange(block.getLocation(), Material.GLASS, (byte) 0);
			}
		}
		return true;
	}
	
	public boolean isGood(Material material){
		if(material.equals(Material.DIRT)){
			return true;
		}
		if(material.equals(Material.GRASS)){
			return true;
		}
		if(material.equals(Material.STONE)){
			return true;
		}
		if(material.equals(Material.GRAVEL)){
			return true;
		}
		if(material.equals(Material.SAND)){
			return true;
		}
		return false;      
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

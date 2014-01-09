package net.charter.orion_pax.OasisExtras.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		list = Util.region(((Player)sender).getLocation().clone().add(radius, radius, radius),((Player)sender).getLocation().clone().add(-radius, -radius, -radius));
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
}

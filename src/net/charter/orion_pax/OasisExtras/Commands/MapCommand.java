package net.charter.orion_pax.OasisExtras.Commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;
import net.charter.orion_pax.OasisExtras.Map.ImgUtility;
import net.charter.orion_pax.OasisExtras.Map.SpotTreatementMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MapCommand implements CommandExecutor {

	private OasisExtras plugin;
	public MapView view;
	public Player player;
	public String url;
	public Inventory inv;
	public MapCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		player = (Player) sender;
		short id;
		MapView map;
		inv = player.getInventory();
		if(args.length==0){
			return false;
		} else if (args.length==1){
			if(args[0].equalsIgnoreCase("get")){
				Util.SendMsg(player, "&6Usage: /map get #ID");
				return true;
			} else if(args[0].equalsIgnoreCase("list")){
				String msg = "";
				int compute = 0;
				ArrayList<String> list = new ArrayList<String>();
				list = ImgUtility.getListMapByPlayer(plugin, player.getName());
				for(;compute < list.size();compute++){
					msg += list.get(compute)+ ", ";
				}
				Util.SendMsg(player, msg + "\nYou have rendered "+ ChatColor.DARK_PURPLE+ (compute + 1)+ ChatColor.RESET+ " pictures");
				return true;
			} else if(args[0].equalsIgnoreCase("delete")){
				Util.SendMsg(player, "&6Usage: /map delete #ID");
				return true;
			}
		} else if (args.length==2){
			if(args[0].equalsIgnoreCase("get")){
				try {
					id = Short.parseShort(args[1]);
				} catch (NumberFormatException e) {
					Util.SendMsg(player, "&4" + args[1] + " is not a number!");
					return true;
				}

				map = ImgUtility.getMap(plugin,id);

				if(map ==null){
					Util.SendMsg(player, "&4Error: This map doesnt exists!");
					return true;
				}

				if(inv.firstEmpty() == -1)
				{
					Util.SendMsg(player,"Your inventory is full, you can't take the map !");
					return true;
				}

				inv.addItem(new ItemStack(Material.MAP, 1, map.getId()));
				player.sendMap(map);
				player.sendMessage("Map "+ ChatColor.ITALIC+ id+ ChatColor.RESET+ " was added in your inventory.");
				return true;

			} else if(args[0].equalsIgnoreCase("delete")){
				try
				{
					id = Short.parseShort(args[1]);
				}
				catch(NumberFormatException err)
				{
					Util.SendMsg(player,"&cYou must enter a number !");
					return true;
				}

				boolean success = ImgUtility.RemoveMap(plugin, id);

				if(success)
				{
					Util.SendMsg(player,"&bMap#"+ id+ " was deleted");
					return true;
				}
				else
				{
					Util.SendMsg(player,"&4Can't delete delete Map#"+ id+ "!");
					return true;
				}
			}
		}
		SpotTreatementMap spot = new SpotTreatementMap(player,args[0],plugin);
		spot.runTaskTimer(plugin, 0, 10);

		return true;
	}

}

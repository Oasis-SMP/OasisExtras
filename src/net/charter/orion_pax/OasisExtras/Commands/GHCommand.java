package net.charter.orion_pax.OasisExtras.Commands;

import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GHCommand implements CommandExecutor {

	private OasisExtras plugin;
	public GHCommand(OasisExtras plugin) {
		this.plugin=plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length==0){
			Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh set - Sets the region for Grief house!");
			Util.SendMsg(player,"&6[&aGH&6] - &aUsage: /gh tools - Gives you the 2 blocks to set the region with!");
			Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh reset - Resets the region, will remove all blocks and drop them naturally!");
			Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh inven - Opens up the Griefhouse inventory from a reset!");
			Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh clear - Clears the griefhouse inventory!");
			Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh - This info here!");
			return true;
		} else if(args.length==1){
			switch(args[0]){
			case "clear":
				plugin.griefhouse.clear();
				Util.SendMsg(player, "&6[&aGH&6] - &aGriefhouse inventory cleared!");
				return true;
			case "inven":
				player.openInventory(plugin.griefhouse);
				return true;
			case "set":
				if (plugin.ghpos1!=null && plugin.ghpos2!=null) {
					plugin.getConfig().set("ghpos1.world", plugin.ghpos1.getWorld().getName());
					plugin.getConfig().set("ghpos1.x", plugin.ghpos1.getBlockX());
					plugin.getConfig().set("ghpos1.y", plugin.ghpos1.getBlockY());
					plugin.getConfig().set("ghpos1.z", plugin.ghpos1.getBlockZ());
					
					plugin.getConfig().set("ghpos2.world", plugin.ghpos2.getWorld().getName());
					plugin.getConfig().set("ghpos2.x", plugin.ghpos2.getBlockX());
					plugin.getConfig().set("ghpos2.y", plugin.ghpos2.getBlockY());
					plugin.getConfig().set("ghpos2.z", plugin.ghpos2.getBlockZ());
					plugin.ghpos1=null;
					plugin.ghpos2=null;
					Util.SendMsg(player, "&6[&aGH&6] - &aGriefhouse markers set!");
					return true;
				} else {
					Util.SendMsg(player, "&6[&aGH&6] - &aOne of your markers is not set!");
					return true;
				}
			case "tools":
				ItemStack item = new ItemStack(Material.WOOL,1);
				ItemStack item2 = item;
				ItemMeta itemmeta = item.getItemMeta();
				itemmeta.setDisplayName("ghpos1");
				item.setItemMeta(itemmeta);
				player.getInventory().addItem(item);
				itemmeta.setDisplayName("ghpos2");
				item2.setItemMeta(itemmeta);
				player.getInventory().addItem(item2);
				Util.SendMsg(player, "&6[&aGH&6] - &aGriefhouse markers given!");
				return true;
			case "reset":
				if(plugin.getConfig().contains("ghpos1") && plugin.getConfig().contains("ghpos2")){
					Location loc1 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("ghpos1.world")),plugin.getConfig().getInt("ghpos1.x"),plugin.getConfig().getInt("ghpos1.y"),plugin.getConfig().getInt("ghpos1.z"));
					Location loc2 = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("ghpos2.world")),plugin.getConfig().getInt("ghpos2.x"),plugin.getConfig().getInt("ghpos2.y"),plugin.getConfig().getInt("ghpos2.z"));
					List<BlockState> bslist = Util.region(loc1, loc2);
					for(BlockState bs:bslist){
						if(!bs.getType().equals(Material.AIR)){
							plugin.griefhouse.addItem(new ItemStack(bs.getType(),1));
							plugin.CoreProtect.logRemoval("GH_Command", bs.getLocation(), bs.getTypeId(), bs.getBlock().getData());
							bs.getBlock().setType(Material.AIR);
						}
					}
					Util.SendMsg(player, "&6[&aGH&6] - &aGriefhouse is reset!");
					return true;
				} else {
					Util.SendMsg(player, "&6[&aGH&6] - &aOne of your markers is not set!");
					return true;
				}
			default:
				Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh set - Sets the region for Grief house!");
				Util.SendMsg(player,"&6[&aGH&6] - &aUsage: /gh tools - Gives you the 2 blocks to set the region with!");
				Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh reset - Resets the region, will remove all blocks and drop them naturally!");
				Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh inven - Opens up the Griefhouse inventory from a reset!");
				Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh clear - Clears the griefhouse inventory!");
				Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh - This info here!");
				return true;
			}
		}
		Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh set - Sets the region for Grief house!");
		Util.SendMsg(player,"&6[&aGH&6] - &aUsage: /gh tools - Gives you the 2 blocks to set the region with!");
		Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh reset - Resets the region, will remove all blocks and drop them naturally!");
		Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh inven - Opens up the Griefhouse inventory from a reset!");
		Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh clear - Clears the griefhouse inventory!");
		Util.SendMsg(player, "&6[&aGH&6] - &aUsage: /gh - This info here!");
		return true;
	}

}

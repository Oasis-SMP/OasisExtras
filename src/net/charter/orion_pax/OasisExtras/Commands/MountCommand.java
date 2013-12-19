package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class MountCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public MountCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("mountme")){
			if (args.length==1){
				Player vehicle = (Player) sender;
				
				Player player = plugin.getServer().getPlayer(args[0]);
				if(player==null){
					sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
					return false;
				}
				vehicle.teleport(player);
				if(vehicle.setPassenger(player)){
					sender.sendMessage(ChatColor.GOLD + "You have been mounted by " + player.getName() + "!");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + player.getName() + " could not mount you!");
					return false;
				}
				
			} else {
				sender.sendMessage(ChatColor.GOLD + "Usage: /mountme playername");
				return true;
			}
		}
		if (args.length==1){
			Player vehicle = plugin.getServer().getPlayer(args[0]);
			if(vehicle==null){
				sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
				return false;
			}
			Player player = (Player) sender;
			
			player.teleport(vehicle);
			if(vehicle.setPassenger(player)){
				sender.sendMessage(ChatColor.GOLD + "You have mounted " + vehicle.getName() + "!");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + vehicle.getName() + " could not be mounted!");
				return false;
			}
			
		} else {
			sender.sendMessage(ChatColor.GOLD + "Usage: /mount playername");
			return true;
		}
	}
	
	

}

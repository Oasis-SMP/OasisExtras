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
	
	Entity vehicle;
	Entity passenger;
	
	public MountCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length==1){
			vehicle=Bukkit.getPlayer(args[0]);
			passenger = (Entity) sender;
			if ((vehicle!=null) && (sender instanceof Player)){
				vehicle.setPassenger(passenger);
				sender.sendMessage(ChatColor.GOLD + "You have mounted " + Bukkit.getPlayer(args[0]).getName());
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.GOLD + "Usage: /mount playername");
			return true;
		}
		return false;
	}
	
	

}

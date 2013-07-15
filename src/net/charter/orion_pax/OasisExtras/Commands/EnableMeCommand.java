package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EnableMeCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public EnableMeCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			plugin.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " is " + ChatColor.GREEN + "ENABLED!");
			return true;
		} else if (args.length == 1) {

			plugin.getServer().broadcastMessage(ChatColor.GOLD + args[0] + " is " + ChatColor.GREEN + "ENABLED!");
			return true;
		}
		return true;
	}
	
	

}

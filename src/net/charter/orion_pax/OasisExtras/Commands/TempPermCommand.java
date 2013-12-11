package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempPermCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public TempPermCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length<2){
			sender.sendMessage(ChatColor.RED + "Too few arguements!");
			return false;
		}
		
		Player player = plugin.getServer().getPlayer(args[0]);
		if(player.equals(null)){
			sender.sendMessage(ChatColor.RED + args[0].toString() + " isnt online!");
			return false;
		}
		player.addAttachment(plugin, args[1].toString(), true);
		sender.sendMessage(ChatColor.GOLD + "Permnode is temporary.  Will be removed when they log out!");
		return true;
		
	}
}

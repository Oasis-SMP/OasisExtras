package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class ListSubCommand {

	private OasisExtras plugin;
	
	public ListSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		int count=0;
		for (String msg : this.plugin.getConfig().getStringList("broadcastmessages")){
			count++;
			String thismsg = ChatColor.GOLD + "[" + ChatColor.BLUE + count + ChatColor.GOLD + "] - " + msg;
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', thismsg));
		}
	}
}

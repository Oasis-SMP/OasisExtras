package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class ListSubCommand {

	private OasisExtras plugin;
	
	public ListSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		sender.sendMessage(ChatColor.GOLD + "In game editable sections:");
		List<String> configsection = (List<String>) plugin.getConfig().getConfigurationSection("oasisextras");
		for(String msg:configsection){
			if(msg!="broadcastmessages"){
				sender.sendMessage(msg);
			}
		}
		sender.sendMessage(ChatColor.GOLD + "Broadcast messages:");
		int count=0;
		for (String msg : this.plugin.getConfig().getStringList("broadcastmessages")){
			count++;
			String thismsg = ChatColor.GOLD + "[" + ChatColor.BLUE + count + ChatColor.GOLD + "] - " + msg;
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', thismsg));
		}
	}
}

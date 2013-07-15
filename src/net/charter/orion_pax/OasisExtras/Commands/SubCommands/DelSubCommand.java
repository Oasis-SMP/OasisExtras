package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class DelSubCommand {

	private OasisExtras plugin;
	public DelSubCommand(OasisExtras plugin,CommandSender sender,int index){
		this.plugin = plugin;
		List<String> msglist = plugin.getConfig().getStringList("broadcastmessages");
		msglist.remove(index-1);
		plugin.getConfig().set("broadcastmessages", msglist);
		plugin.bcastmsgs = plugin.getConfig().getStringList("broadcastmessages");
		sender.sendMessage(ChatColor.GOLD + "Message deleted!");
	}
}

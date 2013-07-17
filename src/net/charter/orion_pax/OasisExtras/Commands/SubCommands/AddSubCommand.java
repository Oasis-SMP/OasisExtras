package net.charter.orion_pax.OasisExtras.Commands.SubCommands;


import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class AddSubCommand {

	private OasisExtras plugin;
	public AddSubCommand(OasisExtras plugin,CommandSender sender, String msg){
		this.plugin = plugin;
		List<String> msglist = plugin.getConfig().getStringList("broadcastmessages");
		msglist.add(msg);
		plugin.getConfig().set("broadcastmessages", msglist);
		plugin.bcastmsgs = plugin.getConfig().getStringList("broadcastmessages");
		sender.sendMessage(ChatColor.GOLD + "New message added to broadcast rotation!");
		plugin.saveConfig();
	}
}

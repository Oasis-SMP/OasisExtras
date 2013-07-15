package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class StartSubCommand {

	private OasisExtras plugin;
	
	public StartSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		
			if (plugin.taskdisabled) {
				plugin.setup();
				plugin.taskdisabled = false;
				sender.sendMessage(ChatColor.GOLD + "All task restarted!");
			}
	}
}

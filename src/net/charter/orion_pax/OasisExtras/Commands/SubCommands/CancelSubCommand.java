package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CancelSubCommand {

	private OasisExtras plugin;
	
	public CancelSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		
		if (!plugin.taskdisabled) {
			plugin.getServer().getScheduler().cancelTasks(plugin);
			sender.sendMessage(ChatColor.GOLD + "All task cancelled!");
			plugin.taskdisabled = true;
		}
	}
}

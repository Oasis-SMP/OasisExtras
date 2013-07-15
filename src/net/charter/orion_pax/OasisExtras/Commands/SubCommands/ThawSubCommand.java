package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class ThawSubCommand {

	private OasisExtras plugin;
	
	public ThawSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		
		plugin.frozenfile.getConfig().set("frozen", null);
		if (!plugin.frozenfile.getConfig().contains("frozen")){
			plugin.frozenfile.getConfig().createSection("frozen");
		}
		sender.sendMessage(ChatColor.GOLD + "All " + ChatColor.AQUA + "FROZEN " + ChatColor.BLUE + "THAWED!");
	}
}

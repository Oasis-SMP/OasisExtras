package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class ReloadSubCommand {

	private OasisExtras plugin;
	
	public ReloadSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		
		plugin.getServer().getScheduler().cancelTasks(plugin);
		plugin.getServer().getPluginManager().disablePlugin(plugin);
		plugin.reloadConfig();
		plugin.getServer().getPluginManager().enablePlugin(plugin);
		plugin.setup();
		sender.sendMessage(ChatColor.GOLD + "Config reloaded!");
	}
}

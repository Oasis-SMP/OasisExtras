package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class ReloadSubCommand {

	private OasisExtras plugin;
	
	public ReloadSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		
		this.plugin.getServer().getScheduler().cancelTasks(plugin);
		this.plugin.getServer().getPluginManager().disablePlugin(plugin);
		this.plugin.reloadConfig();
		this.plugin.getServer().getPluginManager().enablePlugin(plugin);
		this.plugin.task.reload();
		sender.sendMessage(ChatColor.GOLD + "Config reloaded!");
	}
}

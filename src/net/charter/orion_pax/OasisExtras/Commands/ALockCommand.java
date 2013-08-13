package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Commands.SubCommands.AlockAddCommand;
import net.charter.orion_pax.OasisExtras.Commands.SubCommands.AlockDelCommand;
import net.charter.orion_pax.OasisExtras.Commands.SubCommands.AlockListCommand;

public class ALockCommand implements CommandExecutor {

	private OasisExtras plugin;
	public ALockCommand(OasisExtras plugin){
		this.plugin=plugin;
	}
	
	public BukkitTask task;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length==0){
			return true;
		}
		
		if (args.length==2) {
			if (args[0].equalsIgnoreCase("add")) {
				Player addplayer = plugin.getServer().getPlayer(args[1]);
				if (addplayer!=null) {
					AlockAddCommand marking = new AlockAddCommand(plugin, player.getName(), addplayer.getName());
					return true;
				}
			}
		}
		
		if (args.length==2) {
			if (args[0].equalsIgnoreCase("del")) {
				Player delplayer = plugin.getServer().getPlayer(args[1]);
				if (delplayer!=null) {
					AlockDelCommand marking = new AlockDelCommand(plugin, player.getName(), delplayer.getName());
					return true;
				}
			}
		}
		
		if (args.length==1) {
			if (args[0].equalsIgnoreCase("list")){
				new AlockListCommand(plugin, plugin.oasisplayer.get(player.getName()));
			}
		}
		
		return false;
	}
}

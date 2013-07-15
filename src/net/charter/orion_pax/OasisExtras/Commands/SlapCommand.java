package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SlapCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public SlapCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)){
			plugin.getServer().broadcastMessage(ChatColor.RED + "CONSOLE has slapped " + args[0]);
		}
		if (args.length == 0){
			return false;
		}

		if (args[0].equalsIgnoreCase("all")){
			String msg;
			if (args.length > 1){
				StringBuffer buffer = new StringBuffer();
				for (int i = 1; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				msg = buffer.toString();
			} else {
				msg = "none";
			}
			Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
			for (Player oplayer : onlinePlayers){
				plugin.extras.slap(oplayer.getName(),sender,msg);
			}
			return true;

		} else {
			String msg;
			if (args.length > 1){
				StringBuffer buffer = new StringBuffer();
				for (int i = 1; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				msg = buffer.toString();
			} else {
				msg = "none";
			}
			plugin.extras.slap(args[0],sender,msg);
			return true;
		}
	}
	
	

}

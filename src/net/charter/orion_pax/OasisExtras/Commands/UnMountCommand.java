package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnMountCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public UnMountCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (player.isInsideVehicle()) {
			player.leaveVehicle();
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Not riding a player!");
		return true;
	}
	
	

}

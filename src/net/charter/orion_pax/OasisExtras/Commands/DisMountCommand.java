package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisMountCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public DisMountCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		for (Player oplayer : plugin.getServer().getOnlinePlayers()){
			if(oplayer.isInsideVehicle()){
				if(oplayer.getVehicle() instanceof Player){
					Player vehicle = (Player) oplayer.getVehicle();
					if (player.equals(vehicle)){
						oplayer.leaveVehicle();
						oplayer.sendMessage(Util.ColorChat("&cGet off my back!"));
						return true;
					}
				}
			}
		}
		sender.sendMessage(ChatColor.RED + "No one is riding you!");
		return true;
	}
	
	

}

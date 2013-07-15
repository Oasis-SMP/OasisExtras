package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.LightningStrike;
import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ThunderStruckCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public ThunderStruckCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length==1){
			Location loc = player.getLocation();
			try {
				if (Integer.parseInt(args[0])>30){
					sender.sendMessage(ChatColor.RED + "Must be 30 or less!");
					return true;
				}
				LightningStrike ls = new LightningStrike(plugin, loc, 5L, Integer.parseInt(args[0]));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				sender.sendMessage(ChatColor.RED + "Must be an integer!");
				return true;
			}
			return true;
		}
		return false;
	}
	
	

}

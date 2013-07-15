package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public FreezeCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			if (sender.getServer().getPlayer(args[0]) != null) {
				Player target = sender.getServer().getPlayer(args[0]);
				if (target.hasPermission("OasisChat.staff.a")) {
					sender.sendMessage("Can not freeze staff");
				} else {
					if (plugin.frozen.containsKey(target.getName())) {
						plugin.frozen.remove(target.getName());
						sender.sendMessage(ChatColor.RED + target.getName() + ChatColor.BLUE + " is now THAWED!");
						target.sendMessage(ChatColor.GOLD + "You are now " + ChatColor.BLUE + "THAWED!");
						plugin.removefrozen(target);
						return true;
					} else {
						plugin.frozen.put(target.getName(),target.getLocation());
						sender.sendMessage(ChatColor.RED + target.getName() + ChatColor.AQUA + " is now FROZEN!");
						target.sendMessage(ChatColor.GOLD + "You are now " + ChatColor.AQUA + "FROZEN!");
						plugin.savefrozen(target);
						return true;
					}
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + args[0] + " is not online!");
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			return false;
		}
		return true;
	}
}

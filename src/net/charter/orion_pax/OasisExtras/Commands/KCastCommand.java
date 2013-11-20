package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class KCastCommand implements CommandExecutor {
	private OasisExtras plugin;
	public KCastCommand(OasisExtras plugin){
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(args[0]);
			for (int i = 1; i < args.length; i++) {
				buffer.append(" ");
				buffer.append(args[i]);
			}
			String message = buffer.toString();
			plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&4[&fKorea&9Cast&4]&f " + message));
			return true;
		} else {
			return false;
		}
	}
}

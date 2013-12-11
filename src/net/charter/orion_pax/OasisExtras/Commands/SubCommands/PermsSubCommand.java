package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import java.util.Iterator;
import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public class PermsSubCommand {

	private OasisExtras plugin;

	public PermsSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		
		List<Permission> permList = plugin.getDescription().getPermissions();
		Iterator permIt = permList.iterator();
		while(permIt.hasNext()){
			Permission perm = (Permission) permIt.next();
			sender.sendMessage(ChatColor.RED + perm.getName() + ChatColor.GOLD + " - " + ChatColor.GRAY + perm.getDescription());
		}
		return;
		
	}
}

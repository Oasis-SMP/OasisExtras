package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class ALDelCommand implements CommandExecutor {
	
	private OasisExtras plugin;
	public ALDelCommand(OasisExtras plugin){
		this.plugin=plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
}

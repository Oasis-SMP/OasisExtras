package net.charter.orion_pax.OasisExtras.Commands;

import java.io.File;
import java.io.FilenameFilter;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FindSchemCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public FindSchemCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length>0){
			final String fname = args[0].toString();
			File f = new File("plugins/worldedit/schematics");
			File[] matchingFiles = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.contains(fname);
				}
			});
			for (File file : matchingFiles){
				sender.sendMessage(file.getName());
			}
			return true;
		}
		return false;
	}
	
	

}

package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Commands.SubCommands.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OECommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public OECommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length==0){
			sender.sendMessage(plugin.oasisextrassub);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reload")){
			ReloadSubCommand reload = new ReloadSubCommand(plugin,sender);
		}
		
		if (args[0].equalsIgnoreCase("cancel")){
			CancelSubCommand cancel = new CancelSubCommand(plugin,sender);
		}
		
		if (args[0].equalsIgnoreCase("start")){
			StartSubCommand start = new StartSubCommand(plugin,sender);
		}
		
		if (args[0].equalsIgnoreCase("add")){
			if (sender.hasPermission("oasisextras.staff.bcastadd")) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[1]);
				for (int i = 2; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				String message = buffer.toString();
				AddSubCommand add = new AddSubCommand(plugin, sender, message);
			}
		}
		
		if (args[0].equalsIgnoreCase("del")){
			if (sender.hasPermission("oasisextras.staff.bcastdel")) {
				
				try { 
					Integer.parseInt(args[1]); 
				} catch(NumberFormatException e) { 
					sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
					return false; 
				}
				DelSubCommand del = new DelSubCommand(plugin, sender, Integer.parseInt(args[1]));
			}
		}
		
		if (args[0].equalsIgnoreCase("list")){
			if (sender.hasPermission("oasisextras.staff.bcastlist")) {
				ListSubCommand list = new ListSubCommand(plugin, sender);
			}
		}
		return false;
	}
}

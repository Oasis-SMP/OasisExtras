package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCMDCommand implements CommandExecutor {

	private OasisExtras plugin;
	public SetCMDCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==0){
			return false;
		}
		Player player = (Player) sender;
		CommandBlock cmdB = (CommandBlock) player.getTargetBlock(null, 30).getState();
		StringBuilder command = new StringBuilder();
		for(String string:args){
			command.append(string + " ");
		}
		command.deleteCharAt(command.length()-1);
		cmdB.setCommand(command.toString());
		cmdB.update(true);
		return true;
	}

}

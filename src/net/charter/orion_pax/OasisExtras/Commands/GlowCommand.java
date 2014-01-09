package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlowCommand implements CommandExecutor {

	private OasisExtras plugin;

	public GlowCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		Player player = (Player) sender;
		Util.getOPlayer(plugin, player.getName()).toggleGlow();
		return true;
	}

}

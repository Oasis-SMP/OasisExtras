package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscoCommand implements CommandExecutor {

	private OasisExtras plugin;
	public DiscoCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		OasisPlayer oPlayer = plugin.oasisplayer.get(sender.getName());
		if(oPlayer.disco){
			oPlayer.stopDisco();
			oPlayer.disco=!oPlayer.disco;
			oPlayer.SendMsg("&dDisco &cDisabled!");
			return true;
		} else {
			oPlayer.Disco();
			oPlayer.SendMsg("&dDisco &aEnabled!");
			oPlayer.disco=!oPlayer.disco;
			return true;
		}
	}

}

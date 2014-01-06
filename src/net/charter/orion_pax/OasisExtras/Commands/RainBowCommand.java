package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RainBowCommand implements CommandExecutor {

	private OasisExtras plugin;
	public RainBowCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		OasisPlayer oPlayer = plugin.oasisplayer.get(((Player)sender).getName());
		if(oPlayer.rCA){
			oPlayer.stopRandomColorArmor();
			oPlayer.rCA=!oPlayer.rCA;
			oPlayer.SendMsg("&6Rainbow armor &cDISABLED&6!");
		} else {
			oPlayer.armorRandomColorChange();
			oPlayer.rCA=!oPlayer.rCA;
			oPlayer.SendMsg("&6Rainbow armor &aENABLED&6!");
		}
		return true;
	}

}

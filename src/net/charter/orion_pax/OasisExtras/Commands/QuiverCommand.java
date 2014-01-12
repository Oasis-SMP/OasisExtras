package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuiverCommand implements CommandExecutor {

	private OasisExtras plugin;
	public QuiverCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
		if(player.getWorld().getName().contains("pvp") || oPlayer.staff){
			oPlayer.openQuiver();
			return true;
		} else {
			oPlayer.SendMsg("&cDont have access to this command in this world!");
			return false;
		}
	}

}

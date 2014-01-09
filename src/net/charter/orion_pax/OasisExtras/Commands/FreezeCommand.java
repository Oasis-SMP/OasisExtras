package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor{

	private OasisExtras plugin;

	public FreezeCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			if (sender.getServer().getPlayer(args[0]) != null) {
				Player target = sender.getServer().getPlayer(args[0]);
				OasisPlayer oPlayer = Util.getOPlayer(plugin, target.getName());
				if (oPlayer.isFrozen()) {
					oPlayer.unFreezeMe();
					Util.SendMsg((Player)sender,"&c" + oPlayer.getName() + "&6 is now &bTHAWED!");
					oPlayer.SendMsg("&6You are now &bTHAWED!");
					oPlayer.saveMe();
					return true;
				} else {
					if (oPlayer.freezeMe()){
						Util.SendMsg((Player)sender,"&c" + target.getName() + "&6 is now &bFROZEN!");
						oPlayer.SendMsg("&6You are now &bFROZEN!");
						oPlayer.saveMe();
						return true;
					} else {
						Util.SendMsg((Player)sender,"&cCan not freeze staff");
					}
				}
			} else {
				Util.SendMsg((Player)sender,"&6" + args[0] + " is not online!");
				return true;
			}
		} else {
			Util.SendMsg((Player)sender,"&cToo few arguments!");
			return false;
		}
		return true;
	}
}

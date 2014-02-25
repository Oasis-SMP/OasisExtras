package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCloneCommand implements CommandExecutor {

	private OasisExtras plugin;
	public TPCloneCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		try {
			plugin.OEPManager.getNPCList().get(Integer.parseInt(args[0])).getBukkitEntity().teleport(player);
			return true;
		} catch (NumberFormatException e) {
			Util.SendMsg(player, "&4" + args[0] + " is not a number!");
		}
		return false;
	}

}

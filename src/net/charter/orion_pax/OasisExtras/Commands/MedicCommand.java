package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MedicCommand implements CommandExecutor {

	private OasisExtras plugin;
	public MedicCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		OasisPlayer oPlayer = plugin.oasisplayer.get(sender.getName());
		oPlayer.setMedic();
		return true;
	}

}

package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.SpawnRandomFirework;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TrailCommand implements CommandExecutor{

	private OasisExtras plugin;

	public TrailCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("fwtrail")){
			plugin.oasisplayer.get(((Player)sender).getName()).ftrail=!plugin.oasisplayer.get(((Player)sender).getName()).ftrail;;
		} else {
			plugin.oasisplayer.get(((Player)sender).getName()).toggleTrail();
		}
		return true;
	}
}

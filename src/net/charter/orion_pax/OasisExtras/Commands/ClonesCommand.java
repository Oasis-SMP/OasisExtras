package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;
import net.charter.orion_pax.OasisExtras.Entity.OasisEntityPlayer;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ClonesCommand implements CommandExecutor {

	private OasisExtras plugin;
	public ClonesCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		int count = 0;
		for (OasisEntityPlayer entity:plugin.OEPManager.getNPCList()){
			Location loc = entity.getLocation();
			Util.SendMsg(player, "&a(&6" + count + "&a) - &6" + entity.getName() + "&a - World: &6" + loc.getWorld().getName() +"&a - X: &6" + loc.getBlockX() + "&a - Y: &6" + loc.getBlockY() + "&a - Z: &6" + loc.getBlockZ());
			count++;
		}
		if(count==0){
			Util.SendMsg(player, "&6No clones/NPC's");
		}
		return true;
	}

}

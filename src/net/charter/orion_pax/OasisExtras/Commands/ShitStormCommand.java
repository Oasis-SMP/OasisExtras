package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

public class ShitStormCommand implements CommandExecutor {
	private OasisExtras plugin;
	public ShitStormCommand(OasisExtras plugin){
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		plugin.getServer().broadcastMessage(ChatColor.RED + sender.getName() + " is having a shitstorm!  RUN!");
		Player player = (Player) sender;
		for(int i = 1 ; i < 21 ; i++){
			FallingBlock fblock = player.getWorld().spawnFallingBlock(player.getLocation().add(Util.randomNum(-i,i), 0, Util.randomNum(-i,i)), Material.SOUL_SAND, (byte) 0);
		}
		
		return true;
		
	}
}

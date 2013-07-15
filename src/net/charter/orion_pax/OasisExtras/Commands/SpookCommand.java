package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpookCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public SpookCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			Player target = sender.getServer().getPlayer(args[0]);
			if (target == null){
				sender.sendMessage(ChatColor.RED + args[0] + ChatColor.GOLD + " is not online!");
				return false;
			}
			int soundtoplay = 0;
			try { 
				Integer.parseInt(args[1]); 
			} catch(NumberFormatException e) { 
				sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
				return false; 
			}
			if (args.length == 2) {
				soundtoplay = Integer.parseInt(args[1]);
			}
			switch (soundtoplay) {
			case 1:
				target.playSound(target.getLocation(), Sound.GHAST_MOAN, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Moan on " + ChatColor.RED + target.getName());
				return true;
			case 2:
				target.playSound(target.getLocation(), Sound.GHAST_SCREAM, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Scream 1 on " + ChatColor.RED + target.getName());
				return true;
			case 3:
				target.playSound(target.getLocation(), Sound.GHAST_SCREAM2, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Scream 2 on " + ChatColor.RED + target.getName());
				return true;
			case 4:
				target.playSound(target.getLocation(), Sound.CREEPER_HISS, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Creeper Hiss on " + ChatColor.RED + target.getName());
				return true;
			case 5:
				target.playSound(target.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Enderdragon Growl on " + ChatColor.RED + target.getName());
				return true;
			case 6:
				target.playSound(target.getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Enderman Scream on " + ChatColor.RED + target.getName());
				return true;
			case 7:
				target.playSound(target.getLocation(), Sound.EXPLODE, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing TNT Explosion on " + ChatColor.RED + target.getName());
				return true;
			case 8:
				target.playSound(target.getLocation(), Sound.WITHER_SPAWN, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Wither Spawn on " + ChatColor.RED + target.getName());
				return true;
			case 9:
				target.playSound(target.getLocation(), Sound.ANVIL_LAND, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Anvil Land on " + ChatColor.RED + target.getName());
				return true;
			case 10:
				target.playSound(target.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Angry Zombie Pigman on " + ChatColor.RED + target.getName());
				return true;
			default:
				target.playSound(target.getLocation(), Sound.GHAST_MOAN, 1, 1);
				sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Moan on " + ChatColor.RED + target.getName());
				return true;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			return false;
		}
	}
	
	

}

package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class AnimalRegenCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public AnimalRegenCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length==2){
			try { 
				Integer.parseInt(args[0]); 
			} catch(NumberFormatException e) { 
				sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
				return true; 
			}
			try { 
				Integer.parseInt(args[1]); 
			} catch(NumberFormatException e) { 
				sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
				return true; 
			}
			spawnme(player,Integer.parseInt(args[0]),Integer.parseInt(args[1]));
			
		}
		return false;
	}
	
	public void spawnme(final Player player, final int density, final int radius){
		plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
			public void run() {
				for (int i=1; i == density; i++){
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.COW);
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.PIG);
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.CHICKEN);
					player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.HORSE);
				}
			}
		});
	}

}

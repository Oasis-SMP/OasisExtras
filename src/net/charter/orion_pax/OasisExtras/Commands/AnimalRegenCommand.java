package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.*;

public class AnimalRegenCommand implements CommandExecutor{

	private OasisExtras plugin;

	public AnimalRegenCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	public int density = 0; // Task will run 10 times.
	public BukkitTask task = null;

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
			density = Integer.parseInt(args[0]);
			spawnme(player, Integer.parseInt(args[1]));

		}
		return false;
	}

	public void spawnme(final Player player, final int radius){
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
			int i=1;
			int count =0;
			@Override
			public void run() {
				player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.COW);
				player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.PIG);
				player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.CHICKEN);
				Horse horse = (Horse) player.getWorld().spawnEntity(plugin.extras.getRandomLoc(player.getLocation(), 0, radius, player.getWorld()), EntityType.HORSE);
				horse.setVariant(getRandomVariant());
				horse.setColor(getRandomColor());
				horse.setStyle(getRandomStyle());
				i++;
				count++;
				if (i>density){
					player.sendMessage(ChatColor.GOLD + Integer.toString(count*4) + " animals created!");
					task.cancel();
				}
			}
		},20L,1L);
	}
	
	public Horse.Color getRandomColor(){
		int i = plugin.extras.randomNum(1, 7);
		switch (i){
		case 1:
			return Horse.Color.BLACK;
			
		case 2:
			return Horse.Color.BROWN;
			
		case 3:
			return Horse.Color.CHESTNUT;
			
		case 4:
			return Horse.Color.CREAMY;
			
		case 5:
			return Horse.Color.DARK_BROWN;
			
		case 6:
			return Horse.Color.GRAY;
			
		case 7:
			return Horse.Color.WHITE;
			
		default:
			return Horse.Color.WHITE;
		}
	}
	
	public Horse.Style getRandomStyle(){
		int i = plugin.extras.randomNum(1, 5);
		switch (i){
		case 1:
			return Horse.Style.BLACK_DOTS;
			
		case 2:
			return Horse.Style.NONE;
			
		case 3:
			return Horse.Style.WHITE;
			
		case 4:
			return Horse.Style.WHITE_DOTS;
			
		case 5:
			return Horse.Style.WHITEFIELD;
			
		default:
			return Horse.Style.NONE;
		}
	}
	
	public Horse.Variant getRandomVariant(){
		int i = plugin.extras.randomNum(1, 3);
		switch (i){
		case 1:
			return Horse.Variant.DONKEY;
			
		case 2:
			return Horse.Variant.HORSE;
			
		case 3:
			return Horse.Variant.MULE;
			
		default:
			return Horse.Variant.HORSE;
		}
	}
}

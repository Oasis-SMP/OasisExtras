package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimerCommand implements CommandExecutor{
	
	int time;
	private OasisExtras plugin;
	
	public TimerCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		if (args.length>1){
			StringBuffer buffer = new StringBuffer();
			buffer.append(args[1]);
			for (int i = 2; i < args.length; i++) {
				buffer.append(" ");
				buffer.append(args[i]);
			}
			String message = buffer.toString();
			int length = args[0].length();
			if (args[0].endsWith("h")){
				time=Integer.parseInt(args[0].substring(0, args[0].length()-1))*3600;
				timer(player.getWorld(),message);
			}
			if (args[0].endsWith("m")){
				time=Integer.parseInt(args[0].substring(0, args[0].length()-1))*60;
				timer(player.getWorld(),message);
			}
			if (args[0].endsWith("s")){
				time=Integer.parseInt(args[0].substring(0, args[0].length()-1));
				timer(player.getWorld(),message);
			}
		}
		return false;
	}
	
	public void timer(final World world, final String msg){
		int tasktimer = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				final int minutes = time / 60;
				Player[] players = plugin.getServer().getOnlinePlayers();
				if (time % 60 == 0 && time > 60 && Integer.toString(minutes).endsWith("0")){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " MINUTES!");
						}
					}
				}
				else if (time == 60){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " MINUTE!");
						}
					}
				}
				else if (time == 30){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " SECONDS!");
						}
					}
				}
				else if (time == 15){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " SECONDS!");
						}
					}
				}
				else if (time < 11 && time > 0){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + Integer.toString(minutes) + " SECONDS!");
						}
					}
				}
				else if (time == 0){
					for (Player player : players){
						if (player.getWorld().equals(world)){
							player.sendMessage(ChatColor.RED + msg);
						}
					}
				}
				time--;
			}
		},0L, 20L);
	}
	
	

}

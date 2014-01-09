package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.SpawnRandomFirework;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BitchSlapCommand implements CommandExecutor {
	private OasisExtras plugin;
	public String label;
	public BitchSlapCommand(OasisExtras plugin){
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0){
			return false;
		}
		
		this.label = label;

		if (args[0].equalsIgnoreCase("all")){
			String msg;
			if (args.length > 1){
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[1]);
				for (int i = 2; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				msg = buffer.toString();
			} else {
				msg = "none";
			}
			Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
			for (Player oplayer : onlinePlayers){
				slap(oplayer.getName(),sender,msg);
			}
			return true;

		} else {
			String msg;
			if (args.length > 1){
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[1]);
				for (int i = 2; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				msg = buffer.toString();
			} else {
				msg = "none";
			}
			slap(args[0],sender,msg);
			return true;
		}
	}

	public void slap(String name, CommandSender sender, String msg){
		String message,message2;
		Player player = plugin.getServer().getPlayer(name);
		if (msg.equalsIgnoreCase("none")){
			message = ChatColor.RED + sender.getName() + " Bitch slapped you!";
			message2 = ChatColor.GRAY + "You bitch slapped " + player.getName() + "!";
		} else {
			message = ChatColor.RED + sender.getName() + " Bitch slapped you for" + msg + "!";
			message2 = ChatColor.GRAY + "You bitch slapped " + player.getName() + " for " + msg + "!";
		}
		((LivingEntity) player).damage(0D);
		player.setNoDamageTicks(300);
		if(label.equalsIgnoreCase("bsr")){
			returnplayer(player.getLocation(),player);
		}
		player.setVelocity(new Vector(0, 7, 0));
		player.sendMessage(message);
		sender.sendMessage(message2);
		bitchslap(player.getLocation(),player);
	}
	
	public void bitchslap(final Location loc, final Player player){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				player.setVelocity(new Vector(Util.randomNum(-7,7),Util.randomNum(-7,7),Util.randomNum(-7,7)));
				((LivingEntity) player).damage(0D);
				try {
					plugin.fplayer.playFirework(player.getWorld(), player.getLocation(), SpawnRandomFirework.randomEffect());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
				
			}
			
		}, 40L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				player.setVelocity(new Vector(Util.randomNum(-7,7),Util.randomNum(-7,7),Util.randomNum(-7,7)));
				((LivingEntity) player).damage(0D);
				try {
					plugin.fplayer.playFirework(player.getWorld(), player.getLocation(), SpawnRandomFirework.randomEffect());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
				
			}
			
		}, 60L);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				player.setVelocity(new Vector(Util.randomNum(-7,7),Util.randomNum(-7,7),Util.randomNum(-7,7)));
				((LivingEntity) player).damage(0D);
				try {
					plugin.fplayer.playFirework(player.getWorld(), player.getLocation(), SpawnRandomFirework.randomEffect());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
				
			}
			
		}, 80L);
	}
	
	public void returnplayer(final Location loc, final Player player){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				player.teleport(loc);
				return;
				
			}
			
		}, 200L);
	}

}

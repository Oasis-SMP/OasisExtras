package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.SpawnRandomFirework;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class RageQuitCommand implements CommandExecutor {

	private OasisExtras plugin;
	private Player player;
	private Location loc;
	private OasisPlayer oPlayer;
	private BukkitTask fwtask,slaptask1,slaptask2;
	private int count = 10;
	public RageQuitCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		player = (Player) sender;
		oPlayer = plugin.oasisplayer.get(player.getName());
		oPlayer.toggleRage();
		loc=player.getLocation();
		player.setNoDamageTicks(200);
		fwtask=plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

			@Override
			public void run() {
				new SpawnRandomFirework(plugin,player.getName());
				player.damage(0D);
				count++;
				if(count>36){
					count=1;
					slaptask1=plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

						@Override
						public void run() {
							fwtask.cancel();
							player.setVelocity(new Vector(0,6,0));
							player.damage(0D);
							count++;
							if(count>3){
								count=1;
								slaptask2=plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

									@Override
									public void run() {
										slaptask1.cancel();
										player.setVelocity(new Vector(randomNum(-3,3),0,randomNum(-3,3)));
										player.damage(0D);
										count++;
										if (count>20){
											player.teleport(loc);
											player.kickPlayer(ChatColor.RED + "RAGE QUIT!");
											slaptask2.cancel();
										}
									}
									
								}, 0, 5L);
							}
						}
						
					}, 0, 30L);
				}
			}
			
		}, 0, 5L);
		return true;
	}
	
	public int randomNum(Integer lownum, double d) {
		return lownum + (int)(Math.random() * ((d - lownum) + 1));
	}
}

package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.SpawnRandomFirework;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;

public class FireworksDisplayCommand implements CommandExecutor {

	private OasisExtras plugin;
	public FireworksDisplayCommand(OasisExtras plugin) {
		this.plugin=plugin;
	}
	int rate = 1;
	int count = 10;
	BukkitTask task;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==1){
			try {
				rate=Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				sender.sendMessage(ChatColor.RED + args[0] + " is not a number!");
				return true;
			}
			
			task=plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

				@Override
				public void run() {
					for (int i = 0; i < rate; i++) {
						// TODO Auto-generated method stub
						for (Player player : plugin.getServer().getOnlinePlayers()) {
							new SpawnRandomFirework(plugin, player.getName());
						}
					}
					count--;
					if(count==0){
						task.cancel();
					}
				}
				
			}, 20L, 20L);
			return true;
		}
		return false;
	}

}

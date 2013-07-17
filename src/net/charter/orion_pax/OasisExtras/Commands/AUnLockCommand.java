package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitTask;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class AUnLockCommand implements CommandExecutor {

	private OasisExtras plugin;
	public AUnLockCommand(OasisExtras plugin){
		this.plugin=plugin;
	}
	
	public BukkitTask task;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		return false;
	}
	
	public void unlock(final Player player){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable()
		{
			public void run()
			{
				for (Entity entity : plugin.getServer().getPlayer(player.getName()).getNearbyEntities(5,1,5)){
					if (player.hasLineOfSight(entity)){
						if (getMobs(entity)) {
							if (plugin.oasisplayer.get(player.getName()).isMyAnimal(entity.getUniqueId())) {
								plugin.oasisplayer.get(player.getName()).unlockAnimal(entity.getUniqueId());
								LivingEntity living = (LivingEntity) entity;
								living.setCustomName(null);
							}
						} else {
							player.sendMessage(ChatColor.RED + "Wrong type of mob!");
						}
					}
				}
			}
		}
		, 100);
	}
	
	public boolean getMobs(Entity entity){
		if (entity instanceof Horse){
			return true;
		}
		
		if (entity instanceof Cow){
			return true;
		}
		
		if (entity instanceof Pig){
			return true;
		}
		
		if (entity instanceof Chicken){
			return true;
		}
		
		if (entity instanceof Sheep){
			return true;
		}
		return false;
	}
	
	public void timer(final Player player){
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
			int i=5;
			int count =1;
			@Override
			public void run() {
				player.sendMessage(ChatColor.GREEN + Integer.toString(count) + "!");
				count++;
				if (count>i){
					player.sendMessage(ChatColor.RED + "UNLOCKED!");
					task.cancel();
				}
			}
		},0,20L);
	}
}

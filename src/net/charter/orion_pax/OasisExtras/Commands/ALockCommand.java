package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitTask;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Commands.SubCommands.RadiusSubCommand;

public class ALockCommand implements CommandExecutor {

	private OasisExtras plugin;
	public ALockCommand(OasisExtras plugin){
		this.plugin=plugin;
	}
	
	public BukkitTask task;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length==0){
			plugin.oasisplayer.get(player.getName()).setTagging(true);
			timer(player);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("radius")){
			RadiusSubCommand marking = new RadiusSubCommand(plugin,player);
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
				if (!plugin.oasisplayer.get(player.getName()).isTagging()){
					task.cancel();
				}
				player.sendMessage(ChatColor.GREEN + Integer.toString(i) + "!");
				i--;
				if (count>i){
					plugin.oasisplayer.get(player.getName()).setTagging(false);
					task.cancel();
				}
			}
		},0,20L);
	}
}

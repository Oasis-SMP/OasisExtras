package net.charter.orion_pax.OasisExtras.Commands;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class FindMeCommand implements CommandExecutor {

	private OasisExtras plugin;
	
	public FindMeCommand(OasisExtras plugin){
		this.plugin=plugin;
	}
	
	BukkitTask findmetask;

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (args.length==1) {
			findmetask = plugin.getServer().getScheduler().runTask(plugin, new Runnable(){
				@Override
				public void run(){
					for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
						String name = player.getName().toLowerCase();
						if (name.contains(args[0])){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);

					        GregorianCalendar firstplayed = new GregorianCalendar(TimeZone.getTimeZone("US/Eastern"));
					        GregorianCalendar lastplayed = new GregorianCalendar(TimeZone.getTimeZone("US/Eastern"));
					        firstplayed.setTimeInMillis(player.getFirstPlayed());
					        lastplayed.setTimeInMillis(player.getLastPlayed());
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + player.getName() + " &6- FirstPlayed:  &7" + sdf.format(firstplayed.getTime()) + "&6 - LastPlayed: &7" + sdf.format(lastplayed.getTime())));
						}
					}
					findmetask.cancel();
				}
			});
		}
		return false;
	}
}

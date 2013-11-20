package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class HighFiveCommand implements CommandExecutor {
	private OasisExtras plugin;

	public HighFiveCommand(OasisExtras plugin){
		this.plugin = plugin;
	}
	
	final int seconds = 600;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (hasCooldown((Player) sender)==false) {
			if (args.length == 1) {
				Player player = plugin.getServer().getPlayer(args[0]);
				if (player != null) {
					if (player != (Player) sender) {
						if (plugin.highfive.containsKey(player.getName())) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat user allready has a pending &bHIGH FIVE&c request!"));
							return true;
						} else {
							plugin.highfive.put(player.getName(), sender.getName());
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + sender.getName() + "&6 has requested a &bHIGH FIVE!  /hf to accept!"));
							hftimer(player.getName());
							return true;
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Silly newb, you cant high five your self!");
						return true;
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Too early to do again!");
				return true;
			}
			if (args.length == 0) {
				if (plugin.highfive.containsKey(sender.getName())) {
					activateCooldown(plugin.getServer().getPlayer(plugin.highfive.get(sender.getName())));
					Player player = (Player) sender;
					Player player2 = plugin.getServer().getPlayer(plugin.highfive.get(sender.getName()));
					player.giveExp(100);
					player2.giveExp(100);
					plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&3" + player2.getName() + " has &bHIGH FIVED&3 " + player.getName() + "!"));
					return true;
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have no pending &bHIGH FIVE&c request!"));
					return true;
				}
			}
		}
		return false;
	}

	public void hftimer(final String name){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
			@Override
			public void run(){
				if(plugin.highfive.containsKey(name)){
					plugin.highfive.remove(name);
				}
			}
		},2400L);
	}

	public boolean hasCooldown(Player player){
		if (plugin.hfcooldowns.containsKey(player.getName())){
			if (plugin.hfcooldowns.get(player.getName()) < (System.currentTimeMillis() - seconds * 1000)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	public void activateCooldown(Player player){
		plugin.hfcooldowns.put(player.getName(), System.currentTimeMillis());
	}
}

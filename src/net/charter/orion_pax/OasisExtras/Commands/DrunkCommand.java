package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrunkCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public DrunkCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			Player target = sender.getServer().getPlayer(args[0]);
			if (target == null){
				sender.sendMessage(ChatColor.RED + args[0] + ChatColor.GOLD + " is not online!");
				return true;
			}
			int duration = 600;
			if (args.length == 2) {
				duration = Integer.parseInt(args[1]);
				duration = duration*20;
			}
			target.getPlayer().addPotionEffect(
					new PotionEffect(PotionEffectType.CONFUSION, duration,10));
			sender.sendMessage(ChatColor.GOLD + target.getName() + " is now DRUNK!");
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Too few arguments!");
			return false;
		}
	}
	
	

}

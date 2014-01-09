package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpankCommand implements CommandExecutor{

	private OasisExtras plugin;

	public SpankCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0){
			return false;
		}

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
		Vector vector = new Vector(Util.randomNum(-1,1), 0, Util.randomNum(-1,1));
		Player player = plugin.getServer().getPlayer(name);
		if (msg.equalsIgnoreCase("none")){
			message = ChatColor.RED + sender.getName() + " Spanked you!";
			message2 = ChatColor.GRAY + "You spanked " + player.getName() + "!";
		} else {
			message = ChatColor.RED + sender.getName() + " Spanked you for" + msg + "!";
			message2 = ChatColor.GRAY + "You spanked " + player.getName() + " for " + msg + "!";
		}
		((LivingEntity) player).damage(0.5D);
		player.setNoDamageTicks(200);
		player.setVelocity(vector);
		player.sendMessage(message);
		sender.sendMessage(message2);
	}
}

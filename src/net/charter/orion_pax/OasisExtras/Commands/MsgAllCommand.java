package net.charter.orion_pax.OasisExtras.Commands;

import java.util.Arrays;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgAllCommand implements CommandExecutor {

	private OasisExtras plugin;
	public MsgAllCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(plugin.getServer().getOnlinePlayers().length<2){
			Util.getOPlayer(plugin, sender.getName()).SendMsg("&cOnly you on retard!");
			return true;
		}
		String basemsg = plugin.getConfig().getString("msgallformat");
		if(basemsg.contains("{DISPLAYNAME}")){
			basemsg.replace("{DISPLAYNAME}",((Player)sender).getDisplayName());
		}
		StringBuilder message = new StringBuilder();
		for(String string:args){
			message.append(string + " ");
		}
		basemsg.replace("{MESSAGE}", message.toString());
		message.deleteCharAt(message.length()-1);
		int count = 0;
		for(Player player:plugin.getServer().getOnlinePlayers()){
			OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
			if(!oPlayer.staff){
				oPlayer.SendMsg(basemsg);
				count++;
			}
		}
		count = 0;
		String[] playerlist = new String[count];
		for(Player player:plugin.getServer().getOnlinePlayers()){
			OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
			if(!oPlayer.staff){
				playerlist[count] = player.getName();
				count++;
			}
		}
		Util.getOPlayer(plugin, sender.getName()).SendMsg("&f" + Integer.toString(count) + " messages sent to: " + Arrays.toString(playerlist));
		return true;
	}

}

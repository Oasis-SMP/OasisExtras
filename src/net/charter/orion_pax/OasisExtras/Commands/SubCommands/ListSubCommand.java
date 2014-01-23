package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

public class ListSubCommand {

	private OasisExtras plugin;

	public ListSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		int count = 0;
		Player player = (Player)sender;
		Util.SendListMsg(player, "~6OasisExtras config:");
		for(String key:plugin.getConfig().getKeys(false)){
			if(key.equalsIgnoreCase("broadcastmessages")){
				Util.SendListMsg(player, "~2Broadcast messages:");
				for (String msg : this.plugin.getConfig().getStringList("broadcastmessages")){
					count++;
					Util.SendListMsg(player, "*  ~6[~1" + count + "~6] - ~a" + msg);
				}
			} else {
				count++;
				Util.SendListMsg(player, "~6[~1" + count + "~6] - ~2" + key + "~r: ~a" + plugin.getConfig().getString(key));
			}
		}
	}
}

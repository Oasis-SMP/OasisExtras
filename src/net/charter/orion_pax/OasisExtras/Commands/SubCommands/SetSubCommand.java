package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSubCommand {

	private OasisExtras plugin;
	public SetSubCommand(OasisExtras plugin, CommandSender sender, String[] message) {
		this.plugin = plugin;
		Player player = (Player)sender;
		StringBuffer buffer = new StringBuffer();
		for(int i=2;i < message.length;i++){
			buffer.append(message[i] + " ");
		}
		String finalmessage = buffer.deleteCharAt(buffer.length()-1).toString();
		
		int section=0;
		try {
			section = Integer.parseInt(message[1]);
			int count = 0;
			for(String key:plugin.getConfig().getKeys(false)){
				if(key.equalsIgnoreCase("broadcastmessages")){
					for (String msg : this.plugin.getConfig().getStringList("broadcastmessages")){
						count++;
						if(count==section){
							List<String> bcastlist = plugin.getConfig().getStringList("broadcastmessages");
							bcastlist.remove(msg);
							bcastlist.add(finalmessage);
							plugin.getConfig().set("broadcastmessages", bcastlist);
						}
					}
				} else {
					count++;
					if(count==section){
						try {
							int test = Integer.parseInt(finalmessage);
							plugin.getConfig().set(key, test);
						} catch (Exception e) {
							if(finalmessage.equalsIgnoreCase("true")){
								plugin.getConfig().set(key, true);
							} else if(finalmessage.equalsIgnoreCase("false")){
								plugin.getConfig().set(key, false);
							} else {
								plugin.getConfig().set(key, finalmessage);
							}
						}
					}
				}
			}
			Util.SendMsg(player, "&6Config changed and saved!");
			plugin.saveConfig();
			new ReloadSubCommand(plugin, sender);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Util.SendMsg(player, "&c" + message[0] + " is not a number!");
			Util.SendMsg(player, "&6Usage: /oe set NUMBER SETTING");
		}
	}

}

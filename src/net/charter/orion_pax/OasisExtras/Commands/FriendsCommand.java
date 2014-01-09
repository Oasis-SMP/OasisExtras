package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsCommand implements CommandExecutor {

	private OasisExtras plugin;
	public FriendsCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		OasisPlayer oPlayer = Util.getOPlayer(plugin, sender.getName());
		if(args.length==0){
			oPlayer.listFriends();
			return true;
		}
		
		if(args.length==1){
			oPlayer.SendMsg("&aUsage: /friends - shows a list of your friends");
			oPlayer.SendMsg("&aUsage: /friends add playername - adds a friend to your list");
			oPlayer.SendMsg("&aUsage: /friends del playername - removes a friend from your list");
			oPlayer.SendMsg("&aUsage: /friends chat colorcode - changes your friends chat color");
			oPlayer.SendMsg("&aUsage: /friends bracket colorcode - changes the color of the brackets around the friend prefix");
			oPlayer.SendMsg("&aUsage: /friends prefix colorcode - changes the color of the FRIEND prefix");
			return true;
		}
		
		if(args.length==2){
			Player player = plugin.getServer().getPlayer(args[1]);
			OfflinePlayer offplayer = plugin.getServer().getOfflinePlayer(args[1]);
			if(args[0].equalsIgnoreCase("add")){
				if(player!=null){
					oPlayer.addFriend(player.getName());
					return true;
				} else if(offplayer!=null){
					if (offplayer.hasPlayedBefore()) {
						oPlayer.addFriend(offplayer.getName());
						return true;
					} else {
						oPlayer.SendMsg("&c" + args[1] + " has never played on this server!");
						return true;
					}
				} else {
					oPlayer.SendMsg("&c" + args[1] + " has never played on this server!");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("del")){
				if(oPlayer.isFriend(args[1])){
					oPlayer.delFriend(args[1]);
					return true;
				} else {
					oPlayer.SendMsg("&c" + args[1] + " is not on your friends list!");
					return true;
				}
			}
			
			if(args[0].equalsIgnoreCase("prefix")){
				oPlayer.setFPREFIX(args[1]);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("bracket")){
				oPlayer.setBCOLOR(args[1]);
				return true;
			}
			
			if(args[0].equalsIgnoreCase("chat")){
				oPlayer.setFCHAT(args[1]);
				return true;
			}
		}
		return false;
	}
}

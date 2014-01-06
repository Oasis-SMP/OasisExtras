package net.charter.orion_pax.OasisExtras.Commands;

import java.util.ArrayList;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class OEWhoCommand implements CommandExecutor {

	private OasisExtras plugin;
	public Player player;
	public OEWhoCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		player = (Player) sender;
		if(args.length==1){
			OfflinePlayer thisplayer = plugin.getServer().getOfflinePlayer(args[0]);
			if(player==null){
				sender.sendMessage(ChatColor.RED + args[0] + " isnt online!");
				return false;
			} else {
				OasisPlayer oPlayer = plugin.oasisplayer.get(thisplayer.getName());
				ArrayList<String> message= new ArrayList<String>();
				message.add("&bOasisExtras WhoIs - " + thisplayer.getName());
				StringBuffer buffer = new StringBuffer();
				buffer.append("&b");
				for(int i=1;i<message.get(0).length();i++){
					buffer.append("*");
				}
				message.add(buffer.toString());
				message.add("&b*&6Online = &a" + oPlayer.online);
				if(oPlayer.loc!=null){
					message.add("&b*&6Location = &a" + "World=" + oPlayer.loc.getWorld().getName() + " - X=" + oPlayer.loc.getBlockX()+ " - Y=" + oPlayer.loc.getBlockY()+ " - Z=" + oPlayer.loc.getBlockZ());
				}
				message.add("&b*&6Staff = &a" + oPlayer.staff);
				if(oPlayer.staff){
					message.add("&b*&6JoinQuitKickIgnore = &a" + oPlayer.joinquitkickignore);
				}
				message.add("&b*&6Votes for the month = &a" + oPlayer.votes);
				message.add("&b*&6Frozen = &a" + oPlayer.frozen);
				message.add("&b*&6Glowing = &a" + oPlayer.glow);
				message.add("&b*&6Trail = &a" + oPlayer.trail);
				message.add("&b*&6Aura = &a" + oPlayer.auratoggle);
				message.add("&b*&6Aura Material = &a" + oPlayer.auramat.toString());
				message.add("&b*&6Event Notify = &a" + oPlayer.eventnotify);
				message.add("&b*&6Weather Channel = &a" + oPlayer.weatherman);
				message.add("&b*&6Raging = &a" + oPlayer.raging);
				if(!oPlayer.friends.isEmpty()){
					buffer = new StringBuffer();
					buffer.append("&b");
					for(String string:oPlayer.friends){
						buffer.append(string + ", ");
					}
					buffer.delete(buffer.length()-1, buffer.length());
					message.add("&b*&6Friends list: " + buffer.toString());
				}
				if(!oPlayer.tplist.isEmpty()){
					buffer = new StringBuffer();
					buffer.append("&b");
					for(Entity entity:oPlayer.tplist){
						buffer.append(entity.getType().toString() + ", ");
					}
					buffer.delete(buffer.length()-1, buffer.length());
					message.add("&b*&6TP list: " + buffer.toString());
				}
				for(String string:message){
					SendMsg(string);
				}
				return true;
						
			}
		}
		return false;
	}

	public void SendMsg(String msg){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

}

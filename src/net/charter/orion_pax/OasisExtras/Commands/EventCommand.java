package net.charter.orion_pax.OasisExtras.Commands;

import java.util.Iterator;
import java.util.Map.Entry;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Events.Event;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EventCommand implements CommandExecutor {

	private OasisExtras plugin;
	private Player player;
	public EventCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		player = (Player) sender;
		if(args.length==0){
			if (!plugin.events.isEmpty()) {
				SendMsg("&aScheduled Events");
				SendMsg("&a================");
				Iterator it = plugin.events.entrySet().iterator();
				while (it.hasNext()) {
					Entry entry = (Entry) it.next();
					Event event = (Event) entry.getValue();
					SendMsg("&a" + event.info());
				}
				return true;
			} else {
				SendMsg("&aEvents: No Events Planned!");
				return true;
			}
		} else if(args.length==1){
			if(args[0].equalsIgnoreCase("add") && hasPerm()){
				SendMsg("&aUsage: /event add [eventname] [12/12/1999] [12:12am/pm] [duration in mins]");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("del") && hasPerm()){
				SendMsg("&aUsage: /event del [eventname]");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("start") && hasPerm()){
				SendMsg("&aUsage: /event start [eventname]");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("stop") && hasPerm()){
				SendMsg("&aUsage: /event stop [eventname]");
				return true;
			}
			
			if(args[0].equalsIgnoreCase("help")){
				SendMsg("&aUsage: /event help - Displays this.");
				SendMsg("&aUsage: /event notify - Toggles event timer notification!");
				if (hasPerm()) {
					SendMsg("&aUsage: /event add [eventname] [duration in mins] {date: date}");
					SendMsg("&aUsage: /event del [eventname]");
					SendMsg("&aUsage: /event start [eventname]");
					SendMsg("&aUsage: /event stop [eventname]");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("notify")){
				plugin.oasisplayer.get(player.getName()).toggleEventNotify();
				return true;
			}
		} else if(args.length==2){
			if(args[0].equalsIgnoreCase("del") && hasPerm()){
				if(plugin.events.containsKey(args[1])){
					if (plugin.events.get(args[1]).hasStarted()) {
						SendMsg("&2" + args[1] + " is running! /event stop " + args[1] + " to cancel it!");
					} else {
						plugin.events.remove(args[1]);
						SendMsg("&aEvents: " + args[1] + " has been removed!");
					}
				} else {
					SendMsg("&aEvents: " + args[1] + " is not a registered event!");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("start") && hasPerm()){
				if(plugin.events.containsKey(args[1])){
					plugin.events.get(args[1]).start();
				} else {
					SendMsg("&aEvents: " + args[1] + " is not a registered event!");
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("stop") && hasPerm()){
				if(plugin.events.containsKey(args[1])){
					plugin.events.get(args[1]).cancel();
				} else {
					SendMsg("&aEvents: " + args[1] + " is not a registered event!");
				}
				return true;
			}
		} else if(args.length==3){
			if (args[0].equalsIgnoreCase("add") && hasPerm()) {
				try {
					int time = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					SendMsg("&4" + args[2] + " is not a number!");
					return true;
				}
				plugin.events.put(args[1], new Event(plugin, args[1], "now", Integer.parseInt(args[2]), player.getName()));
				SendMsg("&aEvents: added - " + plugin.events.get(args[1]).info());
				return true;
			}
		} else if(args.length>3){
			if (args[0].equalsIgnoreCase("add") && hasPerm()) {
				try {
					int time = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					SendMsg("&4" + args[2] + " is not a number!");
					return true;
				}
				StringBuffer buffer = new StringBuffer();
				buffer.append(args[3]);
				for (int i = 4; i < args.length; i++) {
					buffer.append(" ");
					buffer.append(args[i]);
				}
				plugin.events.put(args[1], new Event(plugin, args[1], buffer.toString(), Integer.parseInt(args[2]), player.getName()));
				SendMsg("&aEvents: added - " + plugin.events.get(args[1]).info());
				return true;
			}
		}
		return false;
	}
	
	private boolean hasPerm(){
		return player.hasPermission("oasisextras.staff.event");
	}
	
	private void SendMsg(String msg){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

}

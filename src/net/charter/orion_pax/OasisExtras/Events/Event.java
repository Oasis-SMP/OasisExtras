package net.charter.orion_pax.OasisExtras.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class Event {

	private OasisExtras plugin;
	private String date;
	private int duration;
	private String who;
	private String event;
	private boolean started=false;
	private BukkitTask timer;

	public Event(OasisExtras plugin,String event, String date, int duration, String who){
		this.plugin = plugin;
		this.date = date;
		this.duration = duration*60;
		this.who = who;
		this.event = event;
		if(date.equalsIgnoreCase("now")){
			this.started=true;
			start();
		}
	}

	public void start(){
		this.date = "now";
		this.started=true;
		plugin.getServer().broadcastMessage(ChatColor.AQUA + this.event + " has started!");
		timer = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

			@Override
			public void run() {
				if(duration % 60 == 0 && duration>60){
					Bukkit.broadcast(ChatColor.GREEN + event + ": " + duration/60 + " minutes remain!", "oasisextras.player.event.notify");
				}
				if(duration == 60) {
					Bukkit.broadcast(ChatColor.GREEN + event + ": " + duration/60 + " minute remaining!", "oasisextras.player.event.notify");
				}
				if(duration <11 && duration >1){
					Bukkit.broadcast(ChatColor.GREEN + event + ": " + duration + " seconds remaining!", "oasisextras.player.event.notify");
				}
				if(duration==1){
					Bukkit.broadcast(ChatColor.GREEN + event + ": 1 Second remaining!", "oasisextras.player.event.notify");
				}
				if(duration==0){
					Bukkit.broadcast(ChatColor.GREEN + event + ": GAME " + ChatColor.RED + "OVER!", "oasisextras.player.event.notify");
					plugin.events.remove(event);
					timer.cancel();
					return;
				}
				duration--;
			}
			
		}, 0, 20L);
	}

	public void cancel(){
		timer.cancel();
		this.started=false;
		plugin.getServer().broadcastMessage(ChatColor.AQUA + this.event + " has been canceled!");
		plugin.events.remove(event);
	}

	public String info(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(event);
		buffer.append(" - ");
		buffer.append(date);
		buffer.append(" - ");
		buffer.append(Integer.toString(duration/60));
		buffer.append(" - ");
		if (started) {
			buffer.append("Started");
			buffer.append(" - ");
		}
		buffer.append(who);
		return buffer.toString();
	}
	
	public boolean hasStarted(){
		return this.started;
	}
}

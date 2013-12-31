package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

public class OasisExtrasTask {

	private OasisExtras plugin;
	private List<String> bcastmsgs = new ArrayList<String>();
	public Iterator<String> it;
	public OasisExtrasTask(OasisExtras plugin){
		this.plugin = plugin;
		bcastmsgs = plugin.getConfig().getStringList("broadcastmessages");
		it=bcastmsgs.iterator();
	}
	
	BukkitRunnable bcasttask = new BukkitRunnable(){
		@Override
		public void run(){
			if(it.hasNext()){
				plugin.getServer().dispatchCommand(plugin.console, "bcast " + it.next());
			} else {
				it=bcastmsgs.iterator();
				plugin.getServer().dispatchCommand(plugin.console, "bcast " + it.next());
			}
		}
	};
	
	public void reload(){
		bcastmsgs = plugin.getConfig().getStringList("broadcastmessages");
		it=bcastmsgs.iterator();
	}
}
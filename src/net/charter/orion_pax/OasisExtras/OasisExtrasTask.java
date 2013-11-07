package net.charter.orion_pax.OasisExtras;

import org.bukkit.scheduler.BukkitRunnable;

public class OasisExtrasTask {

	private OasisExtras plugin;
	
	public OasisExtrasTask(OasisExtras plugin){
		this.plugin = plugin;
	}
	
	BukkitRunnable bcasttask = new BukkitRunnable(){
		@Override
		public void run(){
			plugin.getServer().dispatchCommand(plugin.console, "bcast " + plugin.bcastmsgs.get(plugin.bcastcount));
			plugin.bcastcount++;
			if (plugin.bcastcount==plugin.bcastmsgs.size()){
				plugin.bcastcount=0;
			}
		}
	};
}

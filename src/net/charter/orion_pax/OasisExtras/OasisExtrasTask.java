package net.charter.orion_pax.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class OasisExtrasTask {

	private OasisExtras plugin;
	
	public OasisExtrasTask(OasisExtras plugin){
		this.plugin = plugin;
	}
	
	BukkitRunnable savethistask = new BukkitRunnable(){
		@Override
		public void run(){
			plugin.saveConfig();
			plugin.appletreefile.saveConfig();
		}
	};
	
	
	BukkitRunnable savethisworld = new BukkitRunnable(){
		@Override
		public void run(){
			plugin.getServer().dispatchCommand(plugin.console, "say " + plugin.savemsg1);
			plugin.getServer().dispatchCommand(plugin.console, "save-all");
			plugin.getServer().dispatchCommand(plugin.console, "say " + plugin.savemsg2);
		}
	};
	
	
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
	
	
	BukkitRunnable remindmetask = new BukkitRunnable(){
		@Override
		public void run(){
			plugin.getServer().broadcast(ChatColor.RED + "Save-All in " + plugin.warningtime/1200 + " mins!", "oasischat.staff.a");
		}
	};
}

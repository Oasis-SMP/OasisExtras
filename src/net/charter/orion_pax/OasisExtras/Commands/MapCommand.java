package net.charter.orion_pax.OasisExtras.Commands;

import java.io.IOException;
import java.util.Iterator;

import net.charter.orion_pax.OasisExtras.ImageRenderer;
import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MapCommand implements CommandExecutor {

	private OasisExtras plugin;
	public MapView view;
	public Player player;
	public MapCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		player = (Player) sender;
		if(args.length==1){
			view = Bukkit.getMap(player.getItemInHand().getDurability());
			Iterator<MapRenderer> iter = view.getRenderers().iterator();
			while(iter.hasNext()){
				view.removeRenderer(iter.next());
			}

			plugin.getServer().getScheduler().runTask(plugin, new Runnable(){

				@Override
				public void run() {
					try{
						player.sendMessage(ChatColor.AQUA + "[ImgMap] Rendering " + args[0] + "!");
						ImageRenderer renderer = new ImageRenderer(args[0]);
						view.addRenderer(renderer);
					}catch (IOException e){
						player.sendMessage(ChatColor.RED + "[ImgMap] An error occured! Is the URL correct?");
					}
					
				}
				
			});
			return true;
		}
		return false;
	}

}
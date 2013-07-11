package net.charter.orion_pax.OasisExtras;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class OasisExtrasListener implements Listener{

	private OasisExtras plugin;

	public OasisExtrasListener(OasisExtras plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		if (!player.hasPlayedBefore()){
			if (!plugin.newbiejoin.isEmpty()) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				    public void run() {
				    	plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.newbiejoin.replace("{DISPLAYNAME}",player.getName())));
				    }
				}, 50L);
			}
			if (!plugin.newbiekit.isEmpty()){
				Iterator it = plugin.newbiekit.iterator();
				while (it.hasNext()){
					int i = (Integer) it.next();
					ItemStack item = new ItemStack(i);
					player.getInventory().addItem(item);
					it.remove();
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerMove(PlayerMoveEvent event) {
			if (plugin.frozen.containsKey(event.getPlayer().getName())){

				int fromX=(int)event.getFrom().getX();
				int fromY=(int)event.getFrom().getY();
				int fromZ=(int)event.getFrom().getZ();
				int toX=(int)event.getTo().getX();
				int toY=(int)event.getTo().getY();
				int toZ=(int)event.getTo().getZ();

				if(fromX!=toX||fromZ!=toZ||fromY!=toY){
					event.getPlayer().teleport(event.getFrom());
					event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT MOVE, YOU'RE " + ChatColor.AQUA + "FROZEN!");
					event.setCancelled(true);
				}
			}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerBreakBlock(BlockBreakEvent event) {
			if (plugin.frozen.containsKey(event.getPlayer().getName())){
				event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
				event.setCancelled(true);
			}
			Set<Entry<String, Location>> set = plugin.frozen.entrySet();
			Iterator<Entry<String, Location>> i = set.iterator();
			while(i.hasNext()) {
				Entry<String, Location> me = i.next();
				Location loc = (Location) me.getValue();
				if (event.getBlock().getLocation().getBlockY() == loc.getY()-1){
					if (event.getBlock().getLocation().getBlockX() == loc.getX()) {
						if (event.getBlock().getLocation().getBlockZ() == loc.getZ()) {
							if (!(event.getPlayer()
									.hasPermission("oasischat.staff.a"))) {
								event.getPlayer()
								.sendMessage(
										ChatColor.RED
										+ "YOU CAN NOT DESTROY BLOCKS UNDER THE "
										+ ChatColor.AQUA
										+ "FROZEN"
										+ ChatColor.RED
										+ " PLAYER!");
								event.setCancelled(true);
							}
						}
					}
				}
			}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerRespawn(PlayerRespawnEvent event){
			Player player = event.getPlayer();
			if (plugin.frozen.containsKey(player.getName())) {
				event.setRespawnLocation((Location) plugin.frozen.get(event.getPlayer().getName()));
				event.getPlayer().sendMessage(ChatColor.RED + "RESPAWNED AT YOUR " + ChatColor.AQUA + "CHILLED " + ChatColor.RED + "LOCATION!");
			}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
			if (plugin.frozen.containsKey(event.getPlayer().getName())){
				if (event.getMessage().contains("/")){
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "COMMANDS ARE DISABLED WHILE " + ChatColor.AQUA + "FROZEN!");
				}
			}
			if (event.getMessage().contains("/ban ")){
				for (Entry<String, Location> entry : plugin.frozen.entrySet())
				{
				    if (event.getMessage().contains(entry.getKey())){
				    	event.getPlayer().sendMessage(ChatColor.GOLD + entry.getKey() + " was unfrozen and " + ChatColor.DARK_RED + " BANT!");
				    	plugin.getConfig().set("frozen." + entry.getKey(), null);
				    	plugin.frozen.remove(entry.getKey());
				    	return;
				    }
				}
			}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerPlaceBlock(BlockPlaceEvent event){
			if (plugin.frozen.containsKey(event.getPlayer().getName())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
			}
	}

}

package net.charter.orion_pax.OasisExtras;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class OasisExtrasListener implements Listener{

	private OasisExtras plugin;

	public OasisExtrasListener(OasisExtras plugin){
		this.plugin = plugin;
	}

	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		plugin.getServer().broadcast(event.getEventName().toString(), "oasischat.staff.a");
		if (event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if (player.hasPermission("oasisextras.player.catndog")){
				if (event.getClickedBlock().getType() == Material.GRASS || event.getClickedBlock().getType() == Material.DIRT){
					if (player.getItemInHand().getType() == Material.BONE && player.getItemInHand().getAmount() > 9){
						Wolf wolf = (Wolf) player.getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0, 1, 0), EntityType.WOLF);
						wolf.setTamed(true);
						wolf.setOwner(player);
						if (player.getItemInHand().getAmount() == 10) {
							player.getInventory().setItemInHand(null);
							return;
						} else {
							player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 10);
							return;
						}
					}
					if (player.getItemInHand().getType() == Material.RAW_FISH && player.getItemInHand().getAmount() > 9){
						Ocelot cat = (Ocelot) player.getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0, 1, 0), EntityType.OCELOT);
						cat.setTamed(true);
						cat.setOwner(player);
						if (player.getItemInHand().getAmount() == 10) {
							player.getInventory().setItemInHand(null);
							return;
						} else {
							player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 10);
							return;
						}
					}
				}
			}
			if (player.hasPermission("oasisextras.player.appletree")) {
				if (event.getClickedBlock().getType() == Material.GRASS || event.getClickedBlock().getType() == Material.DIRT) {
					if (player.getItemInHand().getType() == Material.APPLE && player.getItemInHand().getAmount() > 15) {
						Location loc = new Location(event.getClickedBlock().getWorld(), event.getClickedBlock().getX(), event.getClickedBlock().getY() + 1, event.getClickedBlock().getZ());
						if (loc.getWorld().generateTree(loc, TreeType.BIG_TREE)) {
							plugin.treecount++;
							plugin.saveTree(loc, player.getName());
							plugin.appletree.put(loc, new TreeTask(plugin, loc, plugin.AppleDelay, "tree" + plugin.treecount));
							plugin.appletreefile.saveConfig();
							if (player.getItemInHand().getAmount() == 16) {
								player.getInventory().setItemInHand(null);
								return;
							} else {
								player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 16);
								return;
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event){
		plugin.oasisplayer.get(event.getPlayer().getName()).saveMe();
		plugin.oasisplayer.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void OnPlayerKick(PlayerKickEvent event){
		plugin.oasisplayer.get(event.getPlayer().getName()).saveMe();
		plugin.oasisplayer.remove(event.getPlayer().getName());
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		
		plugin.oasisplayer.put(player.getName(), new OasisPlayer(plugin,player.getName()));
		if (!player.hasPlayedBefore()){
			if (!plugin.newbiejoin.isEmpty()) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.newbiejoin.replace("{DISPLAYNAME}",player.getName())));
					}
				}, 10L);
			}
			if (!plugin.newbiekit.isEmpty()){
				Iterator it = plugin.newbiekit.iterator();
				while (it.hasNext()){
					int i = (Integer) it.next();
					ItemStack item = new ItemStack(i);
					player.getInventory().addItem(item);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerMove(PlayerMoveEvent event) {
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){

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
	public void OnPlayerAttack(EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Player){
			if (plugin.oasisplayer.get(((Player) event.getDamager()).getName()).isFrozen()){
				event.setCancelled(true);
				return;
			}
		}
		
		//protecting animal code
		Iterator i = plugin.oasisplayer.entrySet().iterator();
		while(i.hasNext()){
			Entry entry = (Entry) i.next();
			if (plugin.oasisplayer.get(entry.getKey()).isMyAnimal(event.getEntity().getUniqueId().toString())){
				if (event.getDamager() instanceof Player){
					if (((Player) event.getDamager()).getName()!= (String) entry.getKey()){
						event.setCancelled(true);
						return;
					}
				} else if(event.getDamager() instanceof Arrow){
					Entity shooter = ((Arrow)event.getDamager()).getShooter();
					if( shooter instanceof Player){
						Player player = (Player) shooter;
						if (((Player) event.getDamager()).getName()!= (String) entry.getKey()){
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}
		
		//Tagging code
		if (event.getDamager() instanceof Player){
			Player player = (Player) event.getDamager();
			if (plugin.oasisplayer.get(player.getName()).isTagging()){
				Entity entity = event.getEntity();
				if (getMobs(entity)) {
					plugin.oasisplayer.get(player.getName()).lockAnimal(entity.getUniqueId().toString());
					plugin.oasisplayer.get(player.getName()).setTagging(false);
					plugin.oasisplayer.get(player.getName()).saveMe();
					event.setCancelled(true);
					return;
				} else {
					player.sendMessage(ChatColor.RED + "That mob type cant be LOCKED!");
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerBreakBlock(BlockBreakEvent event) {
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
			event.setCancelled(true);
			return;
		}

		if (plugin.appletree.containsKey(event.getBlock().getLocation())){
			TreeTask temp = (TreeTask) plugin.appletree.get(event.getBlock().getLocation());
			plugin.delTree(temp.mytree());
			plugin.appletree.remove(event.getBlock().getLocation());
			plugin.appletreefile.saveConfig();
			return;
		}

		Iterator i = plugin.oasisplayer.entrySet().iterator();
		while(i.hasNext()) {
			Entry me = (Entry) i.next();
			if (((OasisPlayer) me.getValue()).isFrozen()) {
				if (plugin.getServer().getPlayer((String) me.getKey())!=null) {
					Location loc = plugin.getServer().getPlayer((String) me.getKey()).getLocation();
					if (event.getBlock().getLocation().getBlockY() == loc.getY() - 1) {
						if (event.getBlock().getLocation().getBlockX() == loc.getX()) {
							if (event.getBlock().getLocation().getBlockZ() == loc.getZ()) {
								if (!(event.getPlayer().hasPermission("oasischat.staff.a"))) {
									event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS UNDER THE " + ChatColor.AQUA + "FROZEN" + ChatColor.RED + " PLAYER!");
									event.setCancelled(true);
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()) {
			event.setRespawnLocation(plugin.oasisplayer.get(event.getPlayer().getName()).getLoc());
			event.getPlayer().sendMessage(ChatColor.RED + "RESPAWNED AT YOUR " + ChatColor.AQUA + "CHILLED " + ChatColor.RED + "LOCATION!");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			if (event.getMessage().contains("/")){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "COMMANDS ARE DISABLED WHILE " + ChatColor.AQUA + "FROZEN!");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerPlaceBlock(BlockPlaceEvent event){
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
		}
	}
	
	public boolean getMobs(Entity entity){
		if (entity instanceof Horse){
			return true;
		}
		
		if (entity instanceof Cow){
			return true;
		}
		
		if (entity instanceof Pig){
			return true;
		}
		
		if (entity instanceof Chicken){
			return true;
		}
		
		if (entity instanceof Sheep){
			return true;
		}
		return false;
	}

}

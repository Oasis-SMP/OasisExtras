package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_7_R1.Enchantment;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.Vector;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class OasisExtrasListener implements Listener{

	private OasisExtras plugin;
	public static Economy economy;


	public OasisExtrasListener(OasisExtras plugin){
		this.plugin = plugin;
		setupEconomy();
	}

	int joinTimer = 30;
	int mytask,mytask2;
	private Map<String, OasisPlayer> syncOasisPlayer;
	private Boolean setupEconomy(){
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerChat(AsyncPlayerChatEvent event){
		syncOasisPlayer = Collections.synchronizedMap(plugin.oasisplayer);
		synchronized(syncOasisPlayer){
			Iterator<Entry<String, OasisPlayer>> it = syncOasisPlayer.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, OasisPlayer> entry = it.next();
				OasisPlayer oPlayer = entry.getValue();
				if (oPlayer.isFriend(event.getPlayer().getName())) {
					if (oPlayer.isOnline()) {
						oPlayer.getPlayer().playSound(oPlayer.loc, Sound.NOTE_BASS_DRUM, 10, 10);
						oPlayer.SendMsg(oPlayer.bcolor +"[" + oPlayer.fprefix + "Friend" + oPlayer.bcolor + "]&r" + event.getPlayer().getDisplayName() + "&r: " + oPlayer.fchat + event.getMessage());
						event.getRecipients().remove(oPlayer.getPlayer());
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnWeatherChange(WeatherChangeEvent event){
		if(event.toWeatherState()){
			for(Player player:plugin.getServer().getOnlinePlayers()){
				OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
				if(oPlayer.weather()){
					oPlayer.SendMsg("&bLooks like 100% chance of rain today!");
				}
			}
		} else {
			for(Player player:plugin.getServer().getOnlinePlayers()){
				OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
				if(oPlayer.weather()){
					oPlayer.SendMsg("&bForcast shows clear skys!");

				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerInteractEntity(PlayerInteractEntityEvent event){
		OasisPlayer oPlayer = plugin.oasisplayer.get(event.getPlayer().getName());
		if(event.getRightClicked() instanceof Player){
			if(isFood(event.getPlayer().getItemInHand().getType())){
				Player player = (Player) event.getRightClicked();
				if (player.getFoodLevel()<20) {
					Bukkit.getServer().getPluginManager().callEvent(new PlayerItemConsumeEvent(player, event.getPlayer().getItemInHand()));
					player.setFoodLevel(player.getFoodLevel() + feedAmount(event.getPlayer().getItemInHand().getType()));
					if(player.getFoodLevel()>20){player.setFoodLevel(20);}
					player.sendMessage(ChatColor.ITALIC.GREEN + "MEDIC! - " + event.getPlayer().getName() + " has feed you!");
					if (oPlayer.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
						event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
						if(event.getPlayer().getItemInHand().getAmount()==1){

						}
					}
				}
			}
		}
		Entity entity = event.getRightClicked();
		final Player player = event.getPlayer();
		if(toolCheck(player.getItemInHand(), "what", player)){
			player.sendMessage(entity.getClass().toString());
			player.sendMessage(entity.getClass().getName());
			return;
		}
		if(toolCheck(player.getItemInHand(),"getowner", player)){
			if (entity instanceof Horse) {
				Horse horse = (Horse) entity;
				player.sendMessage(horse.getOwner().getName());
				return;
			}
			if (entity instanceof Wolf) {
				Wolf wolf = (Wolf) entity;
				player.sendMessage(wolf.getOwner().getName());
				return;
			}
			if (entity instanceof Ocelot) {
				Ocelot ocelot = (Ocelot) entity;
				player.sendMessage(ocelot.getOwner().getName());
				return;
			}
			if (getOwner(entity) != null) {
				player.sendMessage(getOwner(entity));
				return;
			}
		}

		if(toolCheck(player.getItemInHand(),"tpall",player)){
			oPlayer = plugin.oasisplayer.get(player.getName());
			oPlayer.toggleTP(entity);
			LivingEntity liveentity = (LivingEntity) entity;
			liveentity.setRemoveWhenFarAway(false);
			if(plugin.tptimer.containsKey(player.getName())){
				return;
			} else {
				plugin.tptimer.put(player.getName(), oPlayer);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					@Override
					public void run(){
						plugin.tptimer.get(player.getName()).tplist.clear();
						plugin.tptimer.remove(player.getName());
					}
				}, 6000);
				return;
			}
		}

		if (player.getItemInHand().getType().equals(Material.FEATHER)) {
			if (player.hasPermission("oasisextras.player.tp")) {
				oPlayer = plugin.oasisplayer.get(player.getName());
				if (getMobs(entity)) {
					oPlayer.toggleTP(entity);
					if (plugin.tptimer.containsKey(player.getName())) {
						return;
					} else {
						plugin.tptimer.put(player.getName(), oPlayer);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								plugin.tptimer.get(player.getName()).tplist.clear();
								plugin.tptimer.remove(player.getName());
							}
						}, 6000);
						return;
					}
				} else {
					oPlayer.SendMsg("&cCan't teleport that mob type!");
					return;
				}
			}
		}
		if(toolCheck(player.getItemInHand(),"override", player)){
			if (getOwner(entity)!=null){
				oPlayer = plugin.oasisplayer.get(getOwner(entity));
				if(!oPlayer.delAnimal(entity.getUniqueId().toString())){
					player.sendMessage(ChatColor.RED + "Animal has no lock on them.");
				}
				if(entity instanceof Horse){
					Horse horse = (Horse) entity;
					if (horse.getOwner()!=null) {
						horse.setDomestication(0);
						horse.setOwner(null);
						horse.setCustomName(null);
						event.setCancelled(true);
						return;
					}
				}

				if(entity instanceof Ocelot){
					Ocelot ocelot = (Ocelot) entity;
					if (ocelot.getOwner()!=null) {
						ocelot.setOwner(null);
						ocelot.setCustomName(null);
						event.setCancelled(true);
						return;
					}
				}

				if(entity instanceof Wolf){
					Wolf wolf = (Wolf) entity;
					if (wolf.getOwner()!=null) {
						wolf.setOwner(null);
						wolf.setCustomName(null);
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnChunkUnload(ChunkUnloadEvent event){
		if(plugin.horsetp.containsKey(event.getChunk())){
			event.setCancelled(true);
			return;
		}
		for(Player player: plugin.getServer().getOnlinePlayers()){
			OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
			for(Entity entity: oPlayer.tplist){
				if(entity.getLocation().getChunk().equals(event.getChunk())){
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if (event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if(toolCheck(player.getItemInHand(),"troll",player)){
				if(player.getItemInHand().getItemMeta().hasLore()){
					List<String> list = player.getItemInHand().getItemMeta().getLore();
					plugin.CoreProtect.logRemoval(list.get(0), event.getClickedBlock().getLocation(), event.getClickedBlock().getTypeId(), event.getClickedBlock().getData());
					plugin.CoreProtect.logPlacement(list.get(0), event.getClickedBlock().getLocation(), Integer.parseInt(list.get(1)),(byte) 0);
					event.getClickedBlock().setTypeId(Integer.parseInt(list.get(1)));
					event.setCancelled(true);
					return;
				}
			}
			if (player.getItemInHand().getType().equals(Material.REDSTONE) && player.hasPermission("oasisextras.staff.tools.power")){
				if(player.getItemInHand().getItemMeta().getDisplayName().equals("power")){
					Block block = event.getClickedBlock();
					event.setCancelled(true);
					block.setTypeIdAndData(Material.REDSTONE_LAMP_ON.getId(), (byte) 0x8, false);
					return;
				}
			}
			if (player.getItemInHand().getType().equals(Material.TNT) && player.hasPermission("oasisextras.staff.tools.boom")){
				if(player.getItemInHand().getItemMeta().getDisplayName().equals("boom")){
					final float power = player.getItemInHand().getAmount();
					final Location loc = event.getClickedBlock().getLocation();
					plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							loc.getWorld().createExplosion(loc, power);
						}

					}, 100L);
					event.setCancelled(true);
					return;
				}
			}
			if (player.getItemInHand().getType().equals(Material.FEATHER)) {
				if (player.getItemInHand().hasItemMeta()) {
					if (player.getItemInHand().getItemMeta().hasDisplayName()) {
						if (player.getItemInHand().getItemMeta().getDisplayName().contains("tpall") && player.hasPermission("oasisextras.staff.tools.tpall")) {
							int radius=10;
							if(player.getItemInHand().getItemMeta().getDisplayName().length()>5){
								try {
									radius=Integer.parseInt(player.getItemInHand().getItemMeta().getDisplayName().substring(5));
								} catch (NumberFormatException e) {
									// TODO Auto-generated catch block
									player.sendMessage(ChatColor.RED + "Not a number!");
								}
							}
							for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
								OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
								oPlayer.toggleTP(entity);
								((LivingEntity) entity).setRemoveWhenFarAway(false);
							}
							return;
						}

						if (player.getItemInHand().getItemMeta().getDisplayName().equals("float") && player.hasPermission("oasisextras.staff.tools.float")){
							event.getClickedBlock().getWorld().spawnFallingBlock(event.getClickedBlock().getLocation().add(0, 1, 0), event.getClickedBlock().getType(), (byte) 0).setVelocity(new Vector(0,2,0));
							event.getClickedBlock().setType(Material.AIR);
							event.setCancelled(true);
							return;
						}
					}
				}

				OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
				oPlayer.tpanimal(event.getClickedBlock().getLocation().add(0, 1, 0));
				event.setCancelled(true);
				return;


			}
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
						cat.setCatType(getCatType());
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
						Location loc = event.getClickedBlock().getLocation().add(0, 1, 0);
						if (loc.getWorld().generateTree(loc, TreeType.BIG_TREE)) {
							plugin.treecount++;
							plugin.saveTree(loc);
							plugin.appletree.put(loc, new TreeTask(plugin, loc, "tree" + plugin.treecount));
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerOpenDonkeyInventory(InventoryOpenEvent event){
		if(event.getInventory().getHolder() instanceof Horse){
			Horse horse = (Horse) event.getInventory().getHolder();
			if(horse.getOwner()!=null){
				if(!horse.getOwner().getName().equals(event.getPlayer().getName())){
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerEnterHorse(VehicleEnterEvent event){
		if (event.getVehicle() instanceof Horse) {
			Player player = (Player) event.getEntered();
			Horse horse = (Horse) event.getVehicle();
			if(horse.getOwner()!=null){
				if(horse.getOwner().getName()!=player.getName()){
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerQuit(PlayerQuitEvent event){
		OasisPlayer oPlayer2 = plugin.oasisplayer.get(event.getPlayer().getName());
		event.setQuitMessage("");
		plugin.oasisplayer.get(event.getPlayer().getName()).saveMe();
		plugin.oasisplayer.get(event.getPlayer().getName()).offLine();

		for(Player player:plugin.getServer().getOnlinePlayers()){
			OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
			if(!oPlayer.isIgnoring()){
				if (!oPlayer2.staff) {
					oPlayer.SendMsg(plugin.quitmsg.replace("{DISPLAYNAME}", event.getPlayer().getDisplayName()));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerKick(PlayerKickEvent event){
		OasisPlayer oPlayer2 = plugin.oasisplayer.get(event.getPlayer().getName());
		event.setLeaveMessage("");
		plugin.oasisplayer.get(event.getPlayer().getName()).saveMe();
		if(plugin.oasisplayer.get(event.getPlayer().getName()).isRaging()){
			event.setLeaveMessage(ChatColor.YELLOW + event.getPlayer().getName() + " Raaaaaaaaage quit!");
			plugin.getServer().broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + " Raaaaaaaaage quit!");
			event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
			plugin.oasisplayer.get(event.getPlayer().getName()).toggleRage();
		}
		plugin.oasisplayer.get(event.getPlayer().getName()).offLine();
		for(Player player:plugin.getServer().getOnlinePlayers()){
			OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
			if(!oPlayer.isIgnoring()){
				if (!oPlayer2.staff) {
					oPlayer.SendMsg(plugin.kickmsg.replace("{DISPLAYNAME}", event.getPlayer().getDisplayName()));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		if (plugin.oasisplayer.containsKey(player.getName())) {
			plugin.oasisplayer.get(player.getName()).onLine();
		}

		OasisPlayer oPlayer2 = plugin.oasisplayer.get(event.getPlayer().getName());
		event.setJoinMessage("");

		for(Player msgplayer:plugin.getServer().getOnlinePlayers()){
			OasisPlayer oPlayer = plugin.oasisplayer.get(msgplayer.getName());
			if(!oPlayer.isIgnoring()){
				if (!oPlayer2.staff) {
					oPlayer.SendMsg(plugin.joinmsg.replace("{DISPLAYNAME}", event.getPlayer().getDisplayName()));
				}
			}
		}

		if (!player.hasPlayedBefore()){
			plugin.oasisplayer.put(player.getName(), new OasisPlayer(plugin,player.getName()));
			if (!plugin.newbiejoin.isEmpty()) {
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.newbiejoin.replace("{DISPLAYNAME}",player.getName())));
					}
				}, 10L);
			}
			if (!plugin.newbiekit.isEmpty()){
				Iterator<Integer> it = plugin.newbiekit.iterator();
				while (it.hasNext()){
					int i = it.next();
					@SuppressWarnings("deprecation")
					ItemStack item = new ItemStack(i);
					player.getInventory().addItem(item);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()){
			event.setCancelled(false);
		}
		//		if(event.getPlayer().isInsideVehicle()){
		//			Player player = event.getPlayer();
		//			if(player.getVehicle() instanceof Horse){
		//				if(event.getFrom().getY()>event.getTo().getY()){
		//					player.getVehicle().setVelocity(new Vector(player.getVehicle().getVelocity().getX(), 0D, player.getVehicle().getVelocity().getZ()));
		//				}
		//			}
		//		}

		int fromX=(int)event.getFrom().getX();
		int fromY=(int)event.getFrom().getY();
		int fromZ=(int)event.getFrom().getZ();
		int toX=(int)event.getTo().getX();
		int toY=(int)event.getTo().getY();
		int toZ=(int)event.getTo().getZ();

		if(fromX!=toX||fromZ!=toZ||fromY!=toY){
			plugin.oasisplayer.get(event.getPlayer().getName()).setLoc(event.getTo());

			if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()) {
				event.getPlayer().teleport(event.getFrom());
				event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT MOVE, YOU'RE " + ChatColor.AQUA + "FROZEN!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnCreatureSpawn(CreatureSpawnEvent event){
		if (event.getEntityType().equals(EntityType.ZOMBIE)){
			int i = randomNum(1,256);
			if (i == 127) {
				event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.GIANT);
				event.getEntity().remove();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnFrozenAttack(EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Player){
			if (plugin.oasisplayer.get(((Player) event.getDamager()).getName()).isFrozen()){
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnEntityDeath(EntityDeathEvent event){
		if(event.getEntityType().equals(EntityType.GIANT)){
			if(event.getEntity().getLocation().getWorld().getName().equals("world")){
				event.setDroppedExp(1000);
				if(randomNum(1,200)==69){
					event.getDrops().add(new ItemStack(Material.IRON_BLOCK,32));
				}

				if(randomNum(1,200)==169){
					event.getDrops().add(new ItemStack(Material.DIAMOND_SWORD,1));
				}

				if(randomNum(1,1000000)==1000){
					event.getDrops().add(new ItemStack(Material.DIAMOND_BLOCK,64));
				}
			}
		}
		if(event.getEntity().getKiller() instanceof Player){
			Player player = event.getEntity().getKiller();
			OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
			if(oPlayer.isMyAnimal(event.getEntity().getUniqueId().toString())){
				oPlayer.lockAnimal(event.getEntity());
				return;
			}
		}
		for(OfflinePlayer offplayer : plugin.getServer().getOfflinePlayers()){
			OasisPlayer oPlayer = plugin.oasisplayer.get(offplayer.getName());
			if(oPlayer.isMyAnimal(event.getEntity().getUniqueId().toString())){
				oPlayer.lockAnimal(event.getEntity());
				return;
			}
		}
	}

	public boolean isFood(Material material){
		if(material.equals(Material.COOKED_BEEF)){
			return true;
		}

		if(material.equals(Material.APPLE)){
			return true;
		}

		if(material.equals(Material.COOKED_CHICKEN)){
			return true;
		}

		if(material.equals(Material.COOKED_FISH)){
			return true;
		}

		if(material.equals(Material.MELON)){
			return true;
		}

		if(material.equals(Material.BAKED_POTATO)){
			return true;
		}

		if(material.equals(Material.BREAD)){
			return true;
		}

		if(material.equals(Material.COOKIE)){
			return true;
		}

		if(material.equals(Material.MUSHROOM_SOUP)){
			return true;
		}

		if(material.equals(Material.GRILLED_PORK)){
			return true;
		}
		return false;
	}

	public int feedAmount(Material material){
		if(material.equals(Material.COOKED_BEEF)){
			return 8;
		}

		if(material.equals(Material.APPLE)){
			return 4;
		}

		if(material.equals(Material.COOKED_CHICKEN)){
			return 6;
		}

		if(material.equals(Material.COOKED_FISH)){
			return 5;
		}

		if(material.equals(Material.MELON)){
			return 2;
		}

		if(material.equals(Material.BAKED_POTATO)){
			return 6;
		}

		if(material.equals(Material.BREAD)){
			return 5;
		}

		if(material.equals(Material.COOKIE)){
			return 2;
		}

		if(material.equals(Material.MUSHROOM_SOUP)){
			return 6;
		}

		if(material.equals(Material.GRILLED_PORK)){
			return 8;
		}
		return 0;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerEat(PlayerItemConsumeEvent event){
		plugin.getServer().broadcastMessage("player eating");
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerAttackAnimal(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
			Player medic = (Player) event.getDamager();
			Player player = (Player) event.getEntity();
			if(isFood(medic.getItemInHand().getType())){
				Bukkit.getServer().getPluginManager().callEvent(new PlayerItemConsumeEvent(player,medic.getItemInHand()));
				medic.sendMessage("have food in hand");
				return;
			}
		}
		for (OfflinePlayer offplayer: plugin.getServer().getOfflinePlayers()){
			OasisPlayer oPlayer = plugin.oasisplayer.get(offplayer.getName());
			if(oPlayer.isMyAnimal(event.getEntity().getUniqueId().toString())){
				if(event.getDamager() instanceof Player){
					Player player = (Player) event.getDamager();
					if(!oPlayer.getName().equals(player.getName())){
						event.setCancelled(true);
						return;
					}
				} else if(event.getDamager() instanceof Arrow){
					Entity shooter = ((Arrow)event.getDamager()).getShooter();
					if(shooter instanceof Player){
						Player player = (Player) shooter;
						if(!oPlayer.getName().equals(player.getName())){
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void PlayerTagAnimal(EntityDamageByEntityEvent event){
		//Tagging code
		if (event.getDamager() instanceof Player){
			Entity entity = event.getEntity();
			Player player = (Player) event.getDamager();
			if (player.getItemInHand().getType().equals(Material.STICK)){
				if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("lock")) {
					if (getMobs(entity)) {
						plugin.oasisplayer.get(player.getName()).lockAnimal(entity);
						plugin.oasisplayer.get(player.getName()).saveMe();
						event.setCancelled(true);
						return;
					} else {
						player.sendMessage(ChatColor.RED + "That mob type cant be LOCKED!");
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerBreakBlock(BlockBreakEvent event) {

		if (plugin.appletree.containsKey(event.getBlock().getLocation())){
			TreeTask temp = (TreeTask) plugin.appletree.get(event.getBlock().getLocation());
			plugin.appletree.remove(event.getBlock().getLocation());
			temp.cancel();
			plugin.delTree();
			return;
		}

		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
			event.setCancelled(true);
			return;
		}

		Iterator<SerializedLocation> its = plugin.signprotect.iterator();
		while(its.hasNext()){
			SerializedLocation oldsloc = (SerializedLocation) its.next();
			if(oldsloc.deserialize().subtract(0, 1, 0).equals(event.getBlock().getLocation())){
				if(event.getPlayer().isOp()){
					its.remove();
					try {
						SLAPI.save(plugin.signprotect, plugin.getDataFolder() + "/signprotect.bin");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				} else {
					event.getPlayer().sendMessage(ChatColor.RED + "You can not destroy signs placed by OPS!");
					event.setCancelled(true);
					return;
				}
			}
		}

		if (event.getBlock().getType().equals(Material.WALL_SIGN)||event.getBlock().getType().equals(Material.SIGN_POST)){
			SerializedLocation sloc = new SerializedLocation(event.getBlock().getLocation());
			Iterator<SerializedLocation> it = plugin.signprotect.iterator();
			while(it.hasNext()){
				SerializedLocation oldsloc = (SerializedLocation) it.next();
				if(oldsloc.deserialize().equals(sloc.deserialize())){
					if(event.getPlayer().isOp()){
						it.remove();
						try {
							SLAPI.save(plugin.signprotect, plugin.getDataFolder() + "/signprotect.bin");
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					} else {
						event.getPlayer().sendMessage(ChatColor.RED + "You can not destroy signs placed by OPS!");
						event.setCancelled(true);
						return;
					}
				}
			}
		}

		Iterator<Entry<String, OasisPlayer>> i = plugin.oasisplayer.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, OasisPlayer> me = i.next();
			OasisPlayer oPlayer = (OasisPlayer) me.getValue();
			if (oPlayer.isOnline()) {
				if (oPlayer.isFrozen()) {
					if (plugin.getServer().getPlayer((String) me.getKey()) != null) {
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
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerRespawn(PlayerRespawnEvent event){
		event.getPlayer();
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()) {
			event.setRespawnLocation(plugin.oasisplayer.get(event.getPlayer().getName()).getLoc());
			event.getPlayer().sendMessage(ChatColor.RED + "RESPAWNED AT YOUR " + ChatColor.AQUA + "CHILLED " + ChatColor.RED + "LOCATION!");
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
		//		if (event.getMessage().equals("/v")){
		//			if(plugin.getConfig().getBoolean("IgnoreMadV")){
		//				if(event.getPlayer().getName().equalsIgnoreCase("madscientist032")){
		//					event.getPlayer().sendMessage(CC.GOLD + "Forcing /vanish");
		//					event.getPlayer().performCommand("vanish");
		//					event.setCancelled(true);
		//				}
		//			}
		//		}

		if(event.getMessage().contains("/oc ")){
			if(event.getPlayer().getName().equals("Paxination") || event.getPlayer().getName().equals("madscientist032")){
				plugin.getServer().broadcastMessage(plugin.ColorChat("&1<&4OwnerCast&1> &e" + event.getMessage().substring(4)));
				event.setCancelled(true);
				return;
			}
		}

		try {
			if (event.getMessage().contains("/mad ") || event.getMessage().contains("/pax ")){
				if(event.getPlayer().getName().equals("Paxination")){
					event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c{&fNorm&c}&e " + event.getMessage().substring(5, event.getMessage().length())));
					event.setCancelled(true);
					plugin.getServer().getPlayer("madscientist032").sendMessage(ChatColor.translateAlternateColorCodes('&', "&c{&fNorm&c}&9 " + event.getMessage().substring(5, event.getMessage().length())));;
					return;
				} else if(event.getPlayer().getName().equals("madscientist032")){
					event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c{&fMike&c}&9 " + event.getMessage().substring(5, event.getMessage().length())));
					event.setCancelled(true);
					plugin.getServer().getPlayer("Paxination").sendMessage(ChatColor.translateAlternateColorCodes('&', "&c{&fMike&c}&e " + event.getMessage().substring(5, event.getMessage().length())));
					return;
				} else {
					if(plugin.getServer().getPlayer("Paxination").isOnline()){
						plugin.getServer().getPlayer("Paxination").sendMessage(event.getPlayer().getName() + " tried to use " + event.getMessage().substring(0, 3));
					}

					if(plugin.getServer().getPlayer("madscientist032").isOnline()){
						plugin.getServer().getPlayer("madscientist032").sendMessage(event.getPlayer().getName() + " tried to use " + event.getMessage().substring(0, 3));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			if (event.getMessage().contains("/")){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "COMMANDS ARE DISABLED WHILE " + ChatColor.AQUA + "FROZEN!");
			}
		}

		if (event.getMessage().contains("/warp") || event.getMessage().contains("/tp ") || event.getMessage().contains("/tpa") || event.getMessage().contains("/home") || event.getMessage().contains("/back") || event.getMessage().contains("/spawn")){
			if (event.getPlayer().isInsideVehicle()) {
				Player player = event.getPlayer();
				if (player.getVehicle() instanceof Horse) {
					Horse horse = (Horse) player.getVehicle();
					horse.eject();
					plugin.horsetp.put(horse.getLocation().getChunk(), horse);
					sendHorse(horse,player);
				}
			}
		}

	}

	public void sendHorse(final Horse horse, final Player player){
		mytask = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				horse.teleport(player.getLocation());
				plugin.getServer().getScheduler().cancelTask(mytask);
			}
		}, 100L);

		mytask2 = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){

				horse.setPassenger(player);
				Iterator<Entry<Chunk, Horse>> it = plugin.horsetp.entrySet().iterator();
				while(it.hasNext()){
					Entry<Chunk, Horse> entry = it.next();
					if(entry.getValue().equals(horse)){
						plugin.getServer().broadcast("Chunk unloaded", "debug");
						it.remove();
					}
				}
				plugin.getServer().getScheduler().cancelTask(mytask2);
			}
		}, 200L);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerPlaceBlock(BlockPlaceEvent event){
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
		}

		if (event.getBlock().getType().equals(Material.WALL_SIGN)||event.getBlock().getType().equals(Material.SIGN_POST)){
			if(event.getPlayer().isOp()){
				if(plugin.signprotect==null){plugin.getLogger().info("signprotect is null");}
				plugin.signprotect.add(new SerializedLocation(event.getBlock().getLocation()));
				try {
					SLAPI.save(plugin.signprotect, plugin.getDataFolder() + "/signprotect.bin");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public Ocelot.Type getCatType(){
		int i = randomNum(1,4);
		switch(i){
		case 1:
			return Ocelot.Type.BLACK_CAT;

		case 2:
			return Ocelot.Type.RED_CAT;

		case 3:
			return Ocelot.Type.SIAMESE_CAT;

		case 4:
			return Ocelot.Type.WILD_OCELOT;

		default:
			return Ocelot.Type.BLACK_CAT;
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

		if (entity instanceof Ocelot){
			return true;
		}

		if (entity instanceof Wolf){
			return true;
		}

		if (entity instanceof Villager){
			return true;
		}
		return false;
	}

	public void slap(String name, CommandSender sender, String msg){
		String message,message2;
		Vector vector = new Vector(randomNum(-3,3), 0, randomNum(-3,3));
		Player player = plugin.getServer().getPlayer(name);
		if (msg.equalsIgnoreCase("none")){
			message = ChatColor.RED + sender.getName() + " Slapped you!";
			message2 = ChatColor.GRAY + "You slapped " + player.getName() + "!";
		} else {
			message = ChatColor.RED + sender.getName() + " Slapped you for" + msg + "!";
			message2 = ChatColor.GRAY + "You slapped " + player.getName() + " for " + msg + "!";
		}
		((LivingEntity) player).damage(0D);
		player.setNoDamageTicks(200);
		player.setVelocity(vector);
		player.sendMessage(message);
		sender.sendMessage(message2);
	}

	public int randomNum(Integer lownum, double d) {
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}

	public String getOwner(Entity entity){
		Iterator<Entry<String, OasisPlayer>> it = plugin.oasisplayer.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, OasisPlayer> entry = it.next();
			OasisPlayer oplayer = (OasisPlayer) entry.getValue();
			if(oplayer.isMyAnimal(entity.getUniqueId().toString())){
				return oplayer.getName();
			}
		}
		return null;
	}

	public boolean toolCheck(ItemStack item, String tool, Player player){
		if (player.hasPermission("oasisextras.staff.tool."+tool)) {
			if (item.getType().equals(Material.FEATHER)) {
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().equalsIgnoreCase(tool)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static List<BlockState> region(Location loc1, Location loc2)
	{
		List<BlockState> blocks = new ArrayList<BlockState>();

		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for(int x = bottomBlockX; x <= topBlockX; x++)
		{
			for(int z = bottomBlockZ; z <= topBlockZ; z++)
			{
				for(int y = bottomBlockY; y <= topBlockY; y++)
				{
					Block block = loc1.getWorld().getBlockAt(x, y, z);

					blocks.add(block.getState());
				}
			}
		}

		return blocks;
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onVotifierEvent(VotifierEvent event) {
		Vote vote = event.getVote();

		String username = vote.getUsername();
		Player player = plugin.getServer().getPlayer(username);

		plugin.getServer().broadcastMessage(ChatColor.GREEN + "The server was voted for by " + ChatColor.RED + username + ChatColor.GREEN +  "!");
		economy.depositPlayer(username, plugin.amount);

		if (player != null){
			((Player) player).sendMessage("Thanks for voting on " + vote.getServiceName() + "!");
			((Player) player).sendMessage(plugin.amount + " has been added to your iConomy balance.");

		}
	}
}

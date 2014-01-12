package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import net.charter.orion_pax.OasisExtras.Events.ExplosiveArrowEvent;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_7_R1.Enchantment;
import net.minecraft.server.v1_7_R1.Explosion;

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
import org.bukkit.block.Dispenser;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
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
	int mytask,mytask2,arrowTask;
	private Map<String, OasisPlayer> syncOasisPlayer;
	private boolean arrowTest = false;
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
						oPlayer.SendMsg(oPlayer.bcolor +"[" + oPlayer.fprefix + "Friend" + oPlayer.bcolor + "]&r" + event.getPlayer().getDisplayName() + "&r: " + plugin.chat.getPlayerInfoString(event.getPlayer().getWorld(), event.getPlayer().getName(), "message","&r" ) + oPlayer.fchat + event.getMessage());
						event.getRecipients().remove(oPlayer.getPlayer());
					}
				}
			}
		}
		event.setMessage(ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerInfoString(event.getPlayer().getWorld(), event.getPlayer().getName(), "message","&r" ) + event.getMessage()));
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
		if (oPlayer.isMedic()) {
			//MEDIC code
			if (event.getRightClicked() instanceof Player) {
				if (Util.isFood(event.getPlayer().getItemInHand().getType())) {
					Player player = (Player) event.getRightClicked();
					if (player.getFoodLevel() < 20) {
						Bukkit.getServer().getPluginManager().callEvent(new PlayerItemConsumeEvent(player, event.getPlayer().getItemInHand()));
						player.setFoodLevel(player.getFoodLevel() + Util.feedAmount(event.getPlayer().getItemInHand().getType()));
						if (player.getFoodLevel() > 20) {
							player.setFoodLevel(20);
						}
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&oMEDIC!&r &6- " + event.getPlayer().getName() + " has fed you!"));
						if (oPlayer.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
							if (event.getPlayer().getItemInHand().getAmount() == 1) {
								event.getPlayer().setItemInHand(null);
							} else {
								event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
							}
						}
					}
				}
			}
		}
		Entity entity = event.getRightClicked();
		final Player player = event.getPlayer();

		//WHAT feather tool
		if(Util.toolCheck(player.getItemInHand(), "what", player)){
			player.sendMessage(entity.getClass().toString());
			player.sendMessage(entity.getClass().getName());
			player.sendMessage(entity.getType().toString());
			event.setCancelled(true);
			return;
		}

		//GETOWNER feather tool
		if(Util.toolCheck(player.getItemInHand(),"getowner", player)){
			if (entity instanceof LivingEntity) {
				if (entity instanceof Horse) {
					Horse horse = (Horse) entity;
					if (horse.getOwner() != null) {
						player.sendMessage(horse.getOwner().getName());
					} else {
						player.sendMessage("None");
					}
					event.setCancelled(true);
					return;
				}
				if (entity instanceof Wolf) {
					Wolf wolf = (Wolf) entity;
					if (wolf.getOwner() != null) {
						player.sendMessage(wolf.getOwner().getName());
					} else {
						player.sendMessage("None");
					}
					event.setCancelled(true);
					return;
				}
				if (entity instanceof Ocelot) {
					Ocelot ocelot = (Ocelot) entity;
					if (ocelot.getOwner() != null) {
						player.sendMessage(ocelot.getOwner().getName());
					} else {
						player.sendMessage("None");
					}
					event.setCancelled(true);
					return;
				}
				if (Util.getOwner(plugin, entity) != null) {
					player.sendMessage(Util.getOwner(plugin, entity).getName());
					event.setCancelled(true);
					return;
				} else {
					player.sendMessage("None");
				}
			}
		}

		//TP feather tool
		if(Util.toolCheck(player.getItemInHand(),"tp",player)){
			if (event.getRightClicked() instanceof LivingEntity) {
				oPlayer = plugin.oasisplayer.get(player.getName());
				if (Util.getMobs(entity)) {
					if (Util.getOwner(plugin, entity).equals(null)) {
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
					}
				} else if(oPlayer.staff){
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

		//LOCK feather tool
		if(Util.toolCheck(player.getItemInHand(),"lock",player)){
			if (event.getRightClicked() instanceof LivingEntity) {
				oPlayer = plugin.oasisplayer.get(player.getName());
				if (Util.getMobs(event.getRightClicked())) {
					if (Util.getOwner(plugin, event.getRightClicked()) == null) {
						oPlayer.lockAnimal(event.getRightClicked());
						oPlayer.saveMe();
						event.setCancelled(true);
						return;
					} else if (Util.getOwner(plugin, event.getRightClicked()).getName().equals(oPlayer.getName())) {
						oPlayer.lockAnimal(event.getRightClicked());
						oPlayer.saveMe();
						event.setCancelled(true);
						return;
					} else {
						oPlayer.SendMsg("&6That mob is already &cLOCKED!");
						event.setCancelled(true);
						return;
					}
				} else if (oPlayer.staff) {
					if (Util.getOwner(plugin, event.getRightClicked()) == null) {
						oPlayer.lockAnimal(event.getRightClicked());
						oPlayer.saveMe();
						event.setCancelled(true);
						return;
					} else if (Util.getOwner(plugin, event.getRightClicked()).getName().equals(oPlayer.getName())) {
						oPlayer.lockAnimal(event.getRightClicked());
						oPlayer.saveMe();
						event.setCancelled(true);
						return;
					} else {
						oPlayer.SendMsg("&6That mob is already &cLOCKED!");
						event.setCancelled(true);
						return;
					}
				} else {
					oPlayer.SendMsg("&cThat mob type cant be LOCKED!");
				}
			}
		}

		//OVERRIDE feather tool
		if(Util.toolCheck(player.getItemInHand(),"override", player)){
			oPlayer = plugin.oasisplayer.get(Util.getOwner(plugin, entity));
			if (oPlayer!=null){
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
		if(plugin.eList.contains(event.getChunk())){
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
	public void OnCraftItem(CraftItemEvent event){
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnArrowShot(ProjectileLaunchEvent event){
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if (event.getEntity().getShooter() instanceof Player) {
				Player player = (Player) event.getEntity().getShooter();
				OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
				if (player.getWorld().getName().equals("pvpworld") || oPlayer.staff) {
					ArrowType myarrow = ArrowType.valueOf(plugin.chat.getPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none"));
					for(ItemStack item:oPlayer.quiver.getContents()){
						if(item.getItemMeta().getLore().get(0).equalsIgnoreCase(myarrow.toString())){
							if(item.getAmount()==1){
								oPlayer.quiver.remove(item);
								oPlayer.saveMe();
							} else {
								oPlayer.quiver.remove(item);
								item.setAmount(item.getAmount() - 1);
								oPlayer.quiver.addItem(item);
							}
							event.setCancelled(true);
							Arrow newarrow = player.launchProjectile(Arrow.class);
							arrow.setVelocity(event.getEntity().getVelocity());
							return;
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void OnMyEvent(ExplosiveArrowEvent event){
		
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnArrowHit(ProjectileHitEvent event){
		List<BlockState> blocks = new ArrayList<BlockState>();
		Location loc = event.getEntity().getLocation();
		if(event.getEntityType().equals(EntityType.ARROW)){
			Arrow arrow = (Arrow) event.getEntity();
			if(arrow.getShooter() instanceof Player){
				Player player = (Player) arrow.getShooter();
				ArrowType myarrow = ArrowType.valueOf(plugin.chat.getPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none"));
				if(!myarrow.equals(ArrowType.NONE)){
					switch(myarrow){
					case BLINDNESS: //EntityDamageEntityEvent
					case POISON: //EntityDamageEntityEvent
					case EXPLOSIVE:
						blocks = Util.region(loc.clone().add(5, 5, 5),loc.clone().add(-5, -5, -5),Material.AIR, Material.FIRE);
						Util.restoreState(plugin, blocks);
						ExplosiveArrowEvent explosivearrowevent = new ExplosiveArrowEvent(arrow,loc,blocks);
						plugin.getServer().getPluginManager().callEvent(explosivearrowevent);
						if (!explosivearrowevent.isCancelled()) {
							//plugin.getServer().getPluginManager().callEvent(new EntityExplodeEvent(arrow,loc,blocks,0F));
							loc.getWorld().createExplosion(loc, 3F, true);
							arrow.remove();
						}
					case WEB:
						blocks = Util.circle(loc,3,3,false,true,0,Material.WEB);
						Util.restoreState(plugin, blocks);
						for(BlockState block:blocks){
							if(block.getBlock().getType().equals(Material.AIR)){
								block.getBlock().setType(Material.WEB);
							}
						}
						arrow.remove();
					case SOUL:
						blocks = Util.circle(loc.clone().add(0, -1, 0),3,1,false,true,0,Material.SOUL_SAND);
						Util.restoreState(plugin, blocks);
						for(BlockState block:blocks){
							if(!block.getBlock().getType().equals(Material.AIR)){
								block.getBlock().setType(Material.SOUL_SAND);
							}
						}
						arrow.remove();
					case SAND: //Task
					case LIGHTNING:
						arrow.getWorld().strikeLightning(arrow.getLocation());
						arrow.remove();
					case FIREWORKS: //Task
					case DRUNK: //EntityDamageEntityEvent
					case FREEZE:
						blocks = Util.circle(loc, 3, 3, false, true, 0,Material.ICE);
						Util.restoreState(plugin, blocks);
						for(BlockState block:blocks){
							if (block.getBlock().getType().equals(Material.AIR)) {
								block.getBlock().setType(Material.ICE);
							}
						}
						arrow.remove();
					default:
						
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if (event.getAction()==Action.RIGHT_CLICK_BLOCK){
			//Troll tool code
			if(Util.toolCheck(player.getItemInHand(),"troll",player)){
				if(player.getItemInHand().getItemMeta().hasLore()){
					List<String> list = player.getItemInHand().getItemMeta().getLore();
					if(plugin.CoreProtect==null){plugin.getLogger().info("CoreProtect is null");}
					plugin.CoreProtect.logRemoval(list.get(0), event.getClickedBlock().getLocation(), event.getClickedBlock().getTypeId(), event.getClickedBlock().getData());
					plugin.CoreProtect.logPlacement(list.get(0), event.getClickedBlock().getLocation(), Integer.parseInt(list.get(1)),(byte) 0);
					event.getClickedBlock().setTypeId(Integer.parseInt(list.get(1)));
					event.setCancelled(true);
					return;
				}
			}

			if (Util.toolCheck(player.getItemInHand(),"nophysics",player)) {
				//Power tool code
				Block block = event.getClickedBlock();
				event.setCancelled(true);
				block.setTypeIdAndData(Material.AIR.getId(), (byte) 0x8, false);
				return;
			}
			//Boom tool code
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

			//TPALL tool code
			if (Util.toolCheck(player.getItemInHand(),"tpall",player)) {
				int radius = 10;
				if (player.getItemInHand().getItemMeta().getDisplayName().length() > 5) {
					try {
						radius = Integer.parseInt(player.getItemInHand().getItemMeta().getDisplayName().substring(5));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						player.sendMessage(ChatColor.RED + "Not a number!");
					}
				}
				OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
				for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
					if (entity instanceof LivingEntity) {
						oPlayer.toggleTP(entity);
						((LivingEntity) entity).setRemoveWhenFarAway(false);
					}
				}
				return;
			}

			//FLOAT tool code
			if (Util.toolCheck(player.getItemInHand(),"float",player)) {
				event.getClickedBlock().getWorld().spawnFallingBlock(event.getClickedBlock().getLocation().add(0, 1, 0), event.getClickedBlock().getType(), (byte) 0).setVelocity(new Vector(0, 2, 0));
				event.getClickedBlock().setType(Material.AIR);
				plugin.CoreProtect.logRemoval("FT_" + player.getName(), event.getClickedBlock().getLocation(), event.getClickedBlock().getTypeId(), event.getClickedBlock().getState().getRawData());
				event.setCancelled(true);
				return;
			}

			//DROP tool code
			if (Util.toolCheck(player.getItemInHand(),"drop",player)) {
				event.getClickedBlock().getWorld().spawnFallingBlock(event.getClickedBlock().getLocation().add(0, 1, 0), event.getClickedBlock().getType(), (byte) 0);
				event.getClickedBlock().setType(Material.AIR);
				plugin.CoreProtect.logRemoval("DT_" + player.getName(), event.getClickedBlock().getLocation(), event.getClickedBlock().getTypeId(), event.getClickedBlock().getState().getRawData());
				event.setCancelled(true);
				return;
			}

			//FALCONPUNCH tool code
			if (Util.toolCheck(player.getItemInHand(),"falconpunch",player)){
				int radius = 10;
				if (player.getItemInHand().getItemMeta().getDisplayName().length() > 11) {
					try {
						radius = Integer.parseInt(player.getItemInHand().getItemMeta().getDisplayName().substring(11));
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						player.sendMessage(ChatColor.RED + "Not a number!");
					}
				}
				List<BlockState> blocks = Util.circle(event.getClickedBlock().getLocation(), radius, radius, false, true, 0);
				for(BlockState block:blocks){
					block.getBlock().breakNaturally();
					plugin.CoreProtect.logRemoval("FP_" + player.getName(), block.getLocation(), block.getTypeId(), block.getRawData());
				}
			}

			//TP tool code
			if (Util.toolCheck(player.getItemInHand(),"tp",player)) {
				OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
				oPlayer.tpanimal(event.getClickedBlock().getLocation().clone().add(0, 1, 0));
				event.setCancelled(true);
				return;
			}

			//cat and dog code
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
						cat.setCatType(Util.getCatType());
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

			//appletree code
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
			if (oPlayer!=null) {
				if (!oPlayer.isIgnoring()) {
					if (!oPlayer2.staff) {
						oPlayer.SendMsg(plugin.joinmsg.replace("{DISPLAYNAME}", event.getPlayer().getDisplayName()));
					}
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
		if (!event.isCancelled()) {
			if (event.getLocation().getWorld().getName().equals("world")) {
				if (event.getEntityType().equals(EntityType.ZOMBIE)) {
					int i = Util.randomNum(1, 256);
					if (i == 127) {
						event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.GIANT);
						event.getEntity().remove();
						event.setCancelled(true);
					}
				}
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
				if(Util.randomNum(1,200)==69){
					event.getDrops().add(new ItemStack(Material.IRON_BLOCK,32));
				}

				if(Util.randomNum(1,200)==169){
					event.getDrops().add(new ItemStack(Material.DIAMOND_SWORD,1));
				}

				if(Util.randomNum(1,1000000)==1000){
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerAttackAnimal(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Arrow){
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Player){
				Player player = (Player) arrow.getShooter();
				if(Util.getMetadata(arrow, "name", plugin).equalsIgnoreCase("poison")){
					if(event.getEntity() instanceof Player){
						Player poisoned = (Player) event.getEntity();
						poisoned.addPotionEffect(new PotionEffect(PotionEffectType.POISON,100,1));
						arrow.remove();
					}
				} else if(Util.getMetadata(arrow, "name", plugin).equalsIgnoreCase("drunk")){
					if(event.getEntity() instanceof Player){
						Player poisoned = (Player) event.getEntity();
						poisoned.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,100,100));
						arrow.remove();
					}
				} else if(Util.getMetadata(arrow, "name", plugin).equalsIgnoreCase("blindness")){
					if(event.getEntity() instanceof Player){
						Player poisoned = (Player) event.getEntity();
						poisoned.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,100));
						arrow.remove();
					}
				}
			}
		}
		if(event.getDamager() instanceof Player){
			OasisPlayer owner = Util.getOwner(plugin, event.getEntity());
			if (owner != null) {
				Player player = (Player) event.getDamager();
				OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
				if (!oPlayer.getName().equals(owner.getName())) {
					oPlayer.SendMsg("&6This mob is &cLOCKED&6!");
					event.setCancelled(true);
					return;
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
				plugin.getServer().broadcastMessage(Util.ColorChat("&1<&4OwnerCast&1> &e" + event.getMessage().substring(4)));
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
					Util.sendHorse(plugin, horse,player);
				}
			}
		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerPlaceBlock(BlockPlaceEvent event){
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
		}

		if (event.getBlock().getType().equals(Material.WALL_SIGN)||event.getBlock().getType().equals(Material.SIGN_POST)){
			if(event.getPlayer().isOp()){
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

	@EventHandler(priority=EventPriority.LOWEST)
	public void EnchantEvent(PrepareItemEnchantEvent event){
		OasisPlayer oPlayer = Util.getOPlayer(plugin, event.getEnchanter().getName());
		if(event.getInventory().equals(oPlayer.etable)){
			if (event.getItem()!=null) {
				event.getExpLevelCostsOffered()[0] = 30;
				event.getExpLevelCostsOffered()[1] = 30;
				event.getExpLevelCostsOffered()[2] = 30;
			}
		}
	}
}

package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_7_R1.Explosion;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.daemitus.deadbolt.Deadbolt;
import com.vexsoftware.votifier.model.VotifierEvent;

public class OasisExtrasListener implements Listener{

	private OasisExtras plugin;
	public static Economy economy;


	public OasisExtrasListener(OasisExtras plugin){
		this.plugin = plugin;
	}

	int joinTimer = 30;
	private OasisPlayer oPlayer;
	int mytask,mytask2,arrowTask;
	private Map<String, OasisPlayer> syncOasisPlayer;

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPing(ServerListPingEvent event){
		String[] colors = new String[] {"§c","§6","§e","§2","§1","§d","§5"};
		Random r = new Random();
		List<String> colorList = Arrays.asList(colors);
		String randomColor = (String) colorList.get(r.nextInt(colorList.size()));
		String address = event.getAddress().toString().substring(1);
		String name = GetPlayerName(address);
		if(plugin.getConfig().getString("motdcolor").equals("random")){

			event.setMotd(randomColor + name + plugin.getConfig().getString("motd"));
		} else {

			event.setMotd(plugin.getConfig().getString("motdcolor") + name + plugin.getConfig().getString("motd"));
		}
	}

	public String GetPlayerName(String address){
		for(OasisPlayer oPlayer:plugin.myplayers){
			if(oPlayer.address.equals(address)){
				return "Hey " + oPlayer.getName() + ", ";
			}
		}
		return "";
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
						oPlayer.SendMsg(oPlayer.bcolor +"[" + oPlayer.fprefixcolor + oPlayer.fprefix + oPlayer.bcolor + "]&r" + event.getPlayer().getDisplayName() + "&r: " + plugin.chat.getPlayerInfoString(event.getPlayer().getWorld(), event.getPlayer().getName(), "message","&r" ) + oPlayer.fchat + event.getMessage());
						event.getRecipients().remove(oPlayer.getPlayer());
					}
				}
			}
		}
		event.setMessage(ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerInfoString(event.getPlayer().getWorld(), event.getPlayer().getName(), "message","&r" ) + event.getMessage()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPreLogin(AsyncPlayerPreLoginEvent event){
		if(plugin.closed){
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Server under maintenance!");
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
		oPlayer = plugin.oasisplayer.get(event.getPlayer().getName());
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
					event.setCancelled(true);
					return;
				}
			}
		}

		//TP feather tool
		if(Util.toolCheck(player.getItemInHand(),"tp",player)){
			if (entity instanceof LivingEntity) {
				oPlayer = plugin.oasisplayer.get(player.getName());
				if (Util.getMobs(entity) || oPlayer.staff) {
					if (Util.getOwner(plugin, entity)==null || oPlayer.staff) {
						oPlayer.toggleTP(entity);
						event.setCancelled(true);
						return;
					}
				} else {
					oPlayer.SendMsg("&cCan't teleport that mob type!");
					event.setCancelled(true);
					return;
				}
			}
		}

		//LOCK feather tool
		if(Util.toolCheck(player.getItemInHand(),"lock",player)){
			if(entity instanceof Player){
				event.setCancelled(true);
				return;
			}
			if (entity instanceof LivingEntity) {
				oPlayer = plugin.oasisplayer.get(player.getName());
				if (Util.getMobs(entity)) {
					if (Util.getOwner(plugin, entity) == null) {
						oPlayer.lockAnimal(entity);
						oPlayer.saveMe();
						event.setCancelled(true);
						return;
					} else if (Util.getOwner(plugin, entity).getName().equals(oPlayer.getName())) {
						oPlayer.lockAnimal(entity);
						oPlayer.saveMe();
						event.setCancelled(true);
						return;
					} else {
						oPlayer.SendMsg("&6That mob is already &cLOCKED!");
						event.setCancelled(true);
						return;
					}
				} else if (oPlayer.staff) {
					if (Util.getOwner(plugin, entity) == null) {
						oPlayer.lockAnimal(entity);
						oPlayer.saveMe();
						event.setCancelled(true);
						return;
					} else if (Util.getOwner(plugin, entity).getName().equals(oPlayer.getName())) {
						oPlayer.lockAnimal(entity);
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
					event.setCancelled(true);
					return;
				}
			}
		}

		//OVERRIDE feather tool
		if(Util.toolCheck(player.getItemInHand(),"override", player)){
			if(entity instanceof Player){
				event.setCancelled(true);
				return;
			}
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
		for(Recipe recipe: plugin.recipes){
			if(event.getRecipe().getResult().hasItemMeta()){
				if(event.getRecipe().getResult().getItemMeta().hasLore()){
					if(event.getRecipe().getResult().getItemMeta().getLore().get(0).equals(recipe.getResult().getItemMeta().getLore().get(0))){
						if(event.getWhoClicked() instanceof Player){
							Player player = (Player) event.getWhoClicked();
							if(!player.hasPermission("oasisextras.arrow." + recipe.getResult().getItemMeta().getLore().get(0))){
								event.setCancelled(true);
								player.updateInventory();
								Util.SendMsg(player, "&cYou do not have permissions to craft that!");
								return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnArrowShot(EntityShootBowEvent event){
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
			if (player.getWorld().getName().equals("pvpworld") || oPlayer.staff) {
				String myarrow = plugin.chat.getPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none");
				if(myarrow!="none"){
					for(ItemStack item:oPlayer.quiver.getContents()){
						if (item!=null) {
							if (item.getItemMeta().getLore().get(0).equalsIgnoreCase(myarrow)) {
								if (oPlayer.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
									if (item.getAmount() == 1) {
										oPlayer.quiver.removeItem(item);
										oPlayer.saveMe();
									} else {
										oPlayer.quiver.removeItem(item);
										item.setAmount(item.getAmount() - 1);
										oPlayer.quiver.addItem(item);
										oPlayer.saveMe();
									}
								}
								Arrow newarrow = player.getWorld().spawn(event.getProjectile().getLocation(), Arrow.class);
								newarrow.setVelocity(event.getProjectile().getVelocity());
								event.setCancelled(true);
								newarrow.setShooter(player);
								player.updateInventory();
								if(myarrow.equalsIgnoreCase("sand")){
									Util.sandArrow(plugin, newarrow);
								} else if (myarrow.equalsIgnoreCase("fireworks")){
									Util.fireworksArrow(plugin, newarrow);
								}
								return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnBoom(EntityExplodeEvent event){
		if (!event.isCancelled()) {
			if (event.getEntity() instanceof Arrow) {
				List<BlockState> blocks = new ArrayList<BlockState>();
				for (Block block : event.blockList()) {
					if (!Deadbolt.isProtected(block)) {
						blocks.add(block.getState());
					}
				}
				//event.blockList().clear();
				Util.restoreState(plugin, blocks);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnArrowHit(ProjectileHitEvent event){
		List<BlockState> blocks = new ArrayList<BlockState>();
		Location loc = event.getEntity().getLocation();
		if(event.getEntityType().equals(EntityType.ARROW)){
			Arrow arrow = (Arrow) event.getEntity();
			if(arrow.getShooter() instanceof Player){
				Player player = (Player) arrow.getShooter();
				String myarrow = plugin.chat.getPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none");
				switch(myarrow){
				case "none":
					break;
				case "explosion":
					net.minecraft.server.v1_7_R1.Entity craftarrow = (net.minecraft.server.v1_7_R1.Entity)((CraftEntity)arrow).getHandle();
					Explosion explosion = new Explosion(craftarrow.world,craftarrow,craftarrow.locX,craftarrow.locY,craftarrow.locZ,3F);
					explosion.a();
					explosion.a(true);
					arrow.remove();
					break;
				case "web":
					blocks = Util.circle(loc,3,3,false,true,0,Material.WEB);
					Util.restoreState(plugin, blocks);
					for(BlockState block:blocks){
						if(block.getBlock().getType().equals(Material.AIR)){
							block.getBlock().setType(Material.WEB);
						}
					}
					arrow.remove();
					break;
				case "soul":
					blocks = Util.circle(loc.clone().add(0, -1, 0),3,1,false,true,0,Material.SOUL_SAND);
					Util.restoreState(plugin, blocks);
					for(BlockState block:blocks){
						if(!block.getBlock().getType().equals(Material.AIR)){
							block.getBlock().setType(Material.SOUL_SAND);
						}
					}
					arrow.remove();
					break;
				case "lightning":
					arrow.getWorld().strikeLightning(arrow.getLocation());
					arrow.remove();
					break;
				case "freeze":
					blocks = Util.circle(loc, 3, 3, false, true, 0,Material.ICE);
					Util.restoreState(plugin, blocks);
					for(BlockState block:blocks){
						if (block.getBlock().getType().equals(Material.AIR)) {
							block.getBlock().setType(Material.ICE);
						}
					}
					arrow.remove();
					break;
				case "teleport":
					player.teleport(loc);
					arrow.remove();
					break;
				}
				OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
				if(!Util.arrowCheck(oPlayer.quiver.getContents(), myarrow)){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none");
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
				} event.setCancelled(true);
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
		Player player = (Player) event.getPlayer();
		if(event.getInventory().getHolder() instanceof Horse){
			Horse horse = (Horse) event.getInventory().getHolder();
			if(Util.getOwner(plugin, horse)!=null){
				if(!Util.getOwner(plugin, horse).getName().toLowerCase().equals(player.getName().toLowerCase())){
					Util.SendMsg(player, "&cThis is locked to " + Util.getOwner(plugin, horse).getName() + "!");
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
			if(Util.getOwner(plugin, horse)!=null){
				if(!Util.getOwner(plugin, horse).getName().toLowerCase().equals(player.getName().toLowerCase())){
					Util.SendMsg(player, "&cThis is locked to " + Util.getOwner(plugin, horse).getName() + "!");
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerQuit(PlayerQuitEvent event){
		OasisPlayer oPlayer = plugin.oasisplayer.get(event.getPlayer().getName());
		//		if (oPlayer.isOnline()) {
		//			if (!oPlayer.staff) {
		//				event.setQuitMessage(null);
		//				for (OasisPlayer oPlayer2 : plugin.myplayers) {
		//					if (oPlayer2.isOnline()) {
		//						if (!oPlayer2.isIgnoring()) {
		//							oPlayer2.SendMsg(plugin.getConfig().getString("quit").replace("{DISPLAYNAME}", oPlayer.getPlayer().getDisplayName()));
		//						}
		//					}
		//				}
		//			}
		//		}
		oPlayer.offLine();

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerKick(PlayerKickEvent event){
		OasisPlayer oPlayer = plugin.oasisplayer.get(event.getPlayer().getName());
		//		if(!oPlayer.staff){
		//			event.setLeaveMessage(null);
		//			for(OasisPlayer oPlayer2 : plugin.myplayers){
		//				if (oPlayer2.isOnline()) {
		//					if (!oPlayer2.isIgnoring()) {
		//						oPlayer2.SendMsg(plugin.getConfig().getString("kick").replace("{DISPLAYNAME}", oPlayer.getPlayer().getDisplayName()));
		//					}
		//				}
		//			}
		//		}
		if(oPlayer.isRaging()){
			event.setLeaveMessage(ChatColor.YELLOW + event.getPlayer().getName() + " Raaaaaaaaage quit!");
			plugin.getServer().broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + " Raaaaaaaaage quit!");
			event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
			oPlayer.toggleRage();
		}
		oPlayer.offLine();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerJoin(PlayerJoinEvent event){
		final Player player = event.getPlayer();
		if (!plugin.oasisplayer.containsKey(player.getName())) {
			plugin.oasisplayer.put(player.getName(), new OasisPlayer(plugin,player.getName()));
		}

		OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
		//		if(!oPlayer.staff){
		//			Util.bCast("not staff");
		//			event.setJoinMessage(null);
		//			for(OasisPlayer oPlayer2 : plugin.myplayers){
		//				if (oPlayer2.isOnline()) {
		//					Util.bCast("this player is online");
		//					if (!oPlayer2.isIgnoring()) {
		//						Util.bCast("this player isnt ignoring");
		//						oPlayer2.SendMsg(plugin.getConfig().getString("join").replace("{DISPLAYNAME}", oPlayer.getPlayer().getDisplayName()));
		//					}
		//				}
		//			}
		//		}
		oPlayer.onLine();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void OnPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()){
			event.setCancelled(false);
		}

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
	public void OnEntityAttackEntity(EntityDamageByEntityEvent event){
		//custom arrow code
		if(event.getDamager() instanceof Arrow){
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Player){
				Player player = (Player) arrow.getShooter();
				if(plugin.chat.getPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none").equalsIgnoreCase("poison")){
					if(event.getEntity() instanceof Player){
						Player poisoned = (Player) event.getEntity();
						poisoned.addPotionEffect(new PotionEffect(PotionEffectType.POISON,100,1));
						arrow.remove();
					}
				} else if(plugin.chat.getPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none").equalsIgnoreCase("confusion")){
					if(event.getEntity() instanceof Player){
						Player poisoned = (Player) event.getEntity();
						poisoned.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,200,255));
						arrow.remove();
					}
				} else if(plugin.chat.getPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none").equalsIgnoreCase("blindness")){
					if(event.getEntity() instanceof Player){
						Player poisoned = (Player) event.getEntity();
						poisoned.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,100,100));
						arrow.remove();
					}
				}
			}
		}
		if(event.getDamager() instanceof Player){
			//frozen attack code
			if (plugin.oasisplayer.get(((Player) event.getDamager()).getName()).isFrozen()){
				event.setCancelled(true);
				return;
			}
			//animal attack code
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
		plugin.telnet.sendMsg(event.getPlayer().getDisplayName() + " issued server command: " + event.getMessage());

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
		Player player = event.getPlayer();
		if(player.hasPermission("oasisextras.staff.gh")){
			if (event.getItemInHand().getType().equals(Material.WOOL)) {
				if (event.getItemInHand().hasItemMeta()) {
					if (event.getItemInHand().getItemMeta().hasDisplayName()) {
						if (event.getItemInHand().getItemMeta().getDisplayName().equals("ghpos1")) {
							plugin.ghpos1 = event.getBlock().getLocation();
							Util.SendMsg(player, "&6[&aGH&6] - &aPosition 1 set!");
							event.setCancelled(true);
							return;
						} else if (event.getItemInHand().getItemMeta().getDisplayName().equals("ghpos2")) {
							plugin.ghpos2 = event.getBlock().getLocation();
							Util.SendMsg(player, "&6[&aGH&6] - &aPosition 2 set!");
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}
		if (plugin.oasisplayer.get(player.getName()).isFrozen()){
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
		}

		if (event.getBlock().getType().equals(Material.WALL_SIGN)||event.getBlock().getType().equals(Material.SIGN_POST)){
			if(player.isOp()){
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
		OasisPlayer oPlayer = Util.getOPlayer(plugin, event.getVote().getUsername());
		if(oPlayer!=null){
			oPlayer.votes++;
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

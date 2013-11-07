package net.charter.orion_pax.OasisExtras;

import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
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
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class OasisExtrasListener implements Listener{

	private OasisExtras plugin;

	public OasisExtrasListener(OasisExtras plugin){
		this.plugin = plugin;
	}

	int joinTimer = 30;
	int mytask,mytask2;

	@EventHandler
	public void OnPlayerInteractEntity(PlayerInteractEntityEvent event){
		Entity entity = event.getRightClicked();
		final Player player = event.getPlayer();
		if(player.getItemInHand().getType().equals(Material.STICK) && player.hasPermission("oasisextras.staff.getowner")){
			if (entity instanceof Horse){
				Horse horse = (Horse) entity;
				player.sendMessage(horse.getOwner().getName());
				return;
			}

			if (entity instanceof Wolf){
				Wolf wolf = (Wolf) entity;
				player.sendMessage(wolf.getOwner().getName());
				return;
			}

			if (entity instanceof Ocelot){
				Ocelot ocelot = (Ocelot) entity;
				player.sendMessage(ocelot.getOwner().getName());
				return;
			}

			if (getOwner(entity)!=null){
				player.sendMessage(getOwner(entity));
				return;
			}
		}

		if(player.getItemInHand().getType().equals(Material.FEATHER)){
			OasisPlayer oPlayer = plugin.oasisplayer.get(player.getName());
			if(player.hasPermission("oasisextras.staff.teleportall")){
				oPlayer.toggleTP(entity);
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
			if(getMobs(entity)){
				oPlayer.toggleTP(entity);
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
			} else {
				oPlayer.sendMessage(ChatColor.RED + "Can't teleport that mob type!");
				return;
			}
		}

		if(player.getItemInHand().getType().equals(Material.BLAZE_ROD) && player.hasPermission("oasisextras.staff.override")){
			if (getOwner(entity)!=null){
				OasisPlayer oPlayer = plugin.oasisplayer.get(getOwner(entity));
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

	@EventHandler
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

	@EventHandler
	public void OnPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if (event.getAction()==Action.RIGHT_CLICK_BLOCK){
			if (player.getItemInHand().getType().equals(Material.FEATHER)){
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

	@EventHandler
	public void OnPlayerOpenDonkeyInventory(InventoryOpenEvent event){
		if(event.getInventory().getHolder() instanceof Horse){
			Horse horse = (Horse) event.getInventory().getHolder();
			if(horse.getOwner()!=null){
				if(!horse.getOwner().getName().equals(event.getPlayer().getName()) && GetAnimalPerm(event.getPlayer().getName())==false){
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void OnPlayerEnterHorse(VehicleEnterEvent event){
		if (event.getVehicle() instanceof Horse) {
			Player player = (Player) event.getEntered();
			Horse horse = (Horse) event.getVehicle();
			if(horse.getOwner()!=null){
				if(horse.getOwner().getName()!=player.getName() && GetAnimalPerm(player.getName())==false){
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	public boolean GetAnimalPerm(String name){
		Iterator it = plugin.oasisplayer.entrySet().iterator();
		while(it.hasNext()){
			Entry entry = (Entry) it.next();
			if(((OasisPlayer) entry.getValue()).hasPerm(name)){
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void OnPlayerQuit(PlayerQuitEvent event){
		if (plugin.oasisplayer.containsKey(event.getPlayer().getName())) {
			plugin.oasisplayer.get(event.getPlayer().getName()).saveMe();
			plugin.oasisplayer.remove(event.getPlayer().getName());
		}
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

	@EventHandler()
	public void OnCreatureSpawn(CreatureSpawnEvent event){
		if (event.getEntityType().equals(EntityType.ZOMBIE)){
			int i = randomNum(1,333);
			if (i == 127) {
				event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.GIANT);
				event.getEntity().remove();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnFrozenAttack(EntityDamageByEntityEvent event){
		if (event.getDamager() instanceof Player){
			if (plugin.oasisplayer.get(((Player) event.getDamager()).getName()).isFrozen()){
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerAttackAnimal(EntityDamageByEntityEvent event){

		//protecting animal code
		Iterator i = plugin.oasisplayer.entrySet().iterator();
		while(i.hasNext()){
			Entry entry = (Entry) i.next();
			OasisPlayer myplayer = (OasisPlayer) entry.getValue();
			if (myplayer.isMyAnimal(event.getEntity().getUniqueId().toString())){
				if (event.getDamager() instanceof Player){
					Player player = (Player) event.getDamager();
					if (player.getName()!= (String) entry.getKey()){
						if (myplayer.hasPerm(player.getName())==false) {
							event.setCancelled(true);
							return;
						}
					}

				} else if(event.getDamager() instanceof Arrow){
					Entity shooter = ((Arrow)event.getDamager()).getShooter();
					if( shooter instanceof Player){
						Player player = (Player) shooter;
						if (player.getName()!= (String) entry.getKey()){
							if (((OasisPlayer) entry.getValue()).hasPerm(player.getName())==false) {
								event.setCancelled(true);
								return;
							}
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerTagAnimal(EntityDamageByEntityEvent event){
		//Tagging code
		if (event.getDamager() instanceof Player){
			Entity entity = event.getEntity();
			Player player = (Player) event.getDamager();
			if (player.getItemInHand().getType().equals(Material.STICK)){
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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void FrozenBlockBreak(BlockBreakEvent event) {
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
			event.setCancelled(true);
			return;
		}
		
		if (event.getBlock().getType().equals(Material.SIGN)||event.getBlock().getType().equals(Material.SIGN_POST)){
			if(event.getPlayer().isOp()){
				
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void AppleTreeBreak(BlockBreakEvent event) {
		if (plugin.appletree.containsKey(event.getBlock().getLocation())){
			TreeTask temp = (TreeTask) plugin.appletree.get(event.getBlock().getLocation());
			plugin.appletree.remove(event.getBlock().getLocation());
			temp.cancel();
			plugin.delTree();
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void UnderFrozenBlockBreak(BlockBreakEvent event){

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

	public void OnServerShutDown(ServerCommandEvent event){
		if (event.getCommand().equals("stop")){
			for(Player player : plugin.getServer().getOnlinePlayers()){
				OasisPlayer myplayer = plugin.oasisplayer.get(player.getName());
				myplayer.saveMe();
				plugin.getLogger().info(myplayer.getName() + " is saved!");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
//		if (event.getMessage().equals("/v")){
//			if(plugin.getConfig().getBoolean("IgnoreMadV")){
//				if(event.getPlayer().getName().equalsIgnoreCase("madscientist032")){
//					event.getPlayer().sendMessage(ChatColor.GOLD + "Forcing /vanish");
//					event.getPlayer().performCommand("vanish");
//					event.setCancelled(true);
//				}
//			}
//		}
		if (event.getMessage().contains("/stop")){
			if(plugin.getConfig().getBoolean("IgnorePaxStop")){
				if(event.getPlayer().getName().equalsIgnoreCase("paxination")){
					event.getPlayer().sendMessage(ChatColor.DARK_RED + "RETARD! You cant do that here!");
					event.setCancelled(true);
				}
			}

			for(Player player : plugin.getServer().getOnlinePlayers()){
				OasisPlayer myplayer = plugin.oasisplayer.get(player.getName());
				myplayer.saveMe();
				plugin.getLogger().info(myplayer.getName() + " is saved!");
			}
		}

		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			if (event.getMessage().contains("/")){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "COMMANDS ARE DISABLED WHILE " + ChatColor.AQUA + "FROZEN!");
			}
		}

		if (event.getMessage().contains("/warp") || event.getMessage().contains("/tp") || event.getMessage().contains("/home") || event.getMessage().contains("/back") || event.getMessage().contains("/spawn")){
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
				horse.teleport(player.getLocation().add(5, 0, 5));
				plugin.getServer().getScheduler().cancelTask(mytask);
			}
		}, 100L);
		
		mytask2 = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				horse.setPassenger(player);
				Iterator it = plugin.horsetp.entrySet().iterator();
				while(it.hasNext()){
					Entry entry = (Entry) it.next();
					if(entry.getValue().equals(horse)){
						plugin.getServer().broadcast("Chunk unloaded", "debug");
						it.remove();
					}
				}
				plugin.getServer().getScheduler().cancelTask(mytask2);
			}
		}, 200L);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerPlaceBlock(BlockPlaceEvent event){
		if (plugin.oasisplayer.get(event.getPlayer().getName()).isFrozen()){
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
		}
		
		if (event.getBlock().getType().equals(Material.SIGN)||event.getBlock().getType().equals(Material.SIGN_POST)){
			if(event.getPlayer().isOp()){
				//save sign location here
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
		Iterator it = plugin.oasisplayer.entrySet().iterator();
		while(it.hasNext()){
			Entry entry = (Entry) it.next();
			OasisPlayer oplayer = (OasisPlayer) entry.getValue();
			if(oplayer.isMyAnimal(entity.getUniqueId().toString())){
				return oplayer.getName();
			}
		}
		return null;
	}

}

package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.server.v1_7_R1.PacketPlayOutSetSlot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class OasisPlayer {

	private final OasisExtras plugin;

	private final String name;
	private List<String> animals = new ArrayList<String>();
	public boolean frozen = false;
	public boolean online = false;
	public boolean glow = false;
	public boolean eventnotify = true; 
	public boolean raging = false;
	private boolean medic = false;
	private BlockState gbstate;
	public Location loc = null;
	public boolean staff = false;
	public List<Entity> tplist = new ArrayList<Entity>();
	private MyConfigFile playerfile;
	private BukkitTask aura;
	public Material auramat = Material.GHAST_TEAR;
	private ItemStack auraitem;
	private ItemMeta auraname;
	private List<String> auralore = new ArrayList<String>();
	public boolean trail = false;
	public boolean auratoggle = false;
	public boolean weatherman = false;
	public boolean joinquitkickignore = false;
	public Location ssloc1,ssloc2;
	public List<String> friends = new ArrayList<String>();
	public String bcolor,fprefixcolor,fchat,address,fprefix;
	private BukkitTask discotask,randomColorArmor;
	private BukkitRunnable tptimer;
	private List<BlockState> floor = new ArrayList<BlockState>();
	public boolean disco,rCA = false,ftrail=false;
	public int votes=0;
	public float speed;
	public Inventory etable,quiver;

	public OasisPlayer(OasisExtras plugin, String myname){
		this.plugin = plugin;
		name = myname;
		
		newTPTimer();

		String filename = "players/" + name + ".yml";
		playerfile = new MyConfigFile(plugin, filename);

		animals = playerfile.getConfig().getStringList("animals");
		frozen=playerfile.getConfig().getBoolean("frozen",frozen);

		friends=playerfile.getConfig().getStringList("friends");
		fprefix=playerfile.getConfig().getString("friendprefix", "Friend");
		bcolor=playerfile.getConfig().getString("friendlistbracketcolor","&c");
		fprefixcolor=playerfile.getConfig().getString("friendprefixcolor", "&b");
		fchat=playerfile.getConfig().getString("friendschatcolor", "&b");
		auramat = Material.getMaterial(playerfile.getConfig().getString("auramat",Material.GHAST_TEAR.toString()));
		auraitem = new ItemStack(auramat,1);
		auraname = auraitem.getItemMeta();
		eventnotify = playerfile.getConfig().getBoolean("eventnotify", true);
		if(!eventnotify){
			plugin.getServer().dispatchCommand(plugin.console, "manuaddp " + name + "-oasisextras.player.event.notify");
		}

		votes = playerfile.getConfig().getInt("votes", 0);

		glow = playerfile.getConfig().getBoolean("glowing",false);

		trail = playerfile.getConfig().getBoolean("trail", false);

		auratoggle = playerfile.getConfig().getBoolean("aura", false);

		weatherman = playerfile.getConfig().getBoolean("weatherman", false);

		joinquitkickignore = playerfile.getConfig().getBoolean("joinquitkickignore", false);
		
		address = playerfile.getConfig().getString("address", "none");

		auralore.add("NO");

		auraname.setLore(auralore);
		auraitem.setItemMeta(auraname);
		
		quiver = Bukkit.createInventory(null, 18, "Quiver");
		etable = Bukkit.createInventory(null, InventoryType.ENCHANTING);
		if(playerfile.getConfig().contains("arrows")){
			if(playerfile.getConfig().contains("arrows.explosion")){
				ItemStack arrow = new ItemStack(plugin.explosionarrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.explosion.amount"));
				quiver.addItem(arrow);
			}
			
			if(playerfile.getConfig().contains("arrows.poison")){
				ItemStack arrow = new ItemStack(plugin.poisonarrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.poison.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.blindness")){
				ItemStack arrow = new ItemStack(plugin.blindarrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.blindness.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.freeze")){
				ItemStack arrow = new ItemStack(plugin.freezearrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.freeze.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.confusion")){
				ItemStack arrow = new ItemStack(plugin.confusionarrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.confusion.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.sand")){
				ItemStack arrow = new ItemStack(plugin.sandarrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.sand.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.web")){
				ItemStack arrow = new ItemStack(plugin.webarrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.web.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.soul")){
				ItemStack arrow = new ItemStack(plugin.soularrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.soul.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.fireworks")){
				ItemStack arrow = new ItemStack(plugin.fireworksarrow.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.fireworks.amount"));
				quiver.addItem(arrow);
			}

			if(playerfile.getConfig().contains("arrows.lightning")){
				ItemStack arrow = new ItemStack(plugin.lightningarrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.lightning.amount"));
				quiver.addItem(arrow);
			}
			
			if(playerfile.getConfig().contains("arrows.teleport")){
				ItemStack arrow = new ItemStack(plugin.tparrows.getResult());
				arrow.setAmount(playerfile.getConfig().getInt("arrows.teleport.amount"));
				quiver.addItem(arrow);
			}
		}

		saveMe();
	}
	
	public void newTPTimer(){
		tptimer = new BukkitRunnable(){
			@Override
			public void run() {
				tplist.clear();
				SendMsg("&cTimer ran out for animal tp list!  Please re-add to continue again!");
				tptimer = new BukkitRunnable(){
					@Override
					public void run() {
						tplist.clear();
						SendMsg("&cTimer ran out for animal tp list!  Please re-add to continue again!");
					}
				};
			}
		};
	}

	public void saveMe(){
		playerfile.getConfig().set("animals",animals);
		playerfile.getConfig().set("frozen", frozen);
		playerfile.getConfig().set("auramat", auramat.toString());
		playerfile.getConfig().set("eventnotify", eventnotify);
		playerfile.getConfig().set("glowing", glow);
		playerfile.getConfig().set("auratoggle", auratoggle);
		playerfile.getConfig().set("trail", trail);
		playerfile.getConfig().set("weatherman", weatherman);
		playerfile.getConfig().set("friends", friends);
		playerfile.getConfig().set("friendlistbracketcolor", bcolor);
		playerfile.getConfig().set("friendprefixcolor", fprefixcolor);
		playerfile.getConfig().set("friendschatcolor", fchat);
		playerfile.getConfig().set("joinquitkickignore", joinquitkickignore);
		playerfile.getConfig().set("votes", votes);
		playerfile.getConfig().set("address", address);
		playerfile.getConfig().set("friendprefix", fprefix);
		saveArrows();
		playerfile.saveConfig();
	}
	
	public int getFreeSlot(){
		for(int i=0; i<quiver.getSize()-1; i++){
			if(quiver.getItem(i)==null){
				return i;
			}
		}
		return 255;
	}
	
	public void saveArrows(){
		playerfile.getConfig().set("arrows", null);
		for(ItemStack arrow:quiver.getContents()){
			if (arrow!=null) {
				if (arrow.getType().equals(Material.ARROW)) {
					playerfile.getConfig().set("arrows." + arrow.getItemMeta().getLore().get(0).toLowerCase() + ".amount", arrow.getAmount());
				}
			}
		}
	}

	public void onLine(){

		address=getPlayer().getAddress().getAddress().toString().substring(1);
		playerfile.getConfig().set("address", address);
		saveMe();
		online=true;
		setLoc(getPlayer().getLocation());
		staff = getPlayer().hasPermission("oasischat.staff.staff") ? true : false;

		if(frozen){SendMsg("&6Your &bFROZEN&g!");}
		if(glow){SendMsg("&eGlowing &aEnabled");}
		if(auratoggle){
			SendMsg("&bAura is on!");
		}
		if(trail){SendMsg("&cTrail is &aEnabled!");}
		if(weatherman){SendMsg("&bWeather Channel is &aEnabled!");}
		if(joinquitkickignore){SendMsg("&eJoin/leave/kick notification &aEnabled!");}
	}

	public void Disco(){
		floor.addAll(Util.region(loc.clone().add(5, -1, 5),loc.clone().add(-5, -1, -5)));
		if(floor==null){plugin.getServer().broadcastMessage("floor is null");}
		discotask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (int i =0 ; i<10 ; i++) {
					getPlayer().sendBlockChange(floor.get(Util.randomNum(0, floor.size() - 1)).getLocation(), Material.STAINED_GLASS, (byte) Util.randomNum(0, 15));
				}
			}

		},0L,3L);
	}

	@SuppressWarnings("deprecation")
	public void stopDisco(){
		discotask.cancel();
		for(BlockState block:floor){
			getPlayer().sendBlockChange(block.getLocation(), block.getType(), block.getRawData());
		}
		floor.clear();
	}

	public void setMedic(){
		if(!medic){
			medic=!medic;
			SendMsg("&cMedic&r is now &aENABLED!");
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					medic=!medic;
					SendMsg("&cMedic&r is now &4disabled!");
				}

			}, 300L);
		} else {
			SendMsg("&cMedic&r is still &aENABLED!");
		}
	}

	public boolean isMedic(){
		return medic;
	}

	public boolean isIgnoring(){
		return joinquitkickignore;
	}

	public void toggleIgnoring(){
		joinquitkickignore = !joinquitkickignore;
		if(joinquitkickignore){
			SendMsg("&eJoin/leave/kick notification &aEnabled!");
		} else {
			SendMsg("&eJoin/leave/kick notification &cDisabled!");
		}
	}

	public boolean isFriend(String name){
		if(friends.contains(name)){
			return true;
		} else {
			return false;
		}
	}

	public boolean isFriend(Set<Player> players){
		for(Player player:players){
			if(this.isFriend(player.getName())){
				return true;
			}
		}
		return false;
	}

	public void addFriend(String name){
		if (!friends.contains(name)) {
			friends.add(name);
		}
		SendMsg(fchat + name + " added to firends list!");
		saveMe();
	}

	public void delFriend(String name){
		if (friends.contains(name)) {
			friends.remove(name);
		}
		SendMsg(fchat + name + " remvoed from friends list!");
		saveMe();
	}

	public void listFriends(){
		SendMsg(fchat + friends);
	}

	public void setFCHAT(String string){
		this.fchat = string;
		saveMe();
	}

	public void setFPREFIX(String string){
		this.fprefixcolor = string;
		saveMe();
	}
	
	public void setPREFIX(String string){
		this.fprefix = string;
		saveMe();
	}

	public void setBCOLOR(String string){
		this.bcolor = string;
		saveMe();
	}

	public boolean isRaging(){
		return raging;
	}

	public void toggleRage(){
		raging=!raging;
	}

	public boolean weather(){
		return weatherman;
	}

	public void toggleWeatherNotify(){
		weatherman=!weatherman;
		if(weatherman){
			SendMsg("&bWeather channel is &aEnabled&b!");
		} else {
			SendMsg("&bWeather channel is &cdisabled&b!");
		}
		saveMe();
	}

	public void toggleEventNotify(){
		if(eventnotify){
			plugin.getServer().dispatchCommand(plugin.console, "manuaddp " + name + " -oasisextras.player.event.notify");
			eventnotify = false;
			SendMsg("&aEvents: notification disabled!");
		} else {
			plugin.getServer().dispatchCommand(plugin.console, "manudelp " + name + " -oasisextras.player.event.notify");
			SendMsg("&aEvents: notification enabled!");
			eventnotify = true;
		}
		saveMe();
	}

	public void SendMsg(String msg){
		getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void toggleGlow(){
		glow=!glow;
		if(glow){
			SendMsg("&6Glow &aENABLED&6!");
		} else {
			gbstate.update(true);
			SendMsg("&6Glow &cDISABLED&6!");
		}
		saveMe();
	}

	public boolean isGlowing(){
		return glow;
	}

	public void Glow(Location loc){
		if(gbstate!=null){
			gbstate.update(true);
		}
		if (loc.getBlockY()<60) {
			if (!Util.isFarm(loc.clone().add(0, -1, 0))) {
				if (Util.isBlock(loc.clone().add(0, -1, 0))) {
					gbstate = loc.clone().add(0, -1, 0).getBlock().getState();
					getPlayer().sendBlockChange(gbstate.getLocation(), Material.GLOWSTONE, (byte) 0);
				}
			}
		}
	}

	public void CleanUp(){
		if(glow){
			if (gbstate!=null) {
				gbstate.update(true);
			}
		}

		if(disco){
			stopDisco();
		}
		
		if(rCA){
			stopRandomColorArmor();
			rCA=!rCA;
		}
	}

	public void startAura(){
		auratoggle=true;
		saveMe();
		aura = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

			@Override
			public void run() {
				auraname.setDisplayName(Integer.toString(Util.randomNum(0,1000000)));
				auraitem.setItemMeta(auraname);
				Item item = getPlayer().getWorld().dropItem(getPlayer().getLocation().add(Util.randomNum(-1,1), 0, Util.randomNum(-1,1)), auraitem);
				item.setVelocity(new Vector(0,0.5,0));
				item.setPickupDelay(2000);
				plugin.aura.add(item);
				removeAura(item);
			}

		}, 0, 1L);
	}

	public void removeAura(final Entity item){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				plugin.aura.remove(item);
				item.remove();
			}

		}, 5L);
	}

	public void cancelAura(){
		auratoggle=false;
		saveMe();
		aura.cancel();
	}

	public void setAuraMat(Material mat){
		auramat = mat;
		auraitem.setType(mat);
		auraname.setDisplayName(Integer.toString(Util.randomNum(0,1000000)));
		auraname.setLore(auralore);
		auraitem.setItemMeta(auraname);
		saveMe();
	}

	public Player getPlayer(){
		return plugin.getServer().getPlayer(name);
	}

	public void offLine(){
		ftrail=false;
		getPlayer().setWalkSpeed(0.5F);
		online=false;
		if(auratoggle){
			cancelAura();
		}
		if(rCA){
			this.randomColorArmor.cancel();
			rCA=!rCA;
		}
		if(trail){this.toggleTrail();}
		saveMe();
	}

	public boolean isOnline(){
		return this.online;
	}

	public void toggleTrail(){
		trail=!trail;
		if(trail){
			SendMsg("&6Trails are &aON&6!");
		} else {
			SendMsg("&6Trails are &cOFF!");
		}
		saveMe();
	}

	public boolean isFrozen(){
		return frozen;
	}

	public void setAnimals(List<String> list){
		animals=list;
		saveMe();
	}

	public List<String> getAnimals(){
		return animals;
	}

	public boolean delAnimal(String string){
		if(animals.remove(string)){
			saveMe();
			return true;
		}
		return false;
	}

	public String getName(){
		return name;
	}

	public boolean isMyAnimal(String string){
		if (animals != null) {
			if (animals.contains(string)) {
				return true;
			}
		}
		return false;
	}

	public boolean freezeMe(){
		if (staff){
			return false;
		} else {
			frozen=true;
			saveMe();
			return true;
		}
	}

	public void unFreezeMe(){
		frozen=false;
		saveMe();
	}

	public void lockAnimal(Entity entity){
		if (animals != null) {
			if (!animals.contains(entity.getUniqueId().toString())) {
				animals.add(entity.getUniqueId().toString());
				playerfile.getConfig().set("animals", animals);
				SendMsg("&e" + entity.getType().toString() + " &cLOCKED!");

			} else {
				animals.remove(entity.getUniqueId().toString());
				playerfile.getConfig().set("animals", animals);
				SendMsg("&e" + entity.getType().toString() + " &aUNLOCKED!");
			}
			saveMe();
		}
	}

	@SuppressWarnings("deprecation")
	public void setLoc(Location loc){
		this.loc = loc;
		if(trail){
			//PENIS
			loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0);
		}
		
		if(ftrail){
			try {
				plugin.fplayer.playFirework(getPlayer().getWorld(), loc, SpawnRandomFirework.randomEffect());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(this.glow){
			Glow(loc);
		}

		if(frozen){
			// TODO Change this later if they replace the command....
			getPlayer().sendBlockChange(loc.clone().add(0, 2, 0), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(0, -1, 0), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(1, 0, 0), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(1, 1, 0), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(-1, 0, 0), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(-1, 1, 0), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(0, 0, 1), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(0, 1, 1), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(0, 0, -1), Material.ICE, (byte) 0);
			getPlayer().sendBlockChange(loc.clone().add(0, 1, -1), Material.ICE, (byte) 0);
		}
		ItemStack item = getPlayer().getInventory().getBoots();
		if(item!=null){
			if(item.hasItemMeta()){
				if(item.getItemMeta().hasLore()){
					if(item.getItemMeta().getLore().get(0).equalsIgnoreCase("speed boots")){
						getPlayer().setWalkSpeed(1F);
					} else { 
						getPlayer().setWalkSpeed(0.2F);
					}
				} else { 
					getPlayer().setWalkSpeed(0.2F);
				}
			} else { 
				getPlayer().setWalkSpeed(0.2F);
			}
		} else { 
			getPlayer().setWalkSpeed(0.2F);
		}
	}

	public Location getLoc(){
		return loc;
	}

	public void tpanimal(Location loc){
		if(tplist.isEmpty()){
			SendMsg("&cYour tplist is empty, add animals to it by right clicking with a feather in your hand.  Remove them the same way!");
		} else {
			for(final Entity entity : tplist){
				entity.teleport(loc);
			}
			SendMsg("&6Animals have been teleported!");
			tplist.clear();
			tptimer.cancel();
			newTPTimer();
		}
	}

	public void toggleTP(Entity entity){
		if (!tplist.contains(entity)){
			if(tplist.isEmpty()){
				tptimer.runTaskLater(plugin, 6000L);
			}
			tplist.add(entity);
			SendMsg("&6" + entity.getType().toString() + " added to tplist!");
		} else {
			tplist.remove(entity);
			SendMsg("&6" + entity.getType().toString() + " removed from tplist!");
		}
		if(tplist.isEmpty()){
			tptimer.cancel();
			newTPTimer();
		}
	}

	public void armorRandomColorChange(){
		randomColorArmor = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

			@Override
			public void run() {
				for(ItemStack i : getPlayer().getInventory().getArmorContents()){
					if (i.getType().equals(Material.LEATHER_BOOTS) || i.getType().equals(Material.LEATHER_CHESTPLATE) ||
							i.getType().equals(Material.LEATHER_HELMET) || i.getType().equals(Material.LEATHER_LEGGINGS)) {
						Color[] colors = new Color[] { Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW };
						Random r = new Random();
						List<Color> colorList = Arrays.asList(colors);
						Color randomColor = (Color) colorList.get(r.nextInt(colorList.size()));
						setColor(i, randomColor);
					}
				}
			}
			
		}, 0L, 5L);
	}
	
	public boolean isRandomColorArmor(){
		return rCA;
	}
	
	public void stopRandomColorArmor(){
		if (rCA) {
			randomColorArmor.cancel();
		}
	}
	
	public void openQuiver(){
		getPlayer().openInventory(quiver);
	}
	
	public static ItemStack setColor(ItemStack item, Color color){
        LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
        lam.setColor(color);
        item.setItemMeta(lam);
        return item;
    }
}


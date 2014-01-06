package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
	public String bcolor,fprefix,fchat;
	private BukkitTask discotask,randomColorArmor;
	private List<BlockState> floor = new ArrayList<BlockState>();
	public boolean disco,rCA = false,ftrail=false;
	public int votes=0;
	public float speed;

	public OasisPlayer(OasisExtras plugin, String myname){
		this.plugin = plugin;
		name = myname;

		String filename = "players/" + name + ".yml";
		playerfile = new MyConfigFile(plugin, filename);

		animals = playerfile.getConfig().getStringList("animals");
		frozen=playerfile.getConfig().getBoolean("frozen",frozen);

		friends=playerfile.getConfig().getStringList("friends");
		bcolor=playerfile.getConfig().getString("friendlistbracketcolor","&c");
		fprefix=playerfile.getConfig().getString("friendprefixcolor", "&b");
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

		auralore.add("NO");

		auraname.setLore(auralore);
		auraitem.setItemMeta(auraname);

		saveMe();
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
		playerfile.getConfig().set("friendprefixcolor", fprefix);
		playerfile.getConfig().set("friendschatcolor", fchat);
		playerfile.getConfig().set("joinquitkickignore", joinquitkickignore);
		playerfile.getConfig().set("votes", votes);
		playerfile.saveConfig();
	}

	public void onLine(){
		speed=getPlayer().getWalkSpeed();
		online=true;
		setLoc(getPlayer().getLocation());
		staff = plugin.getServer().getPlayer(name).hasPermission("oasischat.staff.staff") ? true : false;

		if(frozen){SendMsg("&6Your &bFROZEN&g!");}
		if(glow){SendMsg("&eGlowing &aEnabled");}
		if(auratoggle){
			SendMsg("&bAura is on!");
		}
		if(trail){SendMsg("&cTrail is &aEnabled!");}
		if(weatherman){SendMsg("&bWeather Channel is &aEnabled!");}
	}

	public void Disco(){
		floor.addAll(region(loc.clone().add(5, -1, 5),loc.clone().add(-5, -1, -5)));
		if(floor==null){plugin.getServer().broadcastMessage("floor is null");}
		discotask = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				for (int i =0 ; i<10 ; i++) {
					getPlayer().sendBlockChange(floor.get(randomNum(0, floor.size() - 1)).getLocation(), Material.STAINED_GLASS, (byte) randomNum(0, 15));
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

	public void setMedic(){
		if(!medic){
			medic=!medic;
			SendMsg("&cMedic&r is now &aENABLED!");
			plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

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
	}

	public void delFriend(String name){
		if (friends.contains(name)) {
			friends.remove(name);
		}
		SendMsg(fchat + name + " remvoed from friends list!");
	}

	public void listFriends(){
		SendMsg(fchat + friends);
	}

	public void setFCHAT(String string){
		this.fchat = string;
	}

	public void setFPREFIX(String string){
		this.fprefix = string;
	}

	public void setBCOLOR(String string){
		this.bcolor = string;
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
			if (!isFarm(loc.clone().add(0, -1, 0))) {
				if (isBlock(loc.clone().add(0, -1, 0))) {
					gbstate = loc.clone().add(0, -1, 0).getBlock().getState();
					getPlayer().sendBlockChange(gbstate.getLocation(), Material.GLOWSTONE, (byte) 0);
				}
			}
		}
	}

	public void CleanUp(){
		if(glow){
			gbstate.update(true);
		}

		if(disco){
			stopDisco();
		}
		
		if(rCA){
			stopRandomColorArmor();
			rCA=!rCA;
		}
	}

	public boolean isFarm(Location loc){
		Material mat = loc.getBlock().getType();
		if(mat.equals(Material.CROPS)){
			return true;
		}

		if(mat.equals(Material.MELON_STEM)){
			return true;
		}

		if(mat.equals(Material.PUMPKIN_STEM)){
			return true;
		}

		return false;
	}

	public boolean isBlock(Location loc){
		Material mat = loc.getBlock().getType();
		if(mat.equals(Material.STONE)){
			return true;
		}

		if(mat.equals(Material.COBBLESTONE)){
			return true;
		}

		if(mat.equals(Material.DIRT)){
			return true;
		}

		if(mat.equals(Material.SAND)){
			return true;
		}

		if(mat.equals(Material.MOSSY_COBBLESTONE)){
			return true;
		}

		if(mat.equals(Material.MONSTER_EGG)){
			return true;
		}

		if(mat.equals(Material.GRAVEL)){
			return true;
		}
		return false;

	}

	public void startAura(){
		auratoggle=true;
		saveMe();
		aura = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

			@Override
			public void run() {
				auraname.setDisplayName(Integer.toString(randomNum(0,1000000)));
				auraitem.setItemMeta(auraname);
				Item item = getPlayer().getWorld().dropItem(getPlayer().getLocation().add(randomNum(-1,1), 0, randomNum(-1,1)), auraitem);
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
		auraname.setDisplayName(Integer.toString(randomNum(0,1000000)));
		auraname.setLore(auralore);
		auraitem.setItemMeta(auraname);
		saveMe();
	}

	public Player getPlayer(){
		return plugin.getServer().getPlayer(name);
	}

	public void offLine(){
		ftrail=false;
		getPlayer().setWalkSpeed(speed);
		online=false;
		if(auratoggle){
			cancelAura();
		}
		if(trail){this.toggleTrail();}
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

	private int randomNum(Integer lownum, double d) {
		return lownum + (int)(Math.random() * ((d - lownum) + 1));
	}

	public boolean isFrozen(){
		return frozen;
	}

	public void setAnimals(List<String> list){
		animals=list;
		playerfile.getConfig().set("animals", animals);
		playerfile.saveConfig();
	}

	public List<String> getAnimals(){
		return animals;
	}

	public boolean delAnimal(String string){
		return animals.remove(string);
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
			playerfile.saveConfig();
			return true;
		}
	}

	public void unFreezeMe(){
		frozen=false;
		saveMe();
		playerfile.saveConfig();
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
			playerfile.saveConfig();
		}
	}

	@SuppressWarnings("deprecation")
	public void setLoc(Location loc){
		this.loc = loc;
		if(trail){
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
						getPlayer().setWalkSpeed(speed);
					}
				} else { 
					getPlayer().setWalkSpeed(speed);
				}
			} else { 
				getPlayer().setWalkSpeed(speed);
			}
		} else { 
			getPlayer().setWalkSpeed(speed);
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
		}
	}

	public void toggleTP(Entity entity){
		if (!tplist.contains(entity)){
			tplist.add(entity);
			SendMsg("&6" + entity.getClass().getSimpleName() + " added to tplist!");
		} else {
			tplist.remove(entity);
			SendMsg("&6" + entity.getClass().getSimpleName() + " removed from tplist!");
		}
	}

	public String getAnimalClass(Entity entity){
		return entity.getClass().getName().toString();
	}

	public Entity getEntity(String uid){
		for (Entity entity : plugin.getServer().getPlayer(name).getWorld().getEntities()){
			if (entity.getUniqueId().toString().equals(uid)){
				return entity;
			}
		}
		return null;
	}

	public Location behindPlayer(Player player) {
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		Block b = player.getLocation().clone().subtract(0, 1, 0).getBlock();
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) {
			return b.getRelative(BlockFace.SOUTH).getLocation();
		} else if (22.5 <= rotation && rotation < 67.5) {
			return b.getRelative(BlockFace.SOUTH_WEST).getLocation();
		} else if (67.5 <= rotation && rotation < 112.5) {
			return b.getRelative(BlockFace.WEST).getLocation();
		} else if (112.5 <= rotation && rotation < 157.5) {
			return b.getRelative(BlockFace.NORTH_WEST).getLocation();
		} else if (157.5 <= rotation && rotation < 202.5) {
			return b.getRelative(BlockFace.NORTH).getLocation();
		} else if (202.5 <= rotation && rotation < 247.5) {
			return b.getRelative(BlockFace.NORTH_EAST).getLocation();
		} else if (247.5 <= rotation && rotation < 292.5) {
			return b.getRelative(BlockFace.EAST).getLocation();
		} else if (292.5 <= rotation && rotation < 337.5) {
			return b.getRelative(BlockFace.SOUTH_EAST).getLocation() ;
		} else if (337.5 <= rotation && rotation < 360.0) {
			return b.getRelative(BlockFace.SOUTH).getLocation();
		} else {
			return null;
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
	
	public static ItemStack setColor(ItemStack item, Color color){
        LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
        lam.setColor(color);
        item.setItemMeta(lam);
        return item;
    }
}


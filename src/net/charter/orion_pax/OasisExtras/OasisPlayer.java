package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class OasisPlayer {

	private final OasisExtras plugin;

	private final String name;
	private World world;
	private List<String> animals = new ArrayList<String>();
	private boolean frozen = false;
	private boolean online = false;
	private boolean glow = false;
	private boolean eventnotify = true;
	private Location gbloc;
	private Material gbmat;
	private Location loc = null;
	private boolean staff = false;
	public List<Entity> tplist = new ArrayList<Entity>();
	private MyConfigFile playerfile;
	private BukkitTask aura;
	private Material auramat = Material.GHAST_TEAR;
	private ItemStack auraitem;
	private ItemMeta auraname;
	private List<String> auralore = new ArrayList<String>();
	private boolean trail = false;
	private boolean auratoggle = false;
	private boolean weatherman = false;
	private boolean jesus = false;
	private HashMap<Location,Material> jesusblocks = new HashMap<Location,Material>();
	private Location lastblock;

	public OasisPlayer(OasisExtras plugin, String myname){
		this.plugin = plugin;
		name = myname;

		String filename = "players/" + name + ".yml";
		playerfile = new MyConfigFile(plugin, filename);

		frozen=playerfile.getConfig().getBoolean("frozen",frozen);

		auramat = Material.getMaterial(playerfile.getConfig().getString("auramat",Material.GHAST_TEAR.toString()));
		auraitem = new ItemStack(auramat,1);
		auraname = auraitem.getItemMeta();
		eventnotify = playerfile.getConfig().getBoolean("eventnotify", true);
		if(!eventnotify){
			plugin.getServer().dispatchCommand(plugin.console, "manuaddp " + name + "-oasisextras.player.event.notify");
		}

		jesus = playerfile.getConfig().getBoolean("jesus",false);

		glow = playerfile.getConfig().getBoolean("glowing",false);

		trail = playerfile.getConfig().getBoolean("trail", false);
		if(trail){startTrail();}

		auratoggle = playerfile.getConfig().getBoolean("aura", false);

		weatherman = playerfile.getConfig().getBoolean("weatherman", false);

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
		playerfile.getConfig().set("jesus", jesus);
		playerfile.saveConfig();
	}

	public void onLine(){
		online=true;
		staff = plugin.getServer().getPlayer(name).hasPermission("oasischat.staff.staff") ? true : false;

		if(frozen){SendMsg("&6Your &bFROZEN&g!");}
		if(glow){SendMsg("&eGlowing &aEnabled");}
		if(auratoggle){SendMsg("&bAura is on!");}
		if(trail){SendMsg("&cTrail is &aEnabled!");}
		if(weatherman){SendMsg("&bWeather Channel is &aEnabled!");}
		if(jesus){SendMsg("&1Waterwalker is &aEnabled!");}
	}

	public boolean isJesus(){
		return jesus;
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
			gbloc.getBlock().setType(gbmat);
			SendMsg("&6Glow &cDISABLED&6!");
		}
		saveMe();
	}

	public boolean isGlowing(){
		return glow;
	}

	public void Glow(Location loc){
		if(gbloc!=null){
			gbloc.getBlock().setType(gbmat);
		}
		if (loc.getBlockY()<60) {
			Location newloc = loc.add(0, -1, 0);
			if (!isFarm(loc)) {
				if (isBlock(newloc)) {
					gbloc = newloc;
					gbmat = gbloc.getBlock().getType();
					gbloc.getBlock().setType(Material.GLOWSTONE);
				}
			}
		}
	}

	public void CleanUp(){
		if(gbloc!=null){
			gbloc.getBlock().setType(gbmat);
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

	public void startTrail(){
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
				removeTrail(item);
			}

		}, 0, 1L);
	}

	public void removeTrail(final Entity item){
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				plugin.aura.remove(item);
				item.remove();
			}

		}, 5L);
	}

	public void cancelTrail(){
		auratoggle=false;
		saveMe();
		aura.cancel();
	}

	public void setTrailMat(Material mat){
		auramat = mat;
		auraitem.setType(mat);
		auraname.setDisplayName(Integer.toString(randomNum(0,1000000)));
		auraname.setLore(auralore);
		auraitem.setItemMeta(auraname);
		saveMe();
	}

	public void toggleJ(){
		jesus=!jesus;
		if(jesus){
			SendMsg("&1Water walker &aEnabled!");
		} else {
			SendMsg("&1Water walker &cDisabled!");
		}
	}

	public Player getPlayer(){
		return plugin.getServer().getPlayer(name);
	}

	public void offLine(){
		online=false;
		cancelTrail();
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
				plugin.getServer().getPlayer(name).sendMessage(ChatColor.YELLOW + entity.getClass().getSimpleName() + ChatColor.RED + "LOCKED!");

			} else {
				animals.remove(entity.getUniqueId().toString());
				playerfile.getConfig().set("animals", animals);
				plugin.getServer().getPlayer(name).sendMessage(ChatColor.YELLOW + entity.getClass().getSimpleName() + ChatColor.GREEN + "UNLOCKED!");

			}
			saveMe();
			playerfile.saveConfig();
		}
	}

	public void setLoc(Location loc){
		this.loc = loc;
		if(trail){
			loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0);
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
}

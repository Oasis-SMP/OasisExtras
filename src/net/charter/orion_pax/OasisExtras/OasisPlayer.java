package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

public class OasisPlayer {

	private final OasisExtras plugin;

	private final String name;
	private World world;
	private List<String> animals = new ArrayList<String>();
	private boolean frozen = false;
	private Location loc = null;
	private boolean staff = false;
	private List<String> alockperm = new ArrayList<String>();
	public List<Entity> tplist = new ArrayList<Entity>();
	private MyConfigFile playerfile;

	public OasisPlayer(OasisExtras plugin, String myname){
		this.plugin = plugin;
		name = myname;
		staff = plugin.getServer().getPlayer(name).hasPermission("oasischat.staff.staff") ? true : false;
		
		String filename = "players/" + name + ".yml";
		playerfile = new MyConfigFile(plugin, filename);
		
		if (!playerfile.exist()){
			playerfile.getConfig().createSection("frozenlocation");
			playerfile.getConfig().set("frozen", frozen);
			playerfile.saveConfig();
		} else {

			frozen=playerfile.getConfig().getBoolean("frozen");

			int x = 0,y = 0,z = 0;
			World world = null;
			
			if (playerfile.getConfig().contains("frozenlocation.world")) {
				world = plugin.getServer().getWorld(playerfile.getConfig().getString("frozenlocation.world"));
				z = playerfile.getConfig().getInt("frozenlocation.z");
				y = playerfile.getConfig().getInt("frozenlocation.y");
				x = playerfile.getConfig().getInt("frozenlocation.x");
			}

			if (world!=null) {
				loc = new Location(world, x, y, z);
			}
			
			if (playerfile.getConfig().contains("alockperm")){
				alockperm= playerfile.getConfig().getStringList("alockperm");
			}
			playerfile.saveConfig();
		}

	}
	
	public Player getPlayer(){
		return plugin.getServer().getPlayer(name);
	}

	public void saveMe(){
		if (loc!=null){
			playerfile.getConfig().set("frozenlocation.x", loc.getBlockX());
			playerfile.getConfig().set("frozenlocation.y", loc.getBlockY());
			playerfile.getConfig().set("frozenlocation.z", loc.getBlockZ());
			playerfile.getConfig().set("frozenlocation.world", loc.getWorld().toString());
			playerfile.getConfig().set("alockperm",alockperm);
			playerfile.getConfig().set("animals",animals);
		}
		playerfile.saveConfig();
	}

	public void setPerms(List<String> list){
		alockperm = list;
		playerfile.getConfig().set("alockperm", alockperm);
		playerfile.saveConfig();
	}

	public List<String> listPerms(){
		return alockperm;
	}

	public void addPerm(String name){
		if (alockperm != null) {
			if (!alockperm.contains(name)) {
				alockperm.add(name);
				playerfile.getConfig().set("alockperm", alockperm);
				playerfile.saveConfig();
			}
		}
	}

	public void delPerm(String name){
		if (alockperm != null) {
			if (alockperm.contains(name)) {
				alockperm.remove(name);
				playerfile.getConfig().set("alockperm", alockperm);
				playerfile.saveConfig();
			}
		}
	}

	public boolean hasPerm(String name){
		if (alockperm != null) {
			if (alockperm.contains(name)) {
				return true;
			}
		}
		return false;
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
	
	public void sendMessage(String msg){
		plugin.getServer().getPlayer(name).sendMessage(msg);
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
			loc = plugin.getServer().getPlayer(name).getLocation();
			playerfile.getConfig().set("frozenlocation.x", loc.getBlockX());
			playerfile.getConfig().set("frozenlocation.y", loc.getBlockY());
			playerfile.getConfig().set("frozenlocation.z", loc.getBlockZ());
			playerfile.getConfig().set("frozenlocation.world", loc.getWorld().toString());
			playerfile.saveConfig();
			return true;
		}
	}

	public void unFreezeMe(){
		frozen=false;
		loc = null;
		playerfile.getConfig().set("frozenlocation", null);
		playerfile.saveConfig();
	}

	public void lockAnimal(Entity entity){
		if (animals != null) {
			if (!animals.contains(entity.getUniqueId().toString())) {
				animals.add(entity.getUniqueId().toString());
				playerfile.getConfig().set("animals", animals);
				plugin.getServer().getPlayer(name).sendMessage(ChatColor.RED + "LOCKED!");
				
			} else {
				animals.remove(entity.getUniqueId().toString());
				playerfile.getConfig().set("animals", animals);
				plugin.getServer().getPlayer(name).sendMessage(ChatColor.RED + "UNLOCKED!");
				
			}
			playerfile.saveConfig();
		}
	}

	public Location getLoc(){
		return loc;
	}

	public void tpanimal(Location loc){
		if(tplist.isEmpty()){
			this.sendMessage(ChatColor.RED + "Your tplist is empty, add animals to it by right clicking with a feather in your hand.  Remove them the same way!");
		} else {
			for(final Entity entity : tplist){
				entity.teleport(loc);
			}
			sendMessage(ChatColor.GOLD + "Animals have been teleported!");
			tplist.clear();
		}
	}
	
	public void toggleTP(Entity entity){
		if (!tplist.contains(entity)){
			tplist.add(entity);
			this.sendMessage(ChatColor.RED + "Added to tplist!");
		} else {
			tplist.remove(entity);
			this.sendMessage(ChatColor.RED + "Removed from tplist!");
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

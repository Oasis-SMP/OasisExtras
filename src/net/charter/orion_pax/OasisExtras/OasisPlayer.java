package net.charter.orion_pax.OasisExtras;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

public class OasisPlayer {

	private final OasisExtras plugin;

	private final String name;
	private List<String> animals = new ArrayList<String>();
	private boolean frozen = false;
	private Location loc = null;
	private boolean staff = false;
	private List<String> alockperm = new ArrayList<String>();
	private MyConfigFile oasisplayer;
	private boolean tag = false;

	public OasisPlayer(OasisExtras plugin, String myname){
		name = myname;
		this.plugin = plugin;
		if (plugin.getServer().getPlayer(name).hasPermission("oasischat.staff.a")){
			staff = true;
		} else {
			staff = false;
		}
		oasisplayer = new MyConfigFile(plugin, name + ".yml");

		if (!oasisplayer.exist()){
			oasisplayer.getConfig().createSection("frozenlocation");
			oasisplayer.getConfig().set("frozen", frozen);
		} else {
			if (oasisplayer.getConfig().contains("animals")){
				animals= oasisplayer.getConfig().getStringList("animals");
			}
			frozen=oasisplayer.getConfig().getBoolean("frozen");

			int x = 0,y = 0,z = 0;
			World world = null;
			
			if (oasisplayer.getConfig().contains("frozenlocation.world")) {
				world = plugin.getServer().getWorld(oasisplayer.getConfig().getString("frozenlocation.world"));
				z = oasisplayer.getConfig().getInt("frozenlocation.z");
				y = oasisplayer.getConfig().getInt("frozenlocation.y");
				x = oasisplayer.getConfig().getInt("frozenlocation.x");
			}

			if (world!=null) {
				loc = new Location(world, x, y, z);
			}
			
			if (oasisplayer.getConfig().contains("alockperm")){
				alockperm= oasisplayer.getConfig().getStringList("alockperm");
			}
		}

	}

	public void saveMe(){
		if (loc!=null){
			oasisplayer.getConfig().set("frozenlocation.x", loc.getBlockX());
			oasisplayer.getConfig().set("frozenlocation.y", loc.getBlockY());
			oasisplayer.getConfig().set("frozenlocation.z", loc.getBlockZ());
			oasisplayer.getConfig().set("frozenlocation.world", loc.getWorld().toString());
		}
		oasisplayer.saveConfig();
	}
	
	public boolean isTagging(){
		return tag;
	}
	
	public void setTagging(boolean tagme){
		tag=tagme;
	}

	public void setPerms(List<String> list){
		alockperm = list;
		oasisplayer.getConfig().set("alockperm", alockperm);
	}

	public List<String> listPerms(){
		return alockperm;
	}

	public void addPerm(String name){
		if (alockperm != null) {
			if (!alockperm.contains(name)) {
				alockperm.add(name);
				oasisplayer.getConfig().set("alockperm", alockperm);
			}
		}
	}

	public void delPerm(String name){
		if (alockperm != null) {
			if (alockperm.contains(name)) {
				alockperm.remove(name);
				oasisplayer.getConfig().set("alockperm", alockperm);
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
		oasisplayer.getConfig().set("animals", animals);
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
			oasisplayer.getConfig().set("frozenlocation.x", loc.getBlockX());
			oasisplayer.getConfig().set("frozenlocation.y", loc.getBlockY());
			oasisplayer.getConfig().set("frozenlocation.z", loc.getBlockZ());
			oasisplayer.getConfig().set("frozenlocation.world", loc.getWorld().toString());
			return true;
		}
	}

	public void unFreezeMe(){
		frozen=false;
		loc = null;
		oasisplayer.getConfig().set("frozenlocation", null);
	}

	public void lockAnimal(String animal){
		if (animals != null) {
			if (!animals.contains(animal)) {
				animals.add(animal);
				oasisplayer.getConfig().set("animals", animals);
				Entity entity = getEntity(animal);
				LivingEntity living = (LivingEntity) entity;
				living.setCustomName(name + "'s " + living.getType().toString());
				plugin.getServer().getPlayer(name).sendMessage(ChatColor.RED + "LOCKED!");
			} else {
				animals.remove(animal);
				oasisplayer.getConfig().set("animals", animals);
				Entity entity = getEntity(animal);
				LivingEntity living = (LivingEntity) entity;
				living.setCustomName(null);
				plugin.getServer().getPlayer(name).sendMessage(ChatColor.RED + "UNLOCKED!");
			}
		}
	}

	public Location getLoc(){
		return loc;
	}

	public void tpanimal(){

	}

	private Entity getEntity(String uid){
		List<Entity> entities = plugin.getServer().getPlayer(name).getWorld().getEntities();
		for (Entity entity : entities){
			if (entity.getUniqueId().toString().equals(uid)){
				return entity;
			}
		}
		return null;
	}
}

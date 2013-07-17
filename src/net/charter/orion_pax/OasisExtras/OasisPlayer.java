package net.charter.orion_pax.OasisExtras;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;

public class OasisPlayer {

	private OasisExtras plugin;

	private String name;
	private Set<UUID> animals;
	private boolean frozen = false;
	private Location loc = null;
	private boolean staff = false;
	private Set<String> alockperm;
	private MyConfigFile oasisplayer;
	private boolean tag = false;

	public OasisPlayer(OasisExtras plugin, String name){
		this.name = name;
		this.plugin = plugin;
		if (plugin.getServer().getPlayer(name).hasPermission("oasischat.staff.a")){
			this.staff = true;
		} else {
			this.staff = false;
		}
		oasisplayer = new MyConfigFile(plugin, name + ".yml");

		if (!oasisplayer.exist()){
			oasisplayer.getConfig().createSection("animals");
			oasisplayer.getConfig().createSection("frozenlocation");
			oasisplayer.getConfig().createSection("alockperm");
			oasisplayer.getConfig().set("frozen", frozen);
		} else {
			this.animals=(Set<UUID>) oasisplayer.getConfig().getList("animals");

			this.frozen=oasisplayer.getConfig().getBoolean("frozen");

			int x = 0,y = 0,z = 0;
			World world = null;
			
			if (oasisplayer.getConfig().contains("frozenlocation.world")) {
				world = plugin.getServer().getWorld(oasisplayer.getConfig().getString("frozenlocation.world"));
				z = oasisplayer.getConfig().getInt("frozenlocation.z");
				y = oasisplayer.getConfig().getInt("frozenlocation.y");
				x = oasisplayer.getConfig().getInt("frozenlocation.x");
			}

			if (world!=null) {
				this.loc = new Location(world, x, y, z);
			}

			this.alockperm= (Set<String>) oasisplayer.getConfig().getList("alockperm");
		}

	}

	public void saveConfig(){
		if (this.loc!=null){
			oasisplayer.getConfig().set("frozenlocation.x", this.loc.getBlockX());
			oasisplayer.getConfig().set("frozenlocation.y", this.loc.getBlockY());
			oasisplayer.getConfig().set("frozenlocation.z", this.loc.getBlockZ());
			oasisplayer.getConfig().set("frozenlocation.world", this.loc.getWorld().toString());
		}
		oasisplayer.saveConfig();
	}
	
	public boolean isTagging(){
		return this.tag;
	}
	
	public void setTagging(boolean tag){
		this.tag=tag;
	}

	public void setPerms(Set<String> list){
		this.alockperm = list;
	}

	public Set<String> listPerms(){
		return this.alockperm;
	}

	public void addPerm(String name){
		if (!this.alockperm.contains(name)) {
			this.alockperm.add(name);
		}
	}

	public void delPerm(String name){
		if (this.alockperm.contains(name)){
			this.alockperm.remove(name);
		}
	}

	public boolean hasPerm(String name){
		if (this.alockperm.contains(name)){
			return true;
		} else {
			return false;
		}
	}

	public boolean isFrozen(){
		return this.frozen;
	}

	public void setAnimals(Set<UUID> list){
		this.animals=list;
	}

	public boolean isMyAnimal(UUID animal){
		if (!this.animals.isEmpty()) {
			if (this.animals.contains(animal)) {
				return true;
			}
		}
		return false;
	}

	public boolean freezeMe(){
		if (this.staff){
			return false;
		} else {
			this.frozen=true;
			this.loc = plugin.getServer().getPlayer(name).getLocation();
			return true;
		}
	}

	public void unFreezeMe(){
		this.frozen=false;
		this.loc = null;
		oasisplayer.getConfig().set("frozenlocation", null);
	}

	public void lockAnimal(UUID animal){
		if (!this.animals.contains(animal)) {
			this.animals.add(animal);
		}
	}

	public void unlockAnimal(UUID animal){
		if (!this.animals.isEmpty()) {
			if (this.animals.contains(animal)) {
				this.animals.remove(animal);
			}
		}
	}

	public Location getLoc(){
		return this.loc;
	}

	public void tpanimal(){

	}

	private Entity getEntity(UUID id){
		List<Entity> entities = plugin.getServer().getPlayer(name).getWorld().getEntities();
		for (Entity entity : entities){
			if (entity.getUniqueId().equals(id)){
				return entity;
			}
		}
		return null;
	}
}

package net.charter.orion_pax.OasisExtras;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.Bukkit;
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
	private MyConfigFile playerfile;
	private boolean tag = false;
	private boolean overriding = false;
	private Team team;
	private ScoreboardManager manager;
	private Scoreboard board;
	private Score horse,cow,chicken,pig,sheep,villager,ocelot,wolf;
	private Objective objective;

	public OasisPlayer(OasisExtras plugin, String myname){
		name = myname;
		this.plugin = plugin;
		if (plugin.getServer().getPlayer(name).hasPermission("oasischat.staff.a")){
			staff = true;
		} else {
			staff = false;
		}
		String filename = "players/" + name + ".yml";
		playerfile = new MyConfigFile(plugin, filename);
		
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		team = board.registerNewTeam(name);
		objective = board.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		horse = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Horses:"));
		cow = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Cows:"));
		pig = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Pigs:"));
		sheep = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Sheep:"));
		villager = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Villagers:"));
		ocelot = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Ocelots:"));
		wolf = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Wolves:"));
		
		if (!playerfile.exist()){
			playerfile.getConfig().createSection("frozenlocation");
			playerfile.getConfig().set("frozen", frozen);
			playerfile.saveConfig();
		} else {
			if (playerfile.getConfig().contains("animals")){
				animals= playerfile.getConfig().getStringList("animals");
			}
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
	
	public void toggleOverride(){
		if(overriding){
			overriding=false;
			this.sendMessage(ChatColor.GOLD + "Alock Override is disabled!");
		} else {
			overriding=true;
			this.sendMessage(ChatColor.GOLD + "Alock Override is enabled!");
		}
	}
	
	public boolean isOverriding(){
		return overriding;
	}
	
	public boolean isTagging(){
		return tag;
	}
	
	public void setTagging(boolean tagme){
		tag=tagme;
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

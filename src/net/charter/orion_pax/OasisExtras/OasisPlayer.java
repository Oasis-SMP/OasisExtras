package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.List;
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
	private World world;
	private List<String> animals = new ArrayList<String>();
	private boolean frozen = false;
	private Location loc = null;
	private boolean staff = false;
	private List<String> alockperm = new ArrayList<String>();
	public List<Entity> tplist = new ArrayList<Entity>();
	private MyConfigFile playerfile;
	private Team team;
	private ScoreboardManager manager;
	private Scoreboard board;
	private Score horse,cow,chicken,pig,sheep,villager,ocelot,wolf;
	private Objective objective;
	private int horsecount=0,cowcount=0,pigcount=0,sheepcount=0,villagercount=0,ocelotcount=0,wolfcount=0,chickencount=0;

	public OasisPlayer(OasisExtras plugin, String myname){
		this.plugin = plugin;
		name = myname;
		staff = plugin.getServer().getPlayer(name).hasPermission("oasischat.staff.staff") ? true : false;
		
		String filename = "players/" + name + ".yml";
		playerfile = new MyConfigFile(plugin, filename);
		
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		team = board.registerNewTeam(name);
		objective = board.registerNewObjective("Stable", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		horse = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Horses:"));
		cow = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Cows:"));
		pig = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Pigs:"));
		sheep = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Sheep:"));
		villager = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Villagers:"));
		ocelot = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Ocelots:"));
		wolf = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Wolves:"));
		chicken = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Chickens:"));
		
		team.addPlayer(plugin.getServer().getOfflinePlayer(name));
		plugin.getServer().getPlayer(name).setScoreboard(board);
		
		if (!playerfile.exist()){
			playerfile.getConfig().createSection("frozenlocation");
			playerfile.getConfig().set("frozen", frozen);
			playerfile.saveConfig();
		} else {
			if (playerfile.getConfig().contains("animals")){
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
					@Override
					public void run(){
						animals= playerfile.getConfig().getStringList("animals");
						
						for(String animal:animals){
							Entity entity = getEntity(animal);
							if(getAnimalClass(entity).contains("Horse")){
								horsecount++;
								horse.setScore(horsecount);
							}
							
							if(getAnimalClass(entity).contains("Cow")){
								cowcount++;
								cow.setScore(cowcount);
							}
							
							if(getAnimalClass(entity).contains("Pig")){
								pigcount++;
								pig.setScore(pigcount);
							}
							
							if(getAnimalClass(entity).contains("Sheep")){
								sheepcount++;
								sheep.setScore(sheepcount);
							}
							
							if(getAnimalClass(entity).contains("Villager")){
								villagercount++;
								villager.setScore(villagercount);
							}
							
							if(getAnimalClass(entity).contains("Ocelot")){
								ocelotcount++;
								ocelot.setScore(ocelotcount);
							}
							
							if(getAnimalClass(entity).contains("Wolf")){
								wolfcount++;
								wolf.setScore(wolfcount);
							}
							
							if(getAnimalClass(entity).contains("Chicken")){
								chickencount++;
								chicken.setScore(chickencount);
							}
						}
					}
				}, 200L);
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
				
				if(getAnimalClass(entity).contains("Horse")){
					horsecount++;
					horse.setScore(horsecount);
				}
				
				if(getAnimalClass(entity).contains("Cow")){
					cowcount++;
					cow.setScore(cowcount);
				}
				
				if(getAnimalClass(entity).contains("Pig")){
					pigcount++;
					pig.setScore(pigcount);
				}
				
				if(getAnimalClass(entity).contains("Sheep")){
					sheepcount++;
					sheep.setScore(sheepcount);
				}
				
				if(getAnimalClass(entity).contains("Villager")){
					villagercount++;
					villager.setScore(villagercount);
				}
				
				if(getAnimalClass(entity).contains("Ocelot")){
					ocelotcount++;
					ocelot.setScore(ocelotcount);
				}
				
				if(getAnimalClass(entity).contains("Wolf")){
					wolfcount++;
					wolf.setScore(wolfcount);
				}
				
				if(getAnimalClass(entity).contains("Chicken")){
					chickencount++;
					chicken.setScore(chickencount);
				}
				
			} else {
				animals.remove(entity.getUniqueId().toString());
				playerfile.getConfig().set("animals", animals);
				plugin.getServer().getPlayer(name).sendMessage(ChatColor.RED + "UNLOCKED!");
				
				if(getAnimalClass(entity).contains("Horse")){
					horsecount--;
					horse.setScore(horsecount);
				}
				
				if(getAnimalClass(entity).contains("Cow")){
					cowcount--;
					cow.setScore(cowcount);
				}
				
				if(getAnimalClass(entity).contains("Pig")){
					pigcount--;
					pig.setScore(pigcount);
				}
				
				if(getAnimalClass(entity).contains("Sheep")){
					sheepcount--;
					sheep.setScore(sheepcount);
				}
				
				if(getAnimalClass(entity).contains("Villager")){
					villagercount--;
					villager.setScore(villagercount);
				}
				
				if(getAnimalClass(entity).contains("Ocelot")){
					ocelotcount--;
					ocelot.setScore(ocelotcount);
				}
				
				if(getAnimalClass(entity).contains("Wolf")){
					wolfcount--;
					wolf.setScore(wolfcount);
				}
				
				if(getAnimalClass(entity).contains("Chicken")){
					chickencount--;
					chicken.setScore(chickencount);
				}
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
				//CommonEntity<?> newentity = CommonEntity.get(entity);
				//newentity.getLocation().getChunk().load();
				//newentity.teleport(loc);
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

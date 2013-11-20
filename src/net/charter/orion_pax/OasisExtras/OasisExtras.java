package net.charter.orion_pax.OasisExtras;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.charter.orion_pax.OasisExtras.Commands.*;

public class OasisExtras extends JavaPlugin{

	public ConsoleCommandSender console;
	public HashMap<Chunk,Horse> horsetp = new HashMap<Chunk,Horse>();
	public HashMap<String,Long> hfcooldowns = new HashMap<String,Long>();
	public HashMap<String, String> highfive = new HashMap<String, String>();
	public HashMap<String, OasisPlayer> oasisplayer = new HashMap<String, OasisPlayer>();
	public HashMap<Location, Runnable> appletree = new HashMap<Location, Runnable>();
	public HashMap<String, OasisPlayer> tptimer = new HashMap<String, OasisPlayer>();
	String effectslist,newbiejoin;
	List<Integer> newbiekit;
	public int default_min, default_max;
	public int ndt;
	int bcastcount, treecount=0;
	long bcasttimer;
	public List<String> bcastmsgs;
	public MyConfigFile appletreefile, signprotectfile;
	
	public String[] oasisextrassub = {
			ChatColor.GOLD + "Usage: /oasisextras subcommand subcommand"
			,ChatColor.GOLD + "SubCommands:"
			,ChatColor.GOLD + "RELOAD - Reloads config"
			,ChatColor.GOLD + "BCAST LIST/ADD/REMOVE"
			,ChatColor.GOLD + "Do /oasisextras [subcommand] for more info"
	};

	String[] oasisextrassub2 = {
			ChatColor.GOLD + "Usage as follows...."
			,ChatColor.GOLD + "/oasisextras BCAST LIST - List auto bcast msgs"
			,ChatColor.GOLD + "/oasisextras BCAST ADD - Adds a msg to the auto bcast list"
			,ChatColor.GOLD + "/oasisextras BCAST REMOVE - Removes a msg from the auto bcast list"
	};

	public OasisExtrasTask task = new OasisExtrasTask(this);

	@Override
	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(new OasisExtrasListener(this), this);
		getCommand("enableme").setExecutor(new EnableMeCommand(this));
		getCommand("disableme").setExecutor(new DisableMeCommand(this));
		getCommand("brocast").setExecutor(new BroCastCommand(this));
		getCommand("spook").setExecutor(new SpookCommand(this));
		getCommand("freeze").setExecutor(new FreezeCommand(this));
		getCommand("drunk").setExecutor(new DrunkCommand(this));
		getCommand("slap").setExecutor(new SlapCommand(this));
		getCommand("random").setExecutor(new RandomCommand(this));
		getCommand("oasisextras").setExecutor(new OECommand(this));
		getCommand("mount").setExecutor(new MountCommand(this));
		getCommand("unmount").setExecutor(new UnMountCommand(this));
		getCommand("chant").setExecutor(new ChantCommand(this));
		getCommand("thunderstruck").setExecutor(new ThunderStruckCommand(this));
		getCommand("findme").setExecutor(new FindMeCommand(this));
		getCommand("kcast").setExecutor(new KCastCommand(this));
		getCommand("highfive").setExecutor(new HighFiveCommand(this));
		getCommand("blackmarket").setExecutor(new BlackMarketCommand(this));
		appletreefile = new MyConfigFile(this,"appletree.yml");
		signprotectfile = new MyConfigFile(this,"signprotect.yml");
		setup();
		console = Bukkit.getServer().getConsoleSender();
		getLogger().info("OasisExtras has been enabled!");
	}

	@Override
	public void onDisable(){
		task.bcasttask.cancel();
		this.saveConfig();
		this.appletreefile.saveConfig();
		this.signprotectfile.saveConfig();
		for(Player player : this.getServer().getOnlinePlayers()){
			OasisPlayer myplayer = this.oasisplayer.get(player.getName());
			myplayer.saveMe();
			this.getLogger().info(myplayer.getName() + " is saved!");
		}
		getLogger().info("OasisExtras has been disabled!");
	}

	public void setup(){
		newbiekit = getConfig().getIntegerList("newbiekit");
		newbiejoin = getConfig().getString("newplayermsg");
		bcastmsgs = getConfig().getStringList("broadcastmessages");
		bcasttimer = getConfig().getInt("broadcasttimer")*1200;
		default_min = Integer.parseInt(getConfig().getString("min_default_location"));
		default_max = Integer.parseInt(getConfig().getString("max_default_location"));
		ndt = Integer.parseInt(getConfig().getString("default_invulnerability_ticks"));
		bcastcount = 0;
		task.bcasttask.runTaskTimer(this, randomNum(0, 18000), bcasttimer);
		if (!appletreefile.getConfig().contains("appletrees")){
			appletreefile.getConfig().createSection("appletrees");
		}
		if (!signprotectfile.getConfig().contains("signs")){
			signprotectfile.getConfig().createSection("signs");
		}
		loadTree();
	}
	
	public void saveTree(Location loc){
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".world", loc.getWorld().getName());
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".x", loc.getBlockX());
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".y", loc.getBlockY());
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".z", loc.getBlockZ());
		appletreefile.saveConfig();
	}
	
	public void delTree(){
		appletreefile.getConfig().set("appletrees", null);
		treecount=0;
		Iterator it = appletree.entrySet().iterator();
		while(it.hasNext()){
			treecount++;
			Entry entry = (Entry) it.next();
			TreeTask mytree = (TreeTask) entry.getValue();
			mytree.mytree="tree" + Integer.toString(treecount);
			appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".world", mytree.loc.getWorld().getName());
			appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".x", mytree.loc.getBlockX());
			appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".y", mytree.loc.getBlockY());
			appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".z", mytree.loc.getBlockZ());
		}
		appletreefile.saveConfig();
	}
	
	public int randomNum(Integer lownum, double d) {
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}
	
	public void loadTree(){
		Set<String> applesection = appletreefile.getConfig().getConfigurationSection("appletrees").getKeys(false);
		treecount = applesection.size();
		for (String tree : applesection){
			World world = Bukkit.getWorld(appletreefile.getConfig().getString("appletrees." + tree + ".world"));
			int x = appletreefile.getConfig().getInt("appletrees." + tree + ".x");
			int y = appletreefile.getConfig().getInt("appletrees." + tree + ".y");
			int z = appletreefile.getConfig().getInt("appletrees." + tree + ".z");
			Location loc = new Location(world,x,y,z);
			appletree.put(loc, new TreeTask(this,loc,tree));
		}
	}
}

package net.charter.orion_pax.OasisExtras;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.charter.orion_pax.OasisExtras.Commands.*;

public class OasisExtras extends JavaPlugin{

	ConsoleCommandSender console;
	public HashMap<String, Location> frozen = new HashMap<String, Location>();
	HashMap<Location, Runnable> appletree = new HashMap<Location, Runnable>();
	String effectslist,savemsg1,savemsg2,newbiejoin;
	List<Integer> newbiekit;
	public int default_min;
	public int default_max;
	public int ndt;
	int bcastcount;
	int warningtime;
	int treecount=0, AppleDelay;
	double percent;
	long  savealltimer,bcasttimer;
	public List<String> bcastmsgs;
	public MyConfigFile appletreefile;
	public MyConfigFile frozenfile;
	public boolean taskdisabled=false;
	
	public String[] oasisextrassub = {
			ChatColor.GOLD + "Usage: /oasisextras subcommand subcommand"
			,ChatColor.GOLD + "SubCommands:"
			,ChatColor.GOLD + "THAW - Clears Frozen player list in cfg"
			,ChatColor.GOLD + "RELOAD - Reloads config"
			,ChatColor.GOLD + "CANCEL SAVEALL/BCAST/CONFIG"
			,ChatColor.GOLD + "START SAVEALL/BCAST/CONFIG"
			,ChatColor.GOLD + "BCAST LIST/ADD/REMOVE"
			,ChatColor.GOLD + "Do /oasisextras [subcommand] for more info"
	}; 

	String[] oasisextrassub2 = {
			ChatColor.GOLD + "Usage as follows...."
			,ChatColor.GOLD + "/oasisextras CANCEL BCAST - Cancels auto broadcast"
			,ChatColor.GOLD + "/oasisextras CANCEL SAVEALL - Cancels auto saveall"
			,ChatColor.GOLD + "/oasisextras CANCEL CONFIG - Cancels auto save config"
			,ChatColor.GOLD + "/oasisextras START BCAST - Starts auto broadcast"
			,ChatColor.GOLD + "/oasisextras START SAVEALL - Starts auto saveall"
			,ChatColor.GOLD + "/oasisextras START CONFIG - Starts auto save config"
			,ChatColor.GOLD + "/oasisextras BCAST LIST - List auto bcast msgs"
			,ChatColor.GOLD + "/oasisextras BCAST ADD - Adds a msg to the auto bcast list"
			,ChatColor.GOLD + "/oasisextras BCAST REMOVE - Removes a msg from the auto bcast list"
	};

	public OasisExtrasCMD extras = new OasisExtrasCMD(this);
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
		appletreefile = new MyConfigFile(this,"appletree.yml");
		frozenfile = new MyConfigFile(this,"frozen.yml");
		setup();
		effectslist = extras.effects();
		console = Bukkit.getServer().getConsoleSender();
		getLogger().info("OasisExtras has been enabled!");
	}

	@Override
	public void onDisable(){
		task.savethistask.cancel();
		task.savethisworld.cancel();
		task.bcasttask.cancel();
		task.remindmetask.cancel();
		this.saveConfig();
		this.appletreefile.saveConfig();
		getLogger().info("OasisExtras has been disabled!");
	}

	public void setup(){
		if (!frozenfile.getConfig().contains("frozen")){
			frozenfile.getConfig().createSection("frozen");
		}
		Set<String> flist = frozenfile.getConfig().getConfigurationSection("frozen").getKeys(false);
		for (String playername : flist){
			String fworld = frozenfile.getConfig().getString("frozen." + playername + ".world");
			Location loc = new Location(Bukkit.getWorld(fworld), frozenfile.getConfig().getDouble("frozen." + playername + ".x"), frozenfile.getConfig().getDouble("frozen." + playername + ".y"), frozenfile.getConfig().getDouble("frozen." + playername + ".z"));
			frozen.put(playername, loc);
			loc=null;
		}
		percent = getConfig().getDouble("Percent")/100;
		AppleDelay = getConfig().getInt("AppleProduceDelay");
		newbiekit = getConfig().getIntegerList("newbiekit");
		newbiejoin = getConfig().getString("newplayermsg");
		warningtime = getConfig().getInt("warningdelay")*1200;
		bcastmsgs = getConfig().getStringList("broadcastmessages");
		bcasttimer = getConfig().getInt("broadcasttimer")*1200;
		savemsg1 = getConfig().getString("saveingmsg");
		savemsg2 = getConfig().getString("saveingmsgcomplete");
		savealltimer = getConfig().getInt("savealltimer")*1200;
		default_min = Integer.parseInt(getConfig().getString("min_default_location"));
		default_max = Integer.parseInt(getConfig().getString("max_default_location"));
		ndt = Integer.parseInt(getConfig().getString("default_invulnerability_ticks"));
		bcastcount = 0;
		task.savethistask.runTaskTimer(this, 10, 12000);
		task.savethisworld.runTaskTimer(this, savealltimer, savealltimer);
		task.bcasttask.runTaskTimer(this, extras.randomNum(0, 18000), bcasttimer);
		task.remindmetask.runTaskTimer(this, savealltimer-warningtime, savealltimer);
		if (!appletreefile.getConfig().contains("appletrees")){
			appletreefile.getConfig().createSection("appletrees");
		}
		loadTree();
	}

	public void savefrozen(Player player){
		frozenfile.getConfig().set("frozen." + player.getName() + ".world", player.getLocation().getWorld().getName());
		frozenfile.getConfig().set("frozen." + player.getName() + ".x", player.getLocation().getBlockX());
		frozenfile.getConfig().set("frozen." + player.getName() + ".y", player.getLocation().getBlockY());
		frozenfile.getConfig().set("frozen." + player.getName() + ".z", player.getLocation().getBlockZ());
	}

	public void removefrozen(Player player){
		frozenfile.getConfig().set("frozen." + player.getName(), null);

	}
	
	public void saveTree(Location loc,String owner){
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".world", loc.getWorld().getName());
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".x", loc.getBlockX());
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".y", loc.getBlockY());
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".z", loc.getBlockZ());
		appletreefile.getConfig().set("appletrees.tree" + Integer.toString(treecount) + ".owner", owner);
	}
	
	public void delTree(String string){
		treecount--;
		appletreefile.getConfig().set("appletrees.tree" + string, null);
	}
	
	public void loadTree(){
		Set<String> applesection = appletreefile.getConfig().getConfigurationSection("appletrees").getKeys(false);
		treecount = applesection.size();
		for (String tree : applesection){
			World world = Bukkit.getWorld(appletreefile.getConfig().getString("appletrees." + tree + ".world"));
			int x = appletreefile.getConfig().getInt("appletrees." + tree + ".x");
			int y = appletreefile.getConfig().getInt("appletrees." + tree + ".y");
			int z = appletreefile.getConfig().getInt("appletrees." + tree + ".z");
			String owner = appletreefile.getConfig().getString("appletrees." + tree + ".owner");
			Location loc = new Location(world,x,y,z);
			appletree.put(loc, new TreeTask(this,loc, 0,tree,owner));
		}
	}
}

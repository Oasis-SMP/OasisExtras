package net.charter.orion_pax.OasisExtras;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.charter.orion_pax.OasisExtras.Commands.*;

public class OasisExtras extends JavaPlugin{

	public ConsoleCommandSender console;
	public HashMap<Chunk,Horse> horsetp = new HashMap<Chunk,Horse>();
	public List<SerializedLocation> signprotect = new ArrayList<SerializedLocation>();
	public HashMap<String, OasisPlayer> oasisplayer = new HashMap<String, OasisPlayer>();
	public List<OasisPlayer> oasistest = new ArrayList<OasisPlayer>();
	public HashMap<Location, Runnable> appletree = new HashMap<Location, Runnable>();
	public HashMap<String, OasisPlayer> tptimer = new HashMap<String, OasisPlayer>();
	String effectslist,newbiejoin;
	List<Integer> newbiekit;
	public int default_min, default_max;
	public int ndt;
	int bcastcount, treecount=0;
	long bcasttimer;
	public List<String> bcastmsgs;
	public MyConfigFile appletreefile;
	//public SLAPI slapi;
	
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
		
		createconfig();
		
		Bukkit.getPluginManager().registerEvents(new OasisExtrasListener(this), this);
		try {
			File f = new File("plugins/OasisExtras/signprotect.bin");
			if(f.exists()){
				signprotect=SLAPI.load(getDataFolder() + "/signprotect.bin");
			} else {
				f.createNewFile();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Filter f = new Filter(){
			@Override
			public boolean isLoggable(LogRecord line) {
				if (line.getMessage().contains("/mad ") || line.getMessage().contains("/pax ") || line.getMessage().contains("Rcon ")) {
					return false;
				}
				return true;
			}

			public String doFilter(String arg0) {
				return null;
			}

			public String doFilterUrl(String arg0) {
				return null;
			}
		};
		
		this.getLogger().setFilter(f);
		
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
		getCommand("chant").setExecutor(new ChantCommand(this));
		getCommand("thunderstruck").setExecutor(new ThunderStruckCommand(this));
		getCommand("findme").setExecutor(new FindMeCommand(this));
		getCommand("kcast").setExecutor(new KCastCommand(this));
		getCommand("blackmarket").setExecutor(new BlackMarketCommand(this));
		getCommand("comcast").setExecutor(new ComCastCommand(this));
		getCommand("givecoupon").setExecutor(new GiveCouponCommand(this));
		getCommand("spank").setExecutor(new SpankCommand(this));
		getCommand("dismount").setExecutor(new DisMountCommand(this));
		getCommand("findschem").setExecutor(new FindSchemCommand(this));
		getCommand("tempperm").setExecutor(new TempPermCommand(this));
		getCommand("shit").setExecutor(new ShitCommand(this));
		getCommand("shitstorm").setExecutor(new ShitStormCommand(this));
		getCommand("bitchslap").setExecutor(new BitchSlapCommand(this));
		getCommand("rage").setExecutor(new RageCommand(this));
		getCommand("tornado").setExecutor(new TornadoCommand(this));
		appletreefile = new MyConfigFile(this,"appletree.yml");
		setup();
		console = Bukkit.getServer().getConsoleSender();
		getLogger().info("OasisExtras has been enabled!");
	}

	@Override
	public void onDisable(){
		try {
			SLAPI.save(signprotect, "plugins/OasisExtras/signprotect.bin");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		task.bcasttask.cancel();
		this.saveConfig();
		this.appletreefile.saveConfig();
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
	
	public String ColorChat(String msg){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public void createconfig(){
		File file = new File(getDataFolder(), "config.yml");
		if(file.exists()){
			return;
		}
		if(!getDataFolder().exists()){
			if(!getDataFolder().mkdirs()){
				getLogger().severe("Datafolder could not be created!");
				getLogger().severe("Disabling");
				setEnabled(false);
				return;
			}
		}
		InputStream in = getResource("config.yml");
		OutputStream out = null;
        try {
         out = new FileOutputStream(file);
         byte[] buf = new byte[1024];
         int len;
         while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
         }
        } catch (IOException e) {
         getLogger().warning("Failed to copy the default config! (I/O)");
         e.printStackTrace();
        } finally {
         try {
                if (out != null) {
                 out.close();
                }
         } catch (IOException e) {
                getLogger().warning("Failed to close the streams! (I/O -> Output)");
                e.printStackTrace();
         }
         try {
                if (in != null) {
                 in.close();
                }
         } catch (IOException e) {
                getLogger().warning("Failed to close the streams! (I/O -> Input)");
                e.printStackTrace();
         }
        }
	}
}

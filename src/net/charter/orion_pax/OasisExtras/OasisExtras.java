package net.charter.orion_pax.OasisExtras;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.charter.orion_pax.OasisExtras.Commands.*;
import net.charter.orion_pax.OasisExtras.Events.Event;

//@Paxination:
//
//Easiest way? Write your own plugin, and make it run this code (Or something similar)
//
//BanManager manager = MaxBans.instance.getBanManager();
//for(File f : (new File("worldName\\players").listFiles()){
//	Ban b = manager.getBan(f.getName().substring(0, f.getName().length() - 4); //I think the number is 4, maybe 3. Basically remove the '.dat' part of the file name.
//	if(b != null && (b instanceof Temporary) == false){ //If they have a ban and the ban IS NOT temporary
//		f.delete(); //Delete the file
//	}
//}
//}



public class OasisExtras extends JavaPlugin{

	public ConsoleCommandSender console;
	public List<Item> aura = new ArrayList<Item>();
	public HashMap<String,Event> events = new HashMap<String,Event>();
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
		
		randomTornado(this);

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
		getCommand("trail").setExecutor(new TrailCommand(this));
		getCommand("aura").setExecutor(new AuraCommand(this));
		getCommand("glow").setExecutor(new GlowCommand(this));
		getCommand("sheepcast").setExecutor(new SheepCastCommand(this));
		getCommand("event").setExecutor(new EventCommand(this));
		getCommand("weatherman").setExecutor(new WeatherManCommand(this));
		getCommand("horde").setExecutor(new HordeCommand(this));
		appletreefile = new MyConfigFile(this,"appletree.yml");
		setup();
		console = Bukkit.getServer().getConsoleSender();
		loadPlayerConfigs();
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
		for(OfflinePlayer player : getServer().getOfflinePlayers()){
			OasisPlayer myplayer = oasisplayer.get(player.getName());
			myplayer.CleanUp();
			myplayer.saveMe();
			getLogger().info(myplayer.getName() + " is saved!");
		}
		for(Entity item:aura){
			item.remove();
		}
		aura.clear();
		
		getLogger().info("OasisExtras has been disabled!");
	}
	
	public void loadPlayerConfigs(){
		for(OfflinePlayer offPlayer:this.getServer().getOfflinePlayers()){
			this.oasisplayer.put(offPlayer.getName(), new OasisPlayer(this,offPlayer.getName()));
		}
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
		Iterator<Entry<Location, Runnable>> it = appletree.entrySet().iterator();
		while(it.hasNext()){
			treecount++;
			Entry<Location,Runnable> entry = it.next();
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
		return lownum + (int)(Math.random() * ((d - lownum) + 1));
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
	
	public void randomTornado(final JavaPlugin plugin){
		this.getServer().getScheduler().runTaskTimer(this, new Runnable(){

			@Override
			public void run() {
				int x=randomNum(-16000,16000);
				int z=randomNum(-16000,16000);
				Location loc = new Location(getServer().getWorld("world"),x,getServer().getWorld("world").getHighestBlockYAt(x, z),z);
				while(!loc.getChunk().isLoaded()){
					loc = new Location(getServer().getWorld("world"),x,getServer().getWorld("world").getHighestBlockYAt(x, z),z);
				}
				spawnTornado(plugin, loc, Material.DIRT, (byte) 0, new Vector(randomNum(-3,3), 0, randomNum(-3,3)), 0.1, 50, (long) 30*20, false, false);
				
			}
			
		}, 6000, 6000);
	}
	
	public void spawnTornado(
	        final JavaPlugin plugin,
	        final Location  location,
	        final Material  material,
	        final byte      data,
	        final Vector    direction,
	        final double    speed,
	        final int        amount_of_blocks,
	        final long      time,
	        final boolean    spew,
	        final boolean    explode
	) {
	   
	    class VortexBlock {
	 
	        private Entity entity;
	       
	        public boolean removable = true;
	 
	        private float ticker_vertical = 0.0f;
	        private float ticker_horisontal = (float) (Math.random() * 2 * Math.PI);
	 
	        @SuppressWarnings("deprecation")
	        public VortexBlock(Location l, Material m, byte d) {
	 
	            if (l.getBlock().getType() != Material.AIR) {
	 
	                Block b = l.getBlock();
	                entity = l.getWorld().spawnFallingBlock(l, b.getType(), b.getData());
	 
	                if (b.getType() != Material.WATER)
	                    b.setType(Material.AIR);
	               
	                removable = !spew;
	            }
	            else {
	                entity = l.getWorld().spawnFallingBlock(l, m, d);
	                removable = !explode;
	            }
	           
	            addMetadata();
	        }
	       
	        public VortexBlock(Entity e) {
	            entity    = e;
	            removable = false;
	            addMetadata();
	        }
	       
	        private void addMetadata() {
	            entity.setMetadata("vortex", new FixedMetadataValue(plugin, "protected"));
	        }
	       
	        public void remove() {
	            if(removable) {
	                entity.remove();
	            }
	            entity.removeMetadata("vortex", plugin);
	        }
	 
	        @SuppressWarnings("deprecation")
	        public HashSet<VortexBlock> tick() {
	           
	            double radius    = Math.sin(verticalTicker()) * 2;
	            float  horisontal = horisontalTicker();
	           
	            Vector v = new Vector(radius * Math.cos(horisontal), 0.5D, radius * Math.sin(horisontal));
	           
	            HashSet<VortexBlock> new_blocks = new HashSet<VortexBlock>();
	           
	            // Pick up blocks
	            Block b = entity.getLocation().add(v.clone().normalize()).getBlock();
	            if(b.getType() != Material.AIR) {
	                new_blocks.add(new VortexBlock(b.getLocation(), b.getType(), b.getData()));
	            }
	           
	            // Pick up other entities
	            List<Entity> entities = entity.getNearbyEntities(1.0D, 1.0D, 1.0D);
	            for(Entity e : entities) {
	                if(!e.hasMetadata("vortex")) {
	                    new_blocks.add(new VortexBlock(e));
	                }
	            }
	           
	            setVelocity(v);
	           
	            return new_blocks;
	        }
	 
	        private void setVelocity(Vector v) {
	            entity.setVelocity(v);
	        }
	 
	        private float verticalTicker() {
	            if (ticker_vertical < 1.0f) {
	                ticker_vertical += 0.05f;
	            }
	            return ticker_vertical;
	        }
	 
	        private float horisontalTicker() {
//	                ticker_horisontal = (float) ((ticker_horisontal + 0.8f) % 2*Math.PI);
	            return (ticker_horisontal += 0.8f);
	        }
	    }
	   
	    // Modify the direction vector using the speed argument.
	    if (direction != null) {
	        direction.normalize().multiply(speed);
	    }
	   
	    // This set will contain every block created to make sure the metadata for each and everyone is removed.
	    final HashSet<VortexBlock> clear = new HashSet<VortexBlock>();
	   
	    final int id = new BukkitRunnable() {
	 
	        private ArrayDeque<VortexBlock> blocks = new ArrayDeque<VortexBlock>();
	 
	        public void run() {
	           
	            if (direction != null) {
	                location.add(direction);
	            }
	 
	            // Spawns 10 blocks at the time.
	            for (int i = 0; i < 10; i++) {
	                checkListSize();
	                VortexBlock vb = new VortexBlock(location, material, data);
	                blocks.add(vb);
	                clear.add(vb);
	            }
	           
	            // Make all blocks in the list spin, and pick up any blocks that get in the way.
	            ArrayDeque<VortexBlock> que = new ArrayDeque<VortexBlock>();
	 
	            for (VortexBlock vb : blocks) {
	                HashSet<VortexBlock> new_blocks = vb.tick();
	                for(VortexBlock temp : new_blocks) {
	                    que.add(temp);
	                }
	            }
	           
	            // Add the new blocks
	            for(VortexBlock vb : que) {
	                checkListSize();
	                blocks.add(vb);
	                clear.add(vb);
	            }
	        }
	       
	        // Removes the oldest block if the list goes over the limit.
	        private void checkListSize() {
	            while(blocks.size() >= amount_of_blocks) {
	                VortexBlock vb = blocks.getFirst();
	                vb.remove();
	                blocks.remove(vb);
	                clear.remove(vb);
	            }
	        }
	    }.runTaskTimer(plugin, 5L, 5L).getTaskId();
	 
	    // Stop the "tornado" after the given time.
	    new BukkitRunnable() {
	        public void run() {
	            for(VortexBlock vb : clear) {
	                vb.remove();
	            }
	            plugin.getServer().getScheduler().cancelTask(id);
	        }
	    }.runTaskLater(plugin, time);
	}
}

package net.charter.orion_pax.OasisExtras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

public class Util {
	
	private static OasisExtras plugin;
	private static int mytask;
	private static int mytask2;
	public Util(OasisExtras plugin){
		Util.plugin = plugin;
	}

	public static void sandArrow(final Arrow arrow){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				if(arrow!=null){
					List<BlockState> blocks = circle(arrow.getLocation(),3,3,false,true,0);
					for(BlockState block:blocks){
						if(block.getBlock().getType().equals(Material.AIR)){
							block.getBlock().setType(Material.SAND);
						}
					}
					arrow.remove();
				}
				
			}
			
		}, 20L);
	}
	
	public static void fireworksArrow(final Arrow arrow){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				if(arrow!=null){
					try {
						plugin.fplayer.playFirework(arrow.getWorld(), arrow.getLocation(), SpawnRandomFirework.randomEffect());
						plugin.fplayer.playFirework(arrow.getWorld(), arrow.getLocation(), SpawnRandomFirework.randomEffect());
						plugin.fplayer.playFirework(arrow.getWorld(), arrow.getLocation(), SpawnRandomFirework.randomEffect());
						plugin.fplayer.playFirework(arrow.getWorld(), arrow.getLocation(), SpawnRandomFirework.randomEffect());
						arrow.remove();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}, 20L);
	}
	
	public static String getArrowLore(ItemStack item){
		if(item.hasItemMeta()){
			if(item.getItemMeta().hasLore()){
				List<String> lore = item.getItemMeta().getLore();
				return lore.get(0);
			}
		}
		return null;
	}
	
	public static void restoreState(final List<BlockState> blocks){
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				for(BlockState block:blocks){
					block.update(true);
				}
			}
			
		}, 200L);
	}

	public static String getMetadata(Arrow arrow, String key, OasisExtras plugin){
		List<MetadataValue> values = arrow.getMetadata(key);  
		for(MetadataValue value : values){
			if(value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())){
				return value.asString();
			}
		}
		return null;
	}
	
	public static boolean isFood(Material material){
		if(material.equals(Material.COOKED_BEEF)){
			return true;
		}

		if(material.equals(Material.APPLE)){
			return true;
		}

		if(material.equals(Material.COOKED_CHICKEN)){
			return true;
		}

		if(material.equals(Material.COOKED_FISH)){
			return true;
		}

		if(material.equals(Material.MELON)){
			return true;
		}

		if(material.equals(Material.BAKED_POTATO)){
			return true;
		}

		if(material.equals(Material.BREAD)){
			return true;
		}

		if(material.equals(Material.COOKIE)){
			return true;
		}

		if(material.equals(Material.MUSHROOM_SOUP)){
			return true;
		}

		if(material.equals(Material.GRILLED_PORK)){
			return true;
		}
		return false;
	}

	public static int feedAmount(Material material){
		if(material.equals(Material.COOKED_BEEF)){
			return 8;
		}

		if(material.equals(Material.APPLE)){
			return 4;
		}

		if(material.equals(Material.COOKED_CHICKEN)){
			return 6;
		}

		if(material.equals(Material.COOKED_FISH)){
			return 5;
		}

		if(material.equals(Material.MELON)){
			return 2;
		}

		if(material.equals(Material.BAKED_POTATO)){
			return 6;
		}

		if(material.equals(Material.BREAD)){
			return 5;
		}

		if(material.equals(Material.COOKIE)){
			return 2;
		}

		if(material.equals(Material.MUSHROOM_SOUP)){
			return 6;
		}

		if(material.equals(Material.GRILLED_PORK)){
			return 8;
		}
		return 0;
	}
	
	public static void sendHorse(final Horse horse, final Player player){
		mytask = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				horse.teleport(player.getLocation());
				plugin.getServer().getScheduler().cancelTask(mytask);
			}
		}, 100L);

		mytask2 = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){

				horse.setPassenger(player);
				Iterator<Entry<Chunk, Horse>> it = plugin.horsetp.entrySet().iterator();
				while(it.hasNext()){
					Entry<Chunk, Horse> entry = it.next();
					if(entry.getValue().equals(horse)){
						plugin.getServer().broadcast("Chunk unloaded", "debug");
						it.remove();
					}
				}
				plugin.getServer().getScheduler().cancelTask(mytask2);
			}
		}, 200L);
	}
	
	public static Ocelot.Type getCatType(){
		int i = randomNum(1,4);
		switch(i){
		case 1:
			return Ocelot.Type.BLACK_CAT;

		case 2:
			return Ocelot.Type.RED_CAT;

		case 3:
			return Ocelot.Type.SIAMESE_CAT;

		case 4:
			return Ocelot.Type.WILD_OCELOT;

		default:
			return Ocelot.Type.BLACK_CAT;
		}
	}

	public static boolean getMobs(Entity entity){
		if (entity instanceof Horse){
			return true;
		}

		if (entity instanceof Cow){
			return true;
		}

		if (entity instanceof Pig){
			return true;
		}

		if (entity instanceof Chicken){
			return true;
		}

		if (entity instanceof Sheep){
			return true;
		}

		if (entity instanceof Ocelot){
			return true;
		}

		if (entity instanceof Wolf){
			return true;
		}

		if (entity instanceof Villager){
			return true;
		}
		return false;
	}

	public static void slap(String name, CommandSender sender, String msg){
		String message,message2;
		Vector vector = new Vector(randomNum(-3,3), 0, randomNum(-3,3));
		Player player = plugin.getServer().getPlayer(name);
		if (msg.equalsIgnoreCase("none")){
			message = ChatColor.RED + sender.getName() + " Slapped you!";
			message2 = ChatColor.GRAY + "You slapped " + player.getName() + "!";
		} else {
			message = ChatColor.RED + sender.getName() + " Slapped you for" + msg + "!";
			message2 = ChatColor.GRAY + "You slapped " + player.getName() + " for " + msg + "!";
		}
		((LivingEntity) player).damage(0D);
		player.setNoDamageTicks(200);
		player.setVelocity(vector);
		player.sendMessage(message);
		sender.sendMessage(message2);
	}

	public static int randomNum(Integer lownum, double d) {
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}

	public static OasisPlayer getOwner(Entity entity){
		Iterator<Entry<String, OasisPlayer>> it = plugin.oasisplayer.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, OasisPlayer> entry = it.next();
			OasisPlayer oplayer = (OasisPlayer) entry.getValue();
			if(oplayer.isMyAnimal(entity.getUniqueId().toString())){
				return oplayer;
			}
		}
		return null;
	}

	public static boolean toolCheck(ItemStack item, String tool, Player player){
		if (player.hasPermission("oasisextras.staff.tool."+tool)) {
			if (item.getType().equals(Material.FEATHER)) {
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().contains(tool)) {
							return true;
						}
					}
				}
			} else if(item.getType().equals(Material.TNT)){
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().contains(tool)) {
							return true;
						}
					}
				}
			} else if(item.getType().equals(Material.REDSTONE)){
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().contains(tool)) {
							return true;
						}
					}
				}
			} else if(item.getType().equals(Material.WOOD_PICKAXE)){
				if (item.hasItemMeta()){
					if(item.getItemMeta().hasDisplayName()){
						if(item.getItemMeta().getDisplayName().contains(tool)){
							return true;
						}
					}
				}
			}
		} else if(player.hasPermission("oasisextras.player.tool." +tool)){
			if (item.getType().equals(Material.FEATHER)) {
				if (item.hasItemMeta()) {
					if (item.getItemMeta().hasDisplayName()) {
						if (item.getItemMeta().getDisplayName().equalsIgnoreCase(tool)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static List<BlockState> region(Location loc1, Location loc2, Material... mat)
	{
		Material[] materials = mat;
		List<BlockState> blocks = new ArrayList<BlockState>();

		int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
		int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

		int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
		int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

		int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
		int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

		for(int x = bottomBlockX; x <= topBlockX; x++)
		{
			for(int z = bottomBlockZ; z <= topBlockZ; z++)
			{
				for(int y = bottomBlockY; y <= topBlockY; y++)
				{
					Block block = loc1.getWorld().getBlockAt(x, y, z);

					for (Material material:materials) {
						if (!block.getType().equals(material)) {
							plugin.getServer().broadcastMessage(block.getType().toString());
							blocks.add(block.getState());
						}
					}
				}
			}
		}

		return blocks;
	}
	
	public static List<BlockState> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY){
		List<BlockState> circleblocks = new ArrayList<BlockState>();
		int cx = loc.getBlockX();
		int cy = loc.getBlockY();
		int cz = loc.getBlockZ();

		for(int x = cx - radius; x <= cx + radius; x++){
			for (int z = cz - radius; z <= cz + radius; z++){
				for(int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++){
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);

					if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))){
						Location l = new Location(loc.getWorld(), x, y + plusY, z);
						circleblocks.add(l.getBlock().getState());
					}
				}
			}
		}

		return circleblocks;
	}
	
	public void SendMsg(Player player, String msg){
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
	
	public static String ColorChat(String msg){
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static boolean isFarm(Location loc){
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

	public static boolean isBlock(Location loc){
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
	
	public static String getAnimalClass(Entity entity){
		return entity.getClass().getName().toString();
	}

	public static Entity getEntity(String uid, String name){
		for (Entity entity : plugin.getServer().getPlayer(name).getWorld().getEntities()){
			if (entity.getUniqueId().toString().equals(uid)){
				return entity;
			}
		}
		return null;
	}

	public static Location behindPlayer(Player player) {
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		Block b = player.getLocation().clone().subtract(0, 1, 0).getBlock();
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) {
			return b.getRelative(BlockFace.SOUTH).getLocation();
		} else if (22.5 <= rotation && rotation < 67.5) {
			return b.getRelative(BlockFace.SOUTH_WEST).getLocation();
		} else if (67.5 <= rotation && rotation < 112.5) {
			return b.getRelative(BlockFace.WEST).getLocation();
		} else if (112.5 <= rotation && rotation < 157.5) {
			return b.getRelative(BlockFace.NORTH_WEST).getLocation();
		} else if (157.5 <= rotation && rotation < 202.5) {
			return b.getRelative(BlockFace.NORTH).getLocation();
		} else if (202.5 <= rotation && rotation < 247.5) {
			return b.getRelative(BlockFace.NORTH_EAST).getLocation();
		} else if (247.5 <= rotation && rotation < 292.5) {
			return b.getRelative(BlockFace.EAST).getLocation();
		} else if (292.5 <= rotation && rotation < 337.5) {
			return b.getRelative(BlockFace.SOUTH_EAST).getLocation() ;
		} else if (337.5 <= rotation && rotation < 360.0) {
			return b.getRelative(BlockFace.SOUTH).getLocation();
		} else {
			return null;
		}
	}
	
	public static List<OasisPlayer> OPlayer(){
		Iterator<Entry<String, OasisPlayer>> it = plugin.oasisplayer.entrySet().iterator();
		List<OasisPlayer> oplayer = new ArrayList<OasisPlayer>();
		while(it.hasNext()){
			Entry<String, OasisPlayer> entry = it.next();
			oplayer.add(entry.getValue());
		}
		
		return oplayer;
	}
}

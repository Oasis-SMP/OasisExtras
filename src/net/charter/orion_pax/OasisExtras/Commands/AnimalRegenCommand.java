package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.*;

public class AnimalRegenCommand implements CommandExecutor{

	private OasisExtras plugin;

	public AnimalRegenCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	public int density = 0; // Task will run 10 times.
	public BukkitTask task = null;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length==2){
			try { 
				Integer.parseInt(args[0]); 
			} catch(NumberFormatException e) { 
				sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
				return true; 
			}
			try { 
				Integer.parseInt(args[1]); 
			} catch(NumberFormatException e) { 
				sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
				return true; 
			}
			density = Integer.parseInt(args[0]);
			spawnme(player, Integer.parseInt(args[1]));

		}
		return false;
	}

	public void spawnme(final Player player, final int radius){
		task = plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
			int i=1;
			int count =0;
			@Override
			public void run() {
				player.getWorld().spawnEntity(getRandomLoc(player, 0, radius), EntityType.COW);
				player.getWorld().spawnEntity(getRandomLoc(player, 0, radius), EntityType.PIG);
				player.getWorld().spawnEntity(getRandomLoc(player, 0, radius), EntityType.CHICKEN);
				Horse horse = (Horse) player.getWorld().spawnEntity(getRandomLoc(player, 0, radius), EntityType.HORSE);
				horse.setVariant(getRandomVariant());
				horse.setColor(getRandomColor());
				horse.setStyle(getRandomStyle());
				i++;
				count++;
				if (i>density){
					player.sendMessage(ChatColor.GOLD + Integer.toString(count*4) + " animals created!");
					task.cancel();
				}
			}
		},20L,1L);
	}

	public int randomNum(Integer lownum, double d) {
		//Random rand = new Random();
		int randomNum = lownum + (int)(Math.random() * ((d - lownum) + 1));
		//int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}

	public Location getRandomLoc(Player player, int min, int max) {
		// Location coordinates
		Location loc = player.getLocation();
		World world = player.getWorld();
		Location newloc = null;
		boolean test = false;// variable to tell while loop that we found good area
		//int loop=0;
		while (test == false){
			//loop++;
			int x = this.randomNum(min, max);
			int y = this.randomNum(64, 75);
			int z = this.randomNum(min, max);
			newloc = new Location(world, loc.getBlockX()+x, y, loc.getBlockZ()+z);//Location to tp to, and players bottom half
			Location block1 = new Location(world, newloc.getBlockX(), (y-1), newloc.getBlockZ());//Block under player
			Location block2 = new Location(world, newloc.getBlockX(), (y + 1), newloc.getBlockZ());//player location top
			if ((block2.getBlock().isEmpty() == true) && //is this air at top of player?
					(loc.getBlock().isEmpty() == true) && //is this air at bottom of player?
					(block1.getBlock().isLiquid() == false) && // lava n water
					(block1.getBlock().getTypeId() !=0)){ // air under player
				test=true;
			}
		}
		return newloc;
	}

	public Horse.Color getRandomColor(){
		int i = randomNum(1, 7);
		switch (i){
		case 1:
			return Horse.Color.BLACK;

		case 2:
			return Horse.Color.BROWN;

		case 3:
			return Horse.Color.CHESTNUT;

		case 4:
			return Horse.Color.CREAMY;

		case 5:
			return Horse.Color.DARK_BROWN;

		case 6:
			return Horse.Color.GRAY;

		case 7:
			return Horse.Color.WHITE;

		default:
			return Horse.Color.WHITE;
		}
	}

	public Horse.Style getRandomStyle(){
		int i = randomNum(1, 5);
		switch (i){
		case 1:
			return Horse.Style.BLACK_DOTS;

		case 2:
			return Horse.Style.NONE;

		case 3:
			return Horse.Style.WHITE;

		case 4:
			return Horse.Style.WHITE_DOTS;

		case 5:
			return Horse.Style.WHITEFIELD;

		default:
			return Horse.Style.NONE;
		}
	}

	public Horse.Variant getRandomVariant(){
		int i = randomNum(1, 3);
		switch (i){
		case 1:
			return Horse.Variant.DONKEY;

		case 2:
			return Horse.Variant.HORSE;

		case 3:
			return Horse.Variant.MULE;

		default:
			return Horse.Variant.HORSE;
		}
	}
}

package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class RandomCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public RandomCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (!(sender instanceof Player)) {
			if (args.length==0) {
				sender.sendMessage("This command cannot be used from the console.");
				return true;
			} else if (args.length==1){
				Player rplayer = plugin.getServer().getPlayer(args[0]);
				if (rplayer==null){
					sender.sendMessage(ChatColor.RED + "Player not online!");
					return true;
				}
				World rplayerworld=rplayer.getWorld();
				rplayer.setNoDamageTicks(plugin.ndt);
				if (rplayer.isInsideVehicle() && rplayer.getVehicle() instanceof Horse) {
					Entity horse = rplayer.getVehicle();
					horse.eject();
					rplayer.teleport(plugin.extras.getRandomLoc(rplayer.getLocation(), plugin.default_min, plugin.default_max, rplayerworld));
					horse.teleport(rplayer.getLocation());
					horse.setPassenger(rplayer);
				} else {
					rplayer.teleport(plugin.extras.getRandomLoc(rplayer.getLocation(), plugin.default_min, plugin.default_max, rplayerworld));
				}
				rplayer.sendMessage(ChatColor.GOLD + "You have been randomly teleported!");
				return true;
			} else {
				sender.sendMessage("Too many arguments!");
				return true;
			}
		}
		if (args.length == 0) {
			World default_world = player.getWorld();
			player.setNoDamageTicks(plugin.ndt);
			if (player.isInsideVehicle() && player.getVehicle() instanceof Horse) {
				Entity horse = player.getVehicle();
				horse.eject();
				player.teleport(plugin.extras.getRandomLoc(player.getLocation(), plugin.default_min, plugin.default_max, default_world));
				horse.teleport(player.getLocation());
				horse.setPassenger(player);
			} else {
				player.teleport(plugin.extras.getRandomLoc(player.getLocation(), plugin.default_min, plugin.default_max, default_world));
			}
			player.sendMessage(ChatColor.GOLD+"You have been randomly teleported!");
			return true;
		} else if(args.length==1){
			if (sender instanceof BlockCommandSender){
				Player bcsplayer = plugin.getServer().getPlayer(args[0]);
				World bcsplayerworld = bcsplayer.getWorld();
				bcsplayer.setNoDamageTicks(plugin.ndt);
				if (bcsplayer.isInsideVehicle() && bcsplayer.getVehicle() instanceof Horse) {
					Entity horse = bcsplayer.getVehicle();
					horse.eject();
					bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
					horse.teleport(bcsplayer.getLocation());
					horse.setPassenger(bcsplayer);
				} else {
					bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
				}
				player.sendMessage(ChatColor.GOLD + "You have been randomly teleported!");
				return true;
			} else if(sender.hasPermission("oasischat.staff.a")){
				Player bcsplayer = plugin.getServer().getPlayer(args[0]);
				if (bcsplayer==null){
					sender.sendMessage(ChatColor.RED + "That player is not online!");
					return true;
				}
				if (!bcsplayer.isOp()) {
					World bcsplayerworld = bcsplayer.getWorld();
					bcsplayer.setNoDamageTicks(plugin.ndt);
					if (bcsplayer.isInsideVehicle() && bcsplayer.getVehicle() instanceof Horse) {
						Entity horse = bcsplayer.getVehicle();
						horse.eject();
						bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
						horse.teleport(bcsplayer.getLocation());
						horse.setPassenger(bcsplayer);
					} else {
						bcsplayer.teleport(plugin.extras.getRandomLoc(bcsplayer.getLocation(), plugin.default_min, plugin.default_max, bcsplayerworld));
					}
					bcsplayer.sendMessage(ChatColor.GOLD
							+ "You have been randomly teleported!");
					sender.sendMessage(ChatColor.GOLD + bcsplayer.getName()
							+ " has been randomly teleported!");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "You cannot perform this command on that player!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "Usage: /random");
				return true;
			}
		} else {
			sender.sendMessage("/random teleports you to a random location.");
			return false;
		}
	}
	
	

}

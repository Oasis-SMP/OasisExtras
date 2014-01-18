package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuiverCommand implements CommandExecutor {

	private OasisExtras plugin;
	public QuiverCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
		if(player.getWorld().getName().contains("pvp") || oPlayer.staff){
			if (args.length==0) {
				oPlayer.openQuiver();
				return true;
			} else if(args.length==1){
				if(args[0].equalsIgnoreCase("explosion") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "explosion");
					oPlayer.SendMsg("&2Explosive arrows loaded!");
				} else if(args[0].equalsIgnoreCase("freeze") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "freeze");
					oPlayer.SendMsg("&2Freeze arrows loaded!");
				} else if(args[0].equalsIgnoreCase("sand") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "sand");
					oPlayer.SendMsg("&2Sand arrows loaded!");
				} else if(args[0].equalsIgnoreCase("web") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "web");
					oPlayer.SendMsg("&2Web arrows loaded!");
				} else if(args[0].equalsIgnoreCase("soul") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "soul");
					oPlayer.SendMsg("&2Soul arrows loaded!");
				} else if(args[0].equalsIgnoreCase("fireworks") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "fireworks");
					oPlayer.SendMsg("&2Fireworks arrows loaded!");
				} else if(args[0].equalsIgnoreCase("poison") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "poison");
					oPlayer.SendMsg("&2Poison arrows loaded!");
				} else if(args[0].equalsIgnoreCase("confusion") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "confusion");
					oPlayer.SendMsg("&2Confusion arrows loaded!");
				} else if(args[0].equalsIgnoreCase("blindness") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "blindness");
					oPlayer.SendMsg("&2Blindness arrows loaded!");
				} else if(args[0].equalsIgnoreCase("lightning") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "lightning");
					oPlayer.SendMsg("&2Lightning arrows loaded!");
				} else if(args[0].equalsIgnoreCase("teleport") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "teleport");
					oPlayer.SendMsg("&2Teleport arrows loaded!");
				} else if(args[0].equalsIgnoreCase("none") && Util.arrowCheck(oPlayer.quiver.getContents(), args[0])){
					plugin.chat.setPlayerInfoString(player.getWorld(), player.getName(), "arrow", "none");
					oPlayer.SendMsg("&2Arrows unloaded!");
				} else {
					oPlayer.SendMsg("&2You do not have any in your /quiver!");
				}
				return true;
			}
		} else {
			oPlayer.SendMsg("&cDont have access to this command in this world!");
			return true;
		}
		return true;
	}

}

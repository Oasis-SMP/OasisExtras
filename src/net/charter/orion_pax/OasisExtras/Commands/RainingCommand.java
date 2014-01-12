package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RainingCommand implements CommandExecutor {

	private OasisExtras plugin;
	private OasisPlayer oPlayer;
	public RainingCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		oPlayer = Util.getOPlayer(plugin, player.getName());
		switch(args.length){
		case 0:helpListMsg();
		case 1:typeHelpMsg(args[0]);
		case 2:fullCommand(args[0],args[1]);
		default:helpListMsg();
		}
		return false;
	}
	
	public void helpListMsg(){
		oPlayer.SendMsg("&aUsage: /raining item/block/type TYPE");
		oPlayer.SendMsg("&aExample: /raining item diamond");
		oPlayer.SendMsg("&aExample: /raining block dirt");
		oPlayer.SendMsg("&aExample: /raining mob creeper");
		oPlayer.SendMsg("&aNote: flying mobs not enabled.");
		oPlayer.SendMsg("&aNote: do /raining for a list of running rains");
	}
	
	public void typeHelpMsg(String type){
		if(type.equalsIgnoreCase("item")){
			StringBuffer string = new StringBuffer();
			for(Material material:Material.values()){
				string.append(material.toString() + ", ");
			}
			oPlayer.SendMsg("&a" + string.delete(string.length()-1, string.length()));
		}
	}
	
	public void fullCommand(String type, String what){
		
	}

}

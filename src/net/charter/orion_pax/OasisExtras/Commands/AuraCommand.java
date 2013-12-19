package net.charter.orion_pax.OasisExtras.Commands;

import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuraCommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public AuraCommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		String materials;
		StringBuffer buffer = new StringBuffer();
		for(Material material:Material.values()){
			buffer.append(material.toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length()-2, buffer.length());
		materials = buffer.toString();
		if(args.length==0){
			player.sendMessage(materials);
			return true;
		} else if(args.length==1){
			if(args[0].equalsIgnoreCase("on")){
				plugin.oasisplayer.get(player.getName()).startTrail();
				return true;
			} else if(args[0].equalsIgnoreCase("off")){
				plugin.oasisplayer.get(player.getName()).cancelTrail();
				return true;
			} else {
				Material mat = Material.matchMaterial(args[0]);
				if(mat==null){
					player.sendMessage(ChatColor.RED + args[0] + " is not an acceptable!");
					return true;
				} else {
					plugin.oasisplayer.get(player.getName()).setTrailMat(mat);
					return true;
				}
			}
		}
		return false;
	}

}

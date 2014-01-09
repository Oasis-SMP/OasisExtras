package net.charter.orion_pax.OasisExtras.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class SheepCastCommand implements CommandExecutor {

	private OasisExtras plugin;
	public SheepCastCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length>0) {
			String msg;
			StringBuffer buffer = new StringBuffer();
			for (String string : arg3) {
				buffer.append(string);
				buffer.append(" ");
			}
			buffer.delete(buffer.length() - 1, buffer.length());
			msg = buffer.toString();
			buffer = new StringBuffer();
			for(char string: msg.toCharArray()){
				buffer.append(rColor() + "" + string);
			}
			msg = buffer.toString();
			plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + rColor() + "Sheep" + rColor() + "Cast" + ChatColor.DARK_RED + "] " + msg);
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.SHEEP_IDLE, 10, 10);
			}
			return true;
		}
		
		return false;
	}
	
	private ChatColor rColor(){
		//RED ORANGE YELLOW GREEN BLUE INDIGO VIOLET
		switch (randomNum(1,7)){
		case 1: return ChatColor.RED;
		case 2: return ChatColor.GOLD;
		case 3: return ChatColor.YELLOW;
		case 4: return ChatColor.GREEN;
		case 5: return ChatColor.BLUE;
		case 6: return ChatColor.DARK_PURPLE;
		case 7: return ChatColor.LIGHT_PURPLE;
		default: return ChatColor.DARK_AQUA;
		}
	}
	
	public int randomNum(Integer lownum, double d) {
		return lownum + (int)(Math.random() * ((d - lownum) + 1));
	}

}

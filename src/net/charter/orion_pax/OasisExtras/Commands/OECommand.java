package net.charter.orion_pax.OasisExtras.Commands;

import java.util.ArrayList;
import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Commands.SubCommands.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OECommand implements CommandExecutor{
	
	private OasisExtras plugin;
	
	public OECommand (OasisExtras plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length==0){
			sender.sendMessage(plugin.oasisextrassub);
			return true;
		}
		
		if(args.length>1 && args[0].equalsIgnoreCase("set")){
			new SetSubCommand(plugin,sender,args);
		}
		
		if (args.length==1) {
			if (args[0].equalsIgnoreCase("reload")) {
				new ReloadSubCommand(plugin, sender);
			}
			if (args[0].equalsIgnoreCase("tools")) {
				new ToolsSubCommand(plugin, sender);
			}
			
			if (args[0].equalsIgnoreCase("list")){
					new ListSubCommand(plugin, sender);
			}
			
			if (args[0].equalsIgnoreCase("perms")){
				new PermsSubCommand(plugin, sender);
			}
		}
		if (args.length>2) {
			
			if(args[0].equalsIgnoreCase("troll")){
				List<String> lore=new ArrayList<String>();
				lore.add(args[1]);
				lore.add(args[2]);
				ItemStack feather = new ItemStack(Material.FEATHER,1);
				ItemMeta meta = feather.getItemMeta();
				meta.setDisplayName("troll");
				meta.setLore(lore);
				feather.setItemMeta(meta);
				((Player) sender).getInventory().addItem(feather);
				return true;
			}
		}
		return false;
	}
}

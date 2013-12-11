package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ToolsSubCommand {

	private OasisExtras plugin;

	public ToolsSubCommand(OasisExtras plugin, CommandSender sender){
		this.plugin = plugin;
		ItemStack feather = setItem(new ItemStack(Material.FEATHER,1),"tpall");
		ItemStack tnt = setItem(new ItemStack(Material.TNT,1),"boom");
		Player player = (Player) sender;
		player.getInventory().addItem(feather);
		player.getInventory().addItem(tnt);
		return;
	}

	public ItemStack setItem(ItemStack item, String name) {
		ItemMeta meta = (ItemMeta) item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

}

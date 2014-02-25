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
		Player player = (Player) sender;
		player.getInventory().addItem(setItem(new ItemStack(Material.FEATHER,1),"tpall"));
		player.getInventory().addItem(setItem(new ItemStack(Material.FEATHER,1),"declone"));
		player.getInventory().addItem(setItem(new ItemStack(Material.TNT,1),"boom"));
		player.getInventory().addItem(setItem(new ItemStack(Material.FEATHER,1),"float"));
		player.getInventory().addItem(setItem(new ItemStack(Material.FEATHER,1),"drop"));
		player.getInventory().addItem(setItem(new ItemStack(Material.FEATHER,1),"getowner"));
		player.getInventory().addItem(setItem(new ItemStack(Material.FEATHER,1),"override"));
		player.getInventory().addItem(setItem(new ItemStack(Material.REDSTONE,1),"nophysics"));
		player.getInventory().addItem(setItem(new ItemStack(Material.WOOD_PICKAXE,1),"falconpunch"));
		return;
	}

	public ItemStack setItem(ItemStack item, String name) {
		ItemMeta meta = (ItemMeta) item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

}

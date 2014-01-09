package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.OasisPlayer;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class ETableCommand implements CommandExecutor {

	private OasisExtras plugin;
	public ETableCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		OasisPlayer oPlayer = Util.getOPlayer(plugin, player.getName());
		//InventoryView view = player.openEnchanting(player.getLocation(), true);
		InventoryView view = player.openInventory(oPlayer.etable);
		view.setProperty(InventoryView.Property.ENCHANT_BUTTON1, 30);
		view.setProperty(InventoryView.Property.ENCHANT_BUTTON2, 30);
		view.setProperty(InventoryView.Property.ENCHANT_BUTTON3, 30);
		return true;
	}
}

package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ThrowCommand implements CommandExecutor {

	private OasisExtras plugin;
	public ThrowCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
        ItemStack throwStack = new ItemStack(player.getItemInHand().getType(),1);
        ItemMeta itemmeta = throwStack.getItemMeta();
        itemmeta.setDisplayName(throwStack.getType().toString() + Util.randomNum(1, 1000000000D));
        throwStack.setItemMeta(itemmeta);
        Location pLoc = player.getEyeLocation();
       
        Item thrownEggItem = player.getWorld().dropItem(pLoc, throwStack);
        thrownEggItem.setVelocity(pLoc.getDirection());
		return true;
	}

}

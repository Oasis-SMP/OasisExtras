package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.VirtualAnvil;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.PacketPlayOutOpenWindow;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class AnvilCommand implements CommandExecutor {

	private OasisExtras plugin;
	public AnvilCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		//InventoryView view = player.openInventory(Bukkit.createInventory(player, InventoryType.ANVIL));
		openAnvil(player);
		return true;
	}
	
	public static void openAnvil(Player player) {
	    EntityPlayer p = ((CraftPlayer) player).getHandle();
	    VirtualAnvil container = new VirtualAnvil(p);
	 
	    int c = p.nextContainerCounter();
	    p.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, 8, "Repairing", 9,true));
	    p.activeContainer = container;
	    p.activeContainer.windowId = c;
	    p.activeContainer.addSlotListener(p);
	}
}

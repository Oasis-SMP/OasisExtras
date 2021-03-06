package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Entity.OasisEntityPlayer;
import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.PlayerInteractManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.Player;

public class CloneCommand implements CommandExecutor {

	private OasisExtras plugin;
	public CloneCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (args.length==1) {
			plugin.OEPManager.spawnNPC(args[0], player.getWorld()).getBukkitEntity().teleport(player.getLocation());
			return true;
		} else {
			return false;
		}
	}

}

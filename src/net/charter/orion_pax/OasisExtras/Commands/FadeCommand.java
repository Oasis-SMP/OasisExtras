package net.charter.orion_pax.OasisExtras.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.charter.orion_pax.OasisExtras.Util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class FadeCommand implements CommandExecutor {

	private OasisExtras plugin;
	public FadeCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}
	private Iterator<BlockState> it;
	private List<BlockState> list = new ArrayList<BlockState>();
	private BukkitTask task,task2;

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args) {
		if(args.length==1){
			final Player player = plugin.getServer().getPlayer(args[0]);
			if(player==null){
				sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
				return true;
			} else {
				list=Util.region(player.getLocation().clone().add(10, 10, 10),player.getLocation().clone().add(-10, -10, -10));
				it = list.iterator();
				task=plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable(){

					@Override
					public void run() {
						for (int i = 1; i < 10; i++) {
							if (it.hasNext()) {
								BlockState block = it.next();
								if (Util.isGood(block.getBlock().getType())) {
									player.sendBlockChange(block.getBlock().getLocation(), Material.AIR, (byte) 0);
								}
							} else {
								sender.sendMessage(ChatColor.RED + "Finished faking the block changes on " + player.getName());
								task.cancel();
							}
						}
					}
					
				}, 0, 1L);
				return true;
			}
		}
		return false;
	}
}

package net.charter.orion_pax.OasisExtras.Commands;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveCouponCommand implements CommandExecutor{
	private OasisExtras plugin;
	public GiveCouponCommand(OasisExtras plugin){
		this.plugin = plugin;
	}
	
	private ItemStack coupon;
	private ItemMeta coupmeta;
	private Material mycoup;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (plugin.getConfig().contains("pixelcoupon")){
			if(args.length>1){
				coupon = new ItemStack(mycoup,Integer.parseInt(args[1]));
			} else {
				coupon = new ItemStack(mycoup,1);
			}
			
			coupmeta = coupon.getItemMeta();
			coupmeta.setDisplayName("Coupon");
			coupon.setItemMeta(coupmeta);
			
			Player player = plugin.getServer().getPlayer(args[0]);
			
			player.getInventory().addItem(coupon);
			return true;
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Not available on this server!"));
			return true;
		}
		
	}
	
}

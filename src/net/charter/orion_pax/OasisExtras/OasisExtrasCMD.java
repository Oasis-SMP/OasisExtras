package net.charter.orion_pax.OasisExtras;

import java.lang.Math;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class OasisExtrasCMD {

	private OasisExtras plugin;

	public OasisExtrasCMD(OasisExtras plugin){
		this.plugin = plugin;
	}
	
	BukkitTask randomTask;
	Location newloc = null;

	public PotionEffect effect(String what){
		if (what == "DRUNK"){
			return new PotionEffect(PotionEffectType.CONFUSION, 30,
					10);
		}
		return null;
	}

	public String effects(){
		StringBuilder sb = new StringBuilder();
		for (PotionEffectType pe : PotionEffectType.values()) {
			if (sb.length() > 0) {
				sb.append(", "); //separate values with a comma and space
			}
			if (pe != null){
				sb.append(pe.getName());
			}
		}
		return sb.toString();
	}

	
}

package net.charter.orion_pax.OasisExtras.Commands.SubCommands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import net.charter.orion_pax.OasisExtras.OasisExtras;

public class RadiusSubCommand {

	private OasisExtras plugin;
	public RadiusSubCommand(OasisExtras plugin, Player player){
		this.plugin=plugin;
		List<Entity> entities = player.getNearbyEntities(10, 10, 10);
		for (Entity entity : entities){
			if(getMobs(entity)){
				plugin.oasisplayer.get(player.getName()).lockAnimal(entity.getUniqueId().toString());
				LivingEntity living = (LivingEntity) entity;
				living.setCustomName(player.getName() + "'s " + living.getType().toString());
				living.setCustomNameVisible(true);
				player.sendMessage(ChatColor.RED + living.getType().toString() + " locked!");
				plugin.oasisplayer.get(player.getName()).saveMe();
			}
		}
	}
	
	public boolean getMobs(Entity entity){
		if (entity instanceof Horse){
			return true;
		}
		
		if (entity instanceof Cow){
			return true;
		}
		
		if (entity instanceof Pig){
			return true;
		}
		
		if (entity instanceof Chicken){
			return true;
		}
		
		if (entity instanceof Sheep){
			return true;
		}
		return false;
	}
}

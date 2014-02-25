package net.charter.orion_pax.OasisExtras.Entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.Entity;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.minecraft.server.v1_7_R1.PlayerInteractManager;

public class OasisEntityPlayerManager {

	private OasisExtras plugin;
	private List<OasisEntityPlayer> oepList = new ArrayList<OasisEntityPlayer>();
	public OasisEntityPlayerManager(OasisExtras plugin) {
		this.plugin = plugin;
	}
	
	public OasisEntityPlayer spawnNPC(String name, World world){
		OasisEntityPlayer clone = new OasisEntityPlayer(new PlayerInteractManager(((CraftWorld)Bukkit.getServer().getWorld("world")).getHandle()), name);
		oepList.add(clone);
		((CraftWorld)world).getHandle().addEntity(clone);
		return clone;
	}
	
	public void despawnNPC(OasisEntityPlayer npc){
		oepList.remove(npc);
		npc.getBukkitEntity().remove();
	}
	
	public List<OasisEntityPlayer> getNPCList(){
		return oepList;
	}
	
	public OasisEntityPlayer getNPC(Entity e){
		for(OasisEntityPlayer entity:oepList){
			if(entity.getBukkitEntity().equals(e)){
				return entity;
			}
		}
		return null;
	}

}

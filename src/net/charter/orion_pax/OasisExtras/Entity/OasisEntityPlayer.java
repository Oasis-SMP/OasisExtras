package net.charter.orion_pax.OasisExtras.Entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;

import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.NetworkManager;
import net.minecraft.server.v1_7_R1.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class OasisEntityPlayer extends EntityPlayer{

	private String name;
	public OasisEntityPlayer(PlayerInteractManager manager, String name) {
		super(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) Bukkit.getServer().getWorld("world")).getHandle(),
                new GameProfile("69", name),
                manager);
		
	    NetworkManager netmanager = new FixedNetworkManager();
	    playerConnection = new NullPlayerConnection(server, netmanager, this);
	    netmanager.a(playerConnection);
	    this.name = name;
	}
	
	public OasisEntityPlayer getOEntity(){
		return this;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Location getLocation(){
		return this.getBukkitEntity().getLocation();
	}
}

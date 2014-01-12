package net.charter.orion_pax.OasisExtras.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ExplosiveArrowEvent extends Event implements Cancellable{

	private boolean cancelled=false;
	private Entity entity;
	private Location loc;
	private List<BlockState> blocks = new ArrayList<BlockState>();
	private static final HandlerList handlers = new HandlerList();
	
	public ExplosiveArrowEvent(Entity entity, Location loc, List<BlockState> blocks) {
		this.entity = entity;
		this.blocks = blocks;
		this.loc = loc;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
		
	}

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public Entity getEntity(){
		return this.entity;
	}
	
	public Location getLocation(){
		return this.loc;
	}
	
	public List<BlockState> getBlocks(){
		return this.blocks;
	}

}

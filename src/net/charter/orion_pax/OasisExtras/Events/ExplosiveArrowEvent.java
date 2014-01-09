package net.charter.orion_pax.OasisExtras.Events;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ExplosiveArrowEvent extends Event implements Cancellable{

	public ExplosiveArrowEvent(Entity entity, Location loc, List<BlockState> blocks) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCancelled(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerList getHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

}

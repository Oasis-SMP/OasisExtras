package net.charter.orion_pax.OasisExtras;

import net.minecraft.server.v1_7_R1.ContainerAnvil;
import net.minecraft.server.v1_7_R1.EntityHuman;

public class VirtualAnvil extends ContainerAnvil {
	 
    public VirtualAnvil(EntityHuman entity) {
        super(entity.inventory, entity.world, 0, 0, 0, entity);
    }
 
    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }
}

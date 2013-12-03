package net.charter.orion_pax.OasisExtras;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
 
public class SerializedLocation implements Serializable {
    private static final long serialVersionUID = -9094035533656633605L;
   
    private final String WORLDNAME;
    private final double X;
    private final double Y;
    private final double Z;

    public SerializedLocation(Location location) {
        this.WORLDNAME = location.getWorld().getName();
        this.X        = location.getX();
        this.Y        = location.getY();
        this.Z        = location.getZ();

    }
   
    public Location deserialize() {
        return new Location(Bukkit.getWorld(this.WORLDNAME), this.X, this.Y, this.Z);
    }
}
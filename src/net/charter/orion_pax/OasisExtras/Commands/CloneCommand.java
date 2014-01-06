package net.charter.orion_pax.OasisExtras.Commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.charter.orion_pax.OasisExtras.OasisExtras;
import net.minecraft.server.v1_7_R1.PacketPlayOutNamedEntitySpawn;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CloneCommand implements CommandExecutor {

	private OasisExtras plugin;
	public CloneCommand(OasisExtras plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		CraftPlayer p = (CraftPlayer) player;
		
		PacketPlayOutNamedEntitySpawn npc = new PacketPlayOutNamedEntitySpawn(p.getHandle());

		//the a field used to be public, we'll need to use reflection to access:
		try {
			Field field = npc.getClass().getDeclaredField("a");
			field.setAccessible(true);// allows us to access the field
			field.setInt(npc, 123);// sets the field to an integer
			field.setAccessible(!field.isAccessible());//we want to stop accessing this now
			if(args.length==1){
				field = npc.getClass().getDeclaredField("b");
				field.setAccessible(true);
				Object gameProfile = field.get(npc);
				Field name = gameProfile.getClass().getDeclaredField("name");
				name.setAccessible(true);
				name.set(gameProfile, args[0]);
				name.setAccessible(false);
				field.setAccessible(false);
			}
		} catch(Exception x) {
			x.printStackTrace();
		}
		p.getHandle().playerConnection.sendPacket(npc);
		return true;
	}

}

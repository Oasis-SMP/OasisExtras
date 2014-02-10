package net.charter.orion_pax.OasisExtras;

import net.minecraft.server.v1_7_R1.NetworkManager;
import java.lang.reflect.Field;

public class FixedNetworkManager extends NetworkManager {

	public FixedNetworkManager() {
		super(false);
		this.swapFields();
	}

	protected void swapFields() {
		try {
			Field channelField = NetworkManager.class.getDeclaredField("k");
			Field addressField = NetworkManager.class.getDeclaredField("l");
			
			channelField.setAccessible(true);
			addressField.setAccessible(true);
			
			channelField.set(this, new NullChannel(null));
			addressField.set(this, null);
			
			channelField.setAccessible(true);
			addressField.setAccessible(true);
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

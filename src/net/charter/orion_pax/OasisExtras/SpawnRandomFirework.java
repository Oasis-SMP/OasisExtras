package net.charter.orion_pax.OasisExtras;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class SpawnRandomFirework {

	private OasisExtras plugin;
	private static Player player;
	public SpawnRandomFirework(OasisExtras plugin, String name) {
		this.plugin = plugin;
		this.player = plugin.getServer().getPlayer(name);
		spawnFireWork();
	}
	
	public SpawnRandomFirework(OasisExtras plugin){
		this.plugin = plugin;
		
	}
	
	public static FireworkEffect randomEffect(){
		//Get random type
		Type type = getType();

		//Get our random colours 
		Color c1 = getColor();
		Color c2 = getColor();

		//Create our effect with this
		return FireworkEffect.builder().flicker(getRanBoolean()).withColor(c1).withFade(c2).with(type).trail(getRanBoolean()).build();
	}
	
	private Firework spawnFireWork(){
		Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		//Our random generator

		//Get random type
		Type type = getType();

		//Get our random colours 
		Color c1 = getColor();
		Color c2 = getColor();

		//Create our effect with this
		FireworkEffect effect = FireworkEffect.builder().flicker(getRanBoolean()).withColor(c1).withFade(c2).with(type).trail(getRanBoolean()).build();

		//Then apply the effect to the meta
		fwm.addEffect(effect);

		//Generate some random power and set it
		fwm.setPower(randomNum(1,3));

		//Then apply this to our rocket
		fw.setFireworkMeta(fwm);
		return fw;
	}
	
	private static boolean getRanBoolean(){
		switch(randomNum(1,2)){
		case 1:return true;
		case 2:return false;
		default:return true;
		}
	}
	
	private static Type getType(){
		switch(randomNum(1,5)){
		case 1:return Type.BALL;
		case 2:return Type.BALL_LARGE;
		case 3:return Type.BURST;
		case 4:return Type.CREEPER;
		case 5:return Type.STAR;
		default:return Type.BALL;
		}
	}
	
	public static int randomNum(Integer lownum, double d) {
		return lownum + (int)(Math.random() * ((d - lownum) + 1));
	}

	private static Color getColor() {
		switch(randomNum(1,17)){
		case 1:return Color.AQUA;
		case 2:return Color.BLACK;
		case 3:return Color.BLUE;
		case 4:return Color.FUCHSIA;
		case 5:return Color.GRAY;
		case 6:return Color.GREEN;
		case 7:return Color.LIME;
		case 8:return Color.MAROON;
		case 9:return Color.NAVY;
		case 10:return Color.OLIVE;
		case 11:return Color.ORANGE;
		case 12:return Color.PURPLE;
		case 13:return Color.RED;
		case 14:return Color.SILVER;
		case 15:return Color.TEAL;
		case 16:return Color.WHITE;
		case 17:return Color.YELLOW;
		default:return Color.AQUA;
		}
	}

}

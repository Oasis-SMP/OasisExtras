package net.charter.orion_pax.OasisExtras;

public enum ArrowType {
	EXPLOSIVE(1, "explosive","Explosive arrows!"),
	POISON(2, "poison","Poison arrows!"),
	BLINDNESS(3,"blindness","Blindness arrows!"),
	DRUNK(4,"drunk","Confusion arrows!"),
	WEB(5,"web","Web arrows!"),
	SOUL(6,"soul","Soulsand arrows!"),
	SAND(7,"sand","Sand arrows!"),
	FIREWORKS(8,"fireworks","Fireworks arrows!"),
	LIGHTNING(9,"lightning","Lightning arrows!"),
	FREEZE(10,"freeze","Freeze arrows!"),
	NONE(11,"none","Normal arrows!");
	
	private int place;
	private String name;
	private String description;
	
	private ArrowType(int place, String name, String description){
		this.place = place;
		this.name = name;
		this.description = description;
	}
}

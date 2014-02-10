package net.charter.orion_pax.OasisExtras.Map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

public class ImgUtility
{

	// Vérifie que c'est bien un joueur qui exécute la commande
	static boolean VerifierIdentite(CommandSender sender)
	{
		if (sender instanceof Player)
		{
			return true;
		}
		else if (sender instanceof ConsoleCommandSender)
		{System.out.println(ChatColor.RED + "This command may not be used in the console!"); return false;}
		else if (sender instanceof BlockCommandSender)
		{System.out.println(ChatColor.RED + "This command can not be used by a command block!"); return false;}
		else
		{System.out.println(ChatColor.RED + "Cette commande ne peut être lancée de cette façon !"); return false;}
	}

	// Creation du dossier où sera stocké les images
	public static boolean CreateImageDir(OasisExtras plugin)
	{
		File dossier;
		dossier = new File(plugin.getDataFolder().getPath() + "/Image");
		if (!dossier.exists())
		{
			return dossier.mkdirs();
		}
		else
			return true;
	}

	static int getNombreDeMaps(OasisExtras plugin)
	{
		int nombre = 0;
		Set<String> cle = plugin.maps.getConfig().getKeys(false);
		for (String s: cle)
		{
			if(plugin.maps.getConfig().getStringList(s).size() >= 3)
			{
				nombre++;
			}
		}
		return nombre;
	}

	static int getNombreDeMapsParJoueur(OasisExtras plugin, String pseudo)
	{
		int nombre = 0;
		Set<String> cle = plugin.maps.getConfig().getKeys(false);
		for (String s: cle)
		{
			if(plugin.maps.getConfig().getStringList(s).size() >= 3 && plugin.maps.getConfig().getStringList(s).get(2).contentEquals(pseudo))
			{
				nombre++;
			}
		}
		return nombre;
	}

	static boolean EstDansFichier(OasisExtras plugin, short id)
	{
		Set<String> cle = plugin.maps.getConfig().getKeys(false);
		for (String s: cle)
		{
			if(plugin.maps.getConfig().getStringList(s).size() >= 3 && Short.parseShort(plugin.maps.getConfig().getStringList(s).get(0)) == id)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean EstDansFichier(OasisExtras plugin, short id, String pseudo)
	{
		Set<String> cle = plugin.maps.getConfig().getKeys(false);
		for (String s: cle)
		{
			if(plugin.maps.getConfig().getStringList(s).size() >= 3 && Short.parseShort(plugin.maps.getConfig().getStringList(s).get(0)) == id && plugin.maps.getConfig().getStringList(s).get(2).contentEquals(pseudo))
			{
				return true;
			}
		}
		return false;
	}

	// Fait la même chose que EstDansFichier() mais en retournant un objet MapView
	@SuppressWarnings("deprecation")
	public
	static MapView getMap(OasisExtras plugin, short id)
	{
		MapView map;
		if(!ImgUtility.EstDansFichier(plugin, id))
		{
			return null;
		}


		map = Bukkit.getMap(id);
		if(map == null)
		{
			plugin.getLogger().warning("Map#"+ id+ " exists in maps.yml but not in the world folder !");
			return null;
		}

		return map;
	}

	public static boolean RemoveMap(OasisExtras plugin, short id)
	{
		@SuppressWarnings("deprecation")
		MapView carte = Bukkit.getMap(id);

		Set<String> cle = plugin.maps.getConfig().getKeys(false);
		plugin.getLogger().info("POINT 2");
		for (String s: cle)
		{
			if(plugin.maps.getConfig().getStringList(s).size() >= 3)
			{
				if(carte == null && id == Short.parseShort(plugin.maps.getConfig().getStringList(s).get(0)))
				{
					//joueur.sendMessage("Suppression de la map dans fichier conf");
					plugin.maps.getConfig().set(s, null);
					plugin.maps.saveConfig();
					File map = new File("./plugins/OasisExtras/Image/" + s + ".png");
					boolean isDeleted = map.delete();
					//joueur.sendMessage("The picture have been deleted");

					if(isDeleted)
						return true;
					else
					{
						plugin.getLogger().warning("Picture "+ s+ ".png cannot be deleted !");
						return false;
					}
				}

				else if(id == Short.parseShort(plugin.maps.getConfig().getStringList(s).get(0)))
				{
					//joueur.sendMessage("Suppression de la map dans fichier conf + fichier dat");
					ImageRenderer.SupprRendu(carte);
					plugin.maps.getConfig().set(s, null);
					plugin.maps.saveConfig();
					File map = new File("./plugins/OasisExtras/Image/" + s + ".png");
					boolean isDeleted = map.delete();
					//joueur.sendMessage("DEBUG: booléen isDeleted :"+ isDeleted+ "; Nom de la map : "+ plugin.getServer().getWorlds().get(0).getName());
					//joueur.sendMessage("The map has been deleted");
					if(isDeleted)
						return true;
					else
					{
						plugin.getLogger().warning("Picture "+ s+ ".png cannot be deleted !");
						return false;
					}

				}
			}
		}

		plugin.getLogger().info("No map with id"+ id+ " was found");
		return false;
	}

	public static ArrayList<String> getListMapByPlayer(OasisExtras plugin, String pseudo)
	{
		ArrayList<String> listeMap = new ArrayList<String>();
		Set<String> cle = plugin.maps.getConfig().getKeys(false);
		for (String s: cle)
		{
			if(plugin.maps.getConfig().getStringList(s).size() >= 3 && pseudo.equalsIgnoreCase(plugin.maps.getConfig().getStringList(s).get(2)))
			{
				listeMap.add(plugin.maps.getConfig().getStringList(s).get(0));
			}
		}
		return listeMap;
	}

	static void AddMap(ItemStack map, Inventory inv, ArrayList<ItemStack> restant)
	{
		HashMap<Integer,ItemStack> reste = inv.addItem(map);

		if(!reste.isEmpty())
		{
			restant.add(reste.get(0));
		}
	}
}
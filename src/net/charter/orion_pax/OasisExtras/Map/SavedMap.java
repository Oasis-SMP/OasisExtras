package net.charter.orion_pax.OasisExtras.Map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

public class SavedMap
{
	OasisExtras plugin;
	String nomImg, nomJoueur;
	short idMap;
	BufferedImage image;

	SavedMap(OasisExtras plug, String nomJ, short id, BufferedImage img)
	{
		plugin = plug;
		nomJoueur = nomJ;
		idMap = id;
		image = img;
		nomImg = "map" + id;
	}

	public SavedMap(OasisExtras plug, short id)
	{
		idMap = id;
		plugin = plug;
		Set<String> cle = plugin.maps.getConfig().getKeys(false);
		int tmp = 0;
		for (String s: cle)
		{
			if(plugin.maps.getConfig().getStringList(s).size() >= 3 && Short.valueOf(plugin.maps.getConfig().getStringList(s).get(0)) == id)
			{
				tmp++;
				//System.out.println(tmp);
				//MapView carte = Bukkit.getMap(Short.parseShort(plugin.getConfig().getStringList(s).get(0)));
				nomImg = plugin.maps.getConfig().getStringList(s).get(1);
				nomJoueur = plugin.maps.getConfig().getStringList(s).get(2);
				try {
					image = ImageIO.read(new File("./plugins/OasisExtras/Image/"+ nomImg + ".png"));
					//System.out.println("Chargement de l'image fini");
				} catch (IOException e) {
					System.out.println("Image "+ nomImg +".png doesn't exists in Image directory.");
				}
			}
		}
		if(tmp == 0)
		{
			System.out.println("No map was loaded");
		}
	}

	Boolean SaveMap()
	{
		System.out.println("Saving map " + idMap);

		// Enregistrement de l'image sur le disque dur
		try
		{
			File outputfile = new File("./plugins/OasisExtras/Image/"+ nomImg + ".png");
			ImageIO.write(MapPalette.resizeImage(image), "png", outputfile);
		} catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		// Enregistrement de la map dans la config
		ArrayList<String> liste = new ArrayList<String>();
		liste.add(String.valueOf(idMap));
		liste.add(nomImg);
		liste.add(nomJoueur);
		plugin.maps.getConfig().set(nomImg, liste);
		plugin.maps.saveConfig();
		return true;
	}

	@SuppressWarnings("deprecation")
	public
	Boolean LoadMap()
	{
		MapView carte = Bukkit.getMap(idMap);
		if(carte != null)
		{
			ImageRenderer.SupprRendu(carte);
			carte.addRenderer(new Rendu(image));
			return true;
		}
		else
			return false;
	}

}
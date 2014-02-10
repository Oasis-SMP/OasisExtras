package net.charter.orion_pax.OasisExtras.Map;

import java.util.ArrayList;

import net.charter.orion_pax.OasisExtras.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;

public class SpotTreatementMap extends BukkitRunnable
{
	int i;
	Player joueur;
	ImageRenderer renduImg;
	PlayerInventory inv;
	ItemStack map;
	OasisExtras plugin;

	public SpotTreatementMap(Player j, String u, OasisExtras plug)
	{
		i = 0;
		joueur = j;
		renduImg = new ImageRenderer(u);
		renduImg.start();
		inv = joueur.getInventory();
		plugin = plug;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
		if(!renduImg.getStatut())
		{
			//joueur.sendMessage("Nombre d'ex�cution depuis le lancement du timer : " + i);
			i++;
			if(i > 42)
			{
				joueur.sendMessage("TIMEOUT: the render took too much time");
				cancel();
			}
		}
		else
		{
			cancel();
			int nbImage = renduImg.getImg().getPoster().length;
			
			MapView carte;

			ArrayList<ItemStack> restant = new ArrayList<ItemStack>();
			for (int i = 0; i < nbImage; i++)
			{
				carte = Bukkit.createMap(joueur.getWorld());
				ImageRenderer.SupprRendu(carte);
				carte.addRenderer(new Rendu(renduImg.getImg().getPoster()[i]));
				map = new ItemStack(Material.MAP, 1, carte.getId());
				ItemMeta meta = map.getItemMeta();
				meta.setDisplayName("Map (" +renduImg.getImg().NumeroMap.get(i) +")");
				map.setItemMeta(meta);

				ImgUtility.AddMap(map, inv, restant);

				//Svg de la map
				SavedMap svg = new SavedMap(plugin, joueur.getName(), carte.getId(), renduImg.getImg().getPoster()[i]);
				svg.SaveMap();
				joueur.sendMap(carte);
			}
			if(!restant.isEmpty())
				joueur.sendMessage(restant.size()+ " maps can't be place in your inventory. Please make free space in your inventory and run "+ ChatColor.GOLD+ "/maptool rest");
			plugin.setRemainingMaps(joueur.getName(), restant);
			joueur.sendMessage("Render finished");
		}
	}

}

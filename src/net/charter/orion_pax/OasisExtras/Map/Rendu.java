package net.charter.orion_pax.OasisExtras.Map;

import java.awt.Image;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;


public class Rendu extends MapRenderer implements Runnable
{

	boolean estRendu;
	Image touhou;
	private Thread TRendu;
	public MapCanvas canvas;

	public Rendu(Image img)
	{
		estRendu = false;
		touhou = img;
	}

	@Override
	public void render(MapView v, final MapCanvas mc, Player p)
	{
		canvas = mc;


		if (!estRendu) // Si la map a d�j� �t� rendu, on n'entre plus dans la fonction, ce qui �vite de surcharger le serveur
		{
			// On instancie et d�marre le thread de rendu
			TRendu = new Thread(this);
			TRendu.start();
			estRendu = true;
		}
	}

	// Le chargement et le rendu de l'image se font dans un thread afin d'�viter le lag..
	@Override
	public void run()
	{
		// on dessine l'image redimensionn�e dans le canvas (et donc, sur la map !)
		canvas.drawImage(0, 0, touhou);

	}
}
package net.charter.orion_pax.OasisExtras.Map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;

import org.bukkit.map.MapView;


public class ImageRenderer extends Thread
{
	private String URL;
	private BufferedImage imgSrc;
	private Poster img;
	private boolean estPrete = false;
	boolean erreur = false;

	ImageRenderer(String u)
	{
		URL = u;
	}

	public Poster getImg()
	{
		if (estPrete)
			return img;
		else
			return null;
	}



	public Boolean getStatut()
	{
		return estPrete;
	}

	@Override
	public void run()
	{
		try
		{
			imgSrc = ImageIO.read(URI.create(URL).toURL().openStream());
			int width = imgSrc.getWidth();
			int height = imgSrc.getHeight();

			// Fonction qui cherche le multiple de 128 le plus proche
			// de la hauteur / largeur de l'image
			int tmpW = 0, tmpH = 0;
			int i = 1;
			while (tmpW < width)
			{

				tmpW = i * 128;
				i++;
			}

			i = 0;
			while (tmpH < height)
			{

				tmpH = i * 128;
				i++;
			}

			// On cr�e un "canvas" = une image vide qui a une taille multiple de 128
			// dans laquelle on dessinera l'image t�l�charg�es
			BufferedImage canvas = new BufferedImage(tmpW, tmpH, BufferedImage.TYPE_INT_ARGB);
			// On r�cup�re l'objet Grapics2D, servant � dessiner dans notre canvas
			Graphics2D graph = canvas.createGraphics();

			// Variable servant � cadrer l'image
			int centerX = 0, centerY = 0;
			centerX = (tmpW - imgSrc.getWidth()) / 2;
			centerY = (tmpH - imgSrc.getHeight()) / 2;
			//On d�place le point d'origine de graph afin que l'image soit dessin�e au milieu du canvas
			graph.translate(centerX, centerY);
			//graph.rotate(45);
			// on dessine l'image dans le canvas
			graph.drawImage(imgSrc, null, null);
			// on cr�e un Poster � partir de notre canvas
			img = new Poster(canvas);
		}
		catch (IOException e) {
			e.printStackTrace();
			erreur = true;
		}

		estPrete = true;

	}

	static void SupprRendu(MapView map)
	{
		if (map.getRenderers().size() > 0)
		{
			int i = 0, t = map.getRenderers().size();
			while (i < t)
			{
				map.removeRenderer(map.getRenderers().get(i));
				i++;
			}
		}
	}

}
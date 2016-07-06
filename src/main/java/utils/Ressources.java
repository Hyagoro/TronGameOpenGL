package utils;

import java.awt.Color;

import org.newdawn.slick.Font;

public abstract class Ressources 
{
	public static final String spriteJeu = "Images/spriteTextures.png";
	public static final int tailleBloc = 10;
	public static final int port = 54555;
	
	public static String adresseIP = "localhost";
	public static Font font;
	public static String nickname = "TITI";
	public static Color color = new Color(0,102,255);
	public static int fps;
	
	
	public static void setFont(Font font)
	{
		Ressources.font = font;
	}
}

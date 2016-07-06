package controler;

import java.util.LinkedList;

import ui.Bloc;
import ui.BlocMur;
import ui.Map;
import ui.Player;

public class ControleurColision
{
    public static void analyserDeplacements(Map carte, LinkedList<Player> joueurs, Player joueur)
    {

	double positionX = joueur.getX();
	double positionY = joueur.getY();

	Bloc caseC = carte.getCase((int) positionX, (int) positionY);
	if (joueur.isAlive())
	{
	    if (caseC != null)
	    {
		if (caseC instanceof BlocMur)
		{
		    joueur.setAlive(false);
		}
	    }
	}

	for (int i = 0; i < joueurs.size(); i++)
	{
	    if (joueurs.get(i).isBlocMur((int) positionX, (int) positionY))
	    {
		if (joueur.isAlive())
		{
		    joueur.setAlive(false);
		}
	    }
	}
    }
}

package controler;

import ui.Bloc;
import ui.BlocMur;
import ui.Map;
import ui.Player;

import java.util.LinkedList;

public class ControleurColision {
    public static void analyserDeplacements(Map carte, LinkedList<Player> joueurs, Player joueur) {

        double positionX = joueur.getX();
        double positionY = joueur.getY();

        Bloc caseC = carte.getCase((int) positionX, (int) positionY);
        if (joueur.isAlive()) {
            if (caseC != null) {
                if (caseC instanceof BlocMur) {
                    joueur.setAlive(false);
                }
            }
        }

        for (Player joueur1 : joueurs) {
            if (joueur1.isBlocMur((int) positionX, (int) positionY)) {
                if (joueur.isAlive()) {
                    joueur.setAlive(false);
                }
            }
        }
    }
}

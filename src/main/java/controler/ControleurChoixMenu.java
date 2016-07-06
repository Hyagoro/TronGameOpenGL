package controler;

import ui.*;

public class ControleurChoixMenu {
    public static Action analyserDeplacements(Map carte, Player joueur) {
        double positionX = joueur.getX();
        double positionY = joueur.getY();

        Bloc caseC = carte.getCase((int) positionX, (int) positionY);
        if (joueur.isAlive()) {
            if (caseC != null) {
                if (caseC instanceof BlocMur) {
                    joueur.setAlive(false);
                } else if (caseC instanceof BlocCreux) {
                    return ((BlocCreux) caseC).getAction();
                }
            }
        } else {
            joueur.reset(carte.getMatrice().getTailleX() / 2, carte.getMatrice().getTailleY() / 2);
        }
        return Action.AUCUNE;
    }
}

package modele;

public enum Direction {
    NORD, SUD, EST, OUEST, AUCUNE;

    public boolean isOppose(Direction dir) {
        if (this == NORD && dir == SUD) {
            return true;
        } else if (this == SUD && dir == NORD) {
            return true;
        } else if (this == EST && dir == OUEST) {
            return true;
        } else if (this == OUEST && dir == EST) {
            return true;
        } else
            return false;
    }
}

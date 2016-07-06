package ui;

import org.lwjgl.util.Color;

public class Matrice {
    private int tailleX;
    private int tailleY;
    private Bloc[][] matrice;

    public Matrice(int tailleX, int tailleY) {
        this.tailleX = tailleX;
        this.tailleY = tailleY;
        initialiserMatrice();
    }

    public void initialiserMatrice() {
        matrice = new Bloc[tailleX][tailleY];
    }

    public void render() {
        for (int i = 0; i < tailleX; i++) {
            for (int j = 0; j < tailleY; j++) {
                if (matrice[i][j] != null) {
                    matrice[i][j].render();
                }
            }
        }
    }

    public void addBlocMur(int x, int y, Color color, TypeMur type, Orientation orientation) {
        matrice[x][y] = new BlocMur(x, y, color, type, orientation);
    }

    public Bloc getEntite(int x, int y) {
        return matrice[x][y];
    }

    public void setEntite(int x, int y, Bloc element) {
        matrice[x][y] = element;
    }

    public void setEntite(Bloc element) {
        matrice[(int) element.getX()][(int) element.getY()] = element;
    }

    public Bloc[][] getMatrice() {
        return matrice;
    }

    public void setMatrice(Bloc[][] matrice) {
        this.matrice = matrice;
    }

    public int getTailleX() {
        return tailleX;
    }

    public int getTailleY() {
        return tailleY;
    }

    public void setTailleX(int tailleX) {
        this.tailleX = tailleX;
    }

    public void setTailleY(int tailleY) {
        this.tailleY = tailleY;
    }

}

package utils;

public class MyChar {
    private Character lettre;

    public MyChar(Character lettre) {
        this.lettre = lettre;
    }

    public void draw() {

    }

    public float getOffset() {
        return 8f;
    }

    public Character getLettre() {
        return lettre;
    }

    public void setLettre(Character lettre) {
        this.lettre = lettre;
    }
}

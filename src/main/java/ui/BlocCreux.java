package ui;

import org.lwjgl.util.Color;

public class BlocCreux extends Bloc {
    private Action action;

    public BlocCreux(double x, double y, Color color, Action action) {
        super(x, y, color);
        this.action = action;
        // TODO Auto-generated constructor stub
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

}

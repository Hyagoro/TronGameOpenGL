package ui;

import org.lwjgl.util.Color;

public class BlocMur extends Bloc
{    
    private TypeMur type;
    private Orientation orientation;
    
    public BlocMur(double x, double y, Color color, TypeMur type, Orientation orientation)
    {
	super(x, y, color);
	this.type = type;
	this.orientation = orientation;
    }

    public TypeMur getType()
    {
	return type;
    }

    public void setType(TypeMur type)
    {
	this.type = type;
    }

    public Orientation getOrientation()
    {
	return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
	this.orientation = orientation;
    }


}

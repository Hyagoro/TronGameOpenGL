package ui;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.Color;

public class Map
{
    public enum Type
    {
	MENU, GAME;
    }

    private int tailleX;
    private int tailleY;
    private Type type;

    private Matrice matrice;
    private Color color;

    private boolean once = true;
    private int vbo_vertex_handle;
    private int vbo_color_handle;

    public Map(int tailleX, int tailleY, Type type)
    {
	this.tailleX = tailleX;
	this.tailleY = tailleY;
	this.type = type;

	this.color = new Color(230, 230, 230);
	matrice = new Matrice(tailleX, tailleY);

	initialiser();
    }

    private void ajouterElemMatrice(Bloc cc)
    {
	// carte.add(cc);
	matrice.setEntite(cc);
    }

    private void renderSupport()
    {
	int vertices = 4;
	int vertex_size = 3; // XYZ
	int color_size = 3; // RGB

	if (once)
	{
	    // TODO
	    // float r = (float) color.getRed() / 255f;
	    // float g = (float) color.getGreen() / 255f;
	    // float b = (float) color.getBlue() / 255f;

	    float x = new Float(tailleX / 2);
	    float y = new Float(tailleY / 2);
	    float z = new Float(-5);

	    FloatBuffer vertex_data = BufferUtils.createFloatBuffer(vertices * vertex_size);
	    // dessus
	    vertex_data.put(new float[] { x + tailleX / 2, y - tailleY / 2, z });
	    vertex_data.put(new float[] { x + tailleX / 2, y + tailleY / 2, z });
	    vertex_data.put(new float[] { x - tailleX / 2, y + tailleY / 2, z });
	    vertex_data.put(new float[] { x - tailleX / 2, y - tailleY / 2, z });
	    vertex_data.flip();

	    FloatBuffer color_data = BufferUtils.createFloatBuffer(vertices * color_size);

	    color_data.put(new float[] { 0.1f, 0.1f, 0.1f });
	    color_data.put(new float[] { 0.1f, 0.1f, 0.1f });
	    color_data.put(new float[] { 0.1f, 0.1f, 0.1f });
	    color_data.put(new float[] { 0.1f, 0.1f, 0.1f });
	    color_data.flip();

	    vbo_vertex_handle = GL15.glGenBuffers();
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_vertex_handle);
	    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex_data, GL15.GL_STATIC_DRAW);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	    vbo_color_handle = GL15.glGenBuffers();
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_color_handle);
	    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, color_data, GL15.GL_STATIC_DRAW);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_vertex_handle);
	GL11.glVertexPointer(vertex_size, GL11.GL_FLOAT, 0, 0l);

	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_color_handle);
	GL11.glColorPointer(color_size, GL11.GL_FLOAT, 0, 0l);

	GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
	GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);

	GL11.glDrawArrays(GL11.GL_QUADS, 0, vertices);

	GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
	GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }

    public void render()
    {
	renderSupport();
	matrice.render();
    }

    public Bloc getCase(int x, int y)
    {
	if (matrice.getEntite(x, y) != null)
	    return (Bloc) matrice.getEntite(x, y);
	else
	    return null;
    }

    private void initialiser()
    {
	int tailleBloc = 10;

	for (int i = 0; i < tailleX; i += tailleBloc)
	{
	    for (int j = 0; j < tailleY; j += tailleBloc)
	    {
		if (i == 0 || i == tailleX - tailleBloc) // horizontal
		{
		    Bloc cc = new BlocMur(i, j, color, TypeMur.MURMONDE, Orientation.NONE);
		    ajouterElemMatrice(cc);
		}
		else if (j == 0 || j == tailleY - tailleBloc) // vertical
		{
		    Bloc cc = new BlocMur(i, j, color, TypeMur.MURMONDE, Orientation.NONE);
		    ajouterElemMatrice(cc);
		}

		if (type.equals(Type.MENU))
		{
		    if (j == tailleY / 2)
		    {
			if (i == tailleBloc)
			{
			    Bloc cc = new BlocCreux(i, j, new Color(0, 255, 100, 120), Action.MENUCLIENT);
			    ajouterElemMatrice(cc);
			}
			else if( i == tailleX - (2 * tailleBloc))
			{
			    Bloc cc = new BlocCreux(i, j, new Color(0, 255, 100, 120), Action.MENUSERVEUR);
			    ajouterElemMatrice(cc);
			}
		    }
		}
	    }
	}
    }

    public Matrice getMatrice()
    {
	return matrice;
    }

    public void setMatrice(Matrice matrice)
    {
	this.matrice = matrice;
    }

    public Type getType()
    {
	return type;
    }

    public void setType(Type type)
    {
	this.type = type;
    }
}

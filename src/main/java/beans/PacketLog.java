package beans;

public class PacketLog
{
    private String nom;
    private int r, g, b;

    public PacketLog()
    {

    }

    public int getR()
    {
	return r;
    }

    public int getG()
    {
	return g;
    }

    public int getB()
    {
	return b;
    }

    public void setR(int r)
    {
	this.r = r;
    }

    public void setG(int g)
    {
	this.g = g;
    }

    public void setB(int b)
    {
	this.b = b;
    }

    public String getNom()
    {
	return nom;
    }

    public void setNom(String nom)
    {
	this.nom = nom;
    }

}

package beans;

public class PacketMAJJoueurs 
{
	private int nombreJoueurs;
	private int r,g,b;
	private int ID;

	public PacketMAJJoueurs()
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
	public int getNombreJoueurs() 
	{
		return nombreJoueurs;
	}

	public void setNombreJoueurs(int nombreJoueurs) 
	{
		this.nombreJoueurs = nombreJoueurs;
	}

	public int getID()
	{
		return ID;
	}

	public void setID(int iD) 
	{
		ID = iD;
	}
}

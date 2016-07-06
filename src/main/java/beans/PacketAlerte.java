package beans;

public class PacketAlerte 
{
	private String alerte;
	
	public PacketAlerte(String alerte)
	{
		this.alerte = alerte;
	}
	
	public PacketAlerte()
	{
		
	}

	public PacketAlerte(PacketAlerte object) 
	{
		this.alerte = new String(object.alerte);
	}

	public String getAlerte() 
	{
		return alerte;
	}

	public void setAlerte(String alerte) 
	{
		this.alerte = alerte;
	}
}

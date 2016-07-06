package beans;

public class PacketLogBack 
{
	private String message;
	private int xCarte;
	private int yCarte;
	
	public PacketLogBack ()
	{
		
	}
	public String getMessage() 
	{
		return message;
	}
	public void setMessage(String message) 
	{
		this.message = message;
	}
	public int getxCarte() 
	{
		return xCarte;
	}
	public int getyCarte() 
	{
		return yCarte;
	}
	public void setxCarte(int xCarte) 
	{
		this.xCarte = xCarte;
	}
	public void setyCarte(int yCarte) 
	{
		this.yCarte = yCarte;
	}
}

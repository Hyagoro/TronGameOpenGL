package controler;

import org.lwjgl.input.Keyboard;

import modele.Direction;

public class EventMoto 
{
	private boolean eventConsumed;
	private Direction directionPrecedente;
	private Direction directionPrecedentePersistante;
	
	public EventMoto()
	{
		directionPrecedente = Direction.AUCUNE;
		directionPrecedentePersistante = Direction.AUCUNE;
		eventConsumed = true;
	}
	
	public Direction appliquerDirection(Direction d)
	{	
		directionPrecedentePersistante = directionPrecedente;
		if(d != directionPrecedente)
		{
			directionPrecedente = d;
			eventConsumed = false;
		}
		eventConsumed = false;
		return d;
	}

	public Direction getEvent()
	{
		Direction tmp = directionPrecedente;

		if(directionPrecedente != Direction.NORD && directionPrecedente != Direction.SUD)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			{
				tmp = Direction.SUD;
			}
		}
		if(directionPrecedente != Direction.EST && directionPrecedente != Direction.OUEST)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			{
				tmp = Direction.OUEST;
			}
		}
		if(directionPrecedente != Direction.OUEST && directionPrecedente != Direction.EST)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			{
				tmp = Direction.EST;
			}
		}
		if(directionPrecedente != Direction.SUD && directionPrecedente != Direction.NORD)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_UP))
			{
				tmp = Direction.NORD;		
			}
		}
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_M))
		{
			tmp = Direction.AUCUNE;
		}
		return appliquerDirection(tmp);
	}

	
	public boolean isEventConsumed()
	{
		return eventConsumed;
	}
	public void setEventConsumed(boolean eventConsumed) 
	{
		this.eventConsumed = eventConsumed;
	}
	public Direction getDirectionPrecedente() 
	{
		return directionPrecedente;
	}

	public Direction getDirectionPrecedentePersistante() 
	{
		return directionPrecedentePersistante;
	}

	public void setDirectionPrecedentePersistante(
			Direction directionPrecedentePersistante)
	{
		this.directionPrecedentePersistante = directionPrecedentePersistante;
	}
}

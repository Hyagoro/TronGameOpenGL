package controler;

import modele.Camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class ControleurCamera
{
    public static void updateCameraOnEvent(Camera camera)
    {

	if (Mouse.isButtonDown(0))
	{
	    int x = Mouse.getX();
	    int y = Mouse.getY();

	    System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
	}
	if (Mouse.hasWheel())
	{
	    camera.setEyez(camera.getEyez() + -Mouse.getDWheel() / 10);
	    // System.out.println(camera.toString());
	    // camera.setUpz(camera.getUpz() + );
	    // System.out.println("MOUSE Wheel: " +Mouse.getDWheel());
	}
	
//	if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
//	{
//	    System.out.println("SPACE KEY IS DOWN");
//	}
	if (Keyboard.isKeyDown(Keyboard.KEY_8))
	{
	    camera.setEyex(camera.getEyex() + 10);
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_2))
	{
	    camera.setEyex(camera.getEyex() - 10);
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_6))
	{
	    camera.setEyey(camera.getEyey() + 10);
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_4))
	{
	    camera.setEyey(camera.getEyey() - 10);
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_O))
	{
	    camera.setEyez(camera.getEyez() - 10);
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_P))
	{
	    camera.setEyez(camera.getEyez() + 10);
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_Z))
	{
	    camera.setEyez(camera.getEyez() - 10);
	}
    }
}

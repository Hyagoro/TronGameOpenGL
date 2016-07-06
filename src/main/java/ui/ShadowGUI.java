package ui;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.opengl.ARBDepthTexture;
import org.lwjgl.opengl.ARBShadow;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.*;

public class ShadowGUI
{
    public static int width = 800, height = 600;
    protected static final String TITLE = "Shadow Example";
    static boolean running = true;
    final public static int FRAME_RATE = 64;
    static int spheresList, baseList;
    static int shadowMapTexture;
    static int shadowMapSize = 512;
    static Vector3f cameraPosition = new Vector3f(0, 3.5f, -3f);
    static FloatBuffer lightPosition = floatBuffer(0f, 1.6f, 1.0f, 1 );
    static FloatBuffer cameraViewMatrix, lightProjectionMatrix, cameraProjectionMatrix, lightViewMatrix;
    static FloatBuffer white = floatBuffer( 1.0f, 1.0f, 1.0f, 1.0f );
    static FloatBuffer dim = floatBuffer( 0.2f, 0.2f, 0.2f, 1f );
    static FloatBuffer black = floatBuffer( 0, 0, 0, 1f );
    static float angle;
    static float scale = 1f;
    static FloatBuffer bM =  floatBuffer(new float[] { 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f,
	    0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f });
    static Matrix4f textureMatrix = new Matrix4f();
    static Matrix4f biasMatrix = new Matrix4f();
    static Matrix4f lightP = new Matrix4f(), lightV = new Matrix4f();
    static FloatBuffer tempFloatBuffer = floatBuffer(0,0,0,0);
    static Sphere s = new Sphere();

    public static void main(String args[])
    {
	new ShadowGUI();
    }

    public ShadowGUI()
    {
	try
	{
	    initDisplay(false);
	    initGL();
	    run();
	    cleanup();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	    Sys.alert(TITLE, "Error 101: " + e.toString());
	}
    }
    
    private static FloatBuffer floatBuffer(float a, float b, float c, float d)
    {
	float data[] = new float[]{a,b,c,d};
	FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
	fb.put(data);
	fb.flip();
	return fb;
    }
    
    private static FloatBuffer floatBuffer(float[] a)
    {
	FloatBuffer fb = BufferUtils.createFloatBuffer(a.length);
	fb.put(a);
	fb.flip();
	return fb;
    }

    private static void initDisplay(boolean fullscreen) throws Exception
    {
	Display.setTitle(TITLE); // sets aplication name
	Display.setFullscreen(fullscreen); // create a full screen window if
					   // possible
	DisplayMode displayMode = null;
	DisplayMode d[] = Display.getAvailableDisplayModes();
	for (int i = d.length - 1; i >= 0; i--)
	{
	    displayMode = d[i];
	    if (d[i].getWidth() == width && d[i].getHeight() == height
		    && (d[i].getBitsPerPixel() >= 24 && d[i].getBitsPerPixel() <= 24 + 8)
		    && d[i].getFrequency() == FRAME_RATE)
	    {
		break;
	    }
	} // Finds a suitable resolution
	Display.setDisplayMode(displayMode); // sets display resoulation
	Display.setVSyncEnabled(true); // enable vsync if poosible
	Display.create();
	Display.update();
	Keyboard.enableRepeatEvents(false);
    }

    public static void initGL()
    {
	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	GL11.glLoadIdentity();
	GL11.glShadeModel(GL11.GL_SMOOTH);
	GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	GL11.glClearDepth(1.0f);
	GL11.glDepthFunc(GL11.GL_LEQUAL);
	GL11.glEnable(GL11.GL_CULL_FACE);
	GL11.glEnable(GL11.GL_NORMALIZE);

	// Check for necessary extensions
//	if (!game.util.Util.checkExtensionSupport("GL_ARB_depth_texture")
//		|| !game.util.Util.checkExtensionSupport("GL_ARB_shadow"))
//	{
//	    System.out.println("I require ARB_depth_texture and ARB_shadow extensionsn\n");
//	    System.exit(-1);
//	}
	// Create the shadow map texture
	IntBuffer imageBuffer = BufferUtils.createIntBuffer(1);
	GL11.glGenTextures(imageBuffer);
	shadowMapTexture = imageBuffer.get(0);
	ByteBuffer b = ByteBuffer.allocateDirect(shadowMapSize * shadowMapSize).order(ByteOrder.nativeOrder());
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowMapTexture);
	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, shadowMapSize, shadowMapSize, 0,
		GL11.GL_DEPTH_COMPONENT, GL11.GL_UNSIGNED_BYTE, b);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

	// Use the color as the ambient and diffuse material
	GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
	GL11.glEnable(GL11.GL_COLOR_MATERIAL);

	// White specular material color, shininess 16

	GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, white);
	GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 16.0f);

	// Calculate & save matrices
	GL11.glPushMatrix();
	GL11.glLoadIdentity();
	GLU.gluPerspective(45.0f, (float) width / (float) height, 1.0f, 100.0f);
	cameraProjectionMatrix = BufferUtils.createFloatBuffer(16);
	GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, cameraProjectionMatrix);

	GL11.glLoadIdentity();
	GLU.gluLookAt(cameraPosition.x, cameraPosition.y, cameraPosition.z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
	cameraViewMatrix = BufferUtils.createFloatBuffer(16);
	GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, cameraViewMatrix);

	GL11.glLoadIdentity();
	// lower the fov the higher quality the spot light
	GLU.gluPerspective(90f, 1.0f, 0.01f, 100.0f);
	// USE ORTHO for day light
	// GL11.glOrtho(-1.25, 1.25, -1.25, 1.25, 0.01, 100);
	lightProjectionMatrix = BufferUtils.createFloatBuffer(16);
	GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, lightProjectionMatrix);

	GL11.glLoadIdentity();
	GLU.gluLookAt(lightPosition.get(0), lightPosition.get(1), lightPosition.get(2), 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
		0.0f);
	lightViewMatrix = BufferUtils.createFloatBuffer(16);
	GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, lightViewMatrix);

	GL11.glPopMatrix();

	GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public static void run() throws Exception
    {
	while (running)
	{
	    if (Display.isCloseRequested())
		running = false;
	    if (Display.isActive() && Display.isVisible())
	    {
		logic();
		render();
		Display.sync(FRAME_RATE); // IN CASE OF NO VSYNC
		Display.update();
	    }
	    else
	    {
		Display.sync(FRAME_RATE);
		Display.update();
	    }
	}
    }

    public static void logic()
    {
	angle++;
	angle %= 360;

	int x = Mouse.getX();
	int y = Mouse.getY();

	while (Mouse.next())
	{
	    if (Mouse.getEventButtonState())
	    {
		int mouse = Mouse.getEventButton();
		switch (mouse)
		{
		case 0:
		{
		    System.out.println("" + x + " " + y);

		    break;
		}
		case 1:
		{
		    System.out.println("" + x + " " + y);

		    break;
		}
		case 2:
		{
		    break;
		}
		}
	    }
	}

	while (Keyboard.next())
	{
	    if (Keyboard.getEventKeyState())
	    {
		int key = Keyboard.getEventKey();

		switch (key)
		{
		case Keyboard.KEY_SPACE:
		{

		    break;
		}
		case Keyboard.KEY_RETURN:
		{

		    break;
		}
		}
	    }
	}

	if (Keyboard.isKeyDown(Keyboard.KEY_2))
	{
	    scale += 0.01f;
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_1))
	{
	    scale -= 0.01f;
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_A))
	{
	    lightPosition.put(0, lightPosition.get(0) + 0.1f);
	    updateLightPosition();
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_D))
	{
	    lightPosition.put(0, lightPosition.get(0) - 0.1f);
	    updateLightPosition();
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_W))
	{
	    lightPosition.put(1, lightPosition.get(1) + 0.1f);
	    updateLightPosition();
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_S))
	{
	    lightPosition.put(1, lightPosition.get(1) - 0.1f);
	    updateLightPosition();
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_Q))
	{
	    lightPosition.put(2, lightPosition.get(2) + 0.1f);
	    updateLightPosition();
	}
	if (Keyboard.isKeyDown(Keyboard.KEY_E))
	{
	    lightPosition.put(2, lightPosition.get(2) - 0.1f);
	    updateLightPosition();
	}

    }

    private static void updateLightPosition()
    {

	GL11.glLoadIdentity();
	GL11.glPushMatrix();
	GLU.gluLookAt(lightPosition.get(0), lightPosition.get(1), lightPosition.get(2), 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
		0.0f);
	GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, lightViewMatrix);

	GL11.glPopMatrix();
    }

    public static void render()
    {
	GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear
									   // Screen
									   // And
									   // Depth
									   // Buffer

	// 3D Render Code

	GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadMatrix(lightProjectionMatrix);

	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	GL11.glLoadMatrix(lightViewMatrix);

	// Use viewport the same size as the shadow map
	GL11.glViewport(0, 0, shadowMapSize, shadowMapSize);

	// Draw back faces into the shadow map
	GL11.glCullFace(GL11.GL_FRONT);

	// Disable color writes, and use flat shading for speed
	GL11.glShadeModel(GL11.GL_FLAT);
	// GL11.glColorMask(false, false, false, false);

	// Draw the scene
	DrawScene(angle, false);

	// Read the depth buffer into the shadow map texture
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, shadowMapTexture);
	GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, shadowMapSize, shadowMapSize);

	// restore states
	GL11.glCullFace(GL11.GL_BACK);
	GL11.glShadeModel(GL11.GL_SMOOTH);
	// GL11.glColorMask(true,true,true,true);

	// 2nd pass - Draw from camera's point of view
	GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

	GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadMatrix(cameraProjectionMatrix);

	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	GL11.glLoadMatrix(cameraViewMatrix);

	GL11.glViewport(0, 0, width, height);

	// Use dim light to represent shadowed areas
	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition);
	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, dim);
	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, dim);
	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, black);
	GL11.glEnable(GL11.GL_LIGHT1);
	GL11.glEnable(GL11.GL_LIGHTING);

	DrawScene(angle, true);

	// 3rd pass
	// Draw with bright light
	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, white);
	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, white);

	// bias from [-1, 1] to [0, 1]
	bM.position(0);
	biasMatrix.load(bM);

	lightP.load(lightProjectionMatrix);
	lightProjectionMatrix.flip();
	lightV.load(lightViewMatrix);
	lightViewMatrix.flip();

	Matrix4f.mul(biasMatrix, lightP, lightP);

	Matrix4f.mul(lightP, lightV, textureMatrix);

	// Set up texture coordinate generation.
	GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);

	tempFloatBuffer.put(0, textureMatrix.m00);
	tempFloatBuffer.put(1, textureMatrix.m10);
	tempFloatBuffer.put(2, textureMatrix.m20);
	tempFloatBuffer.put(3, textureMatrix.m30);

	GL11.glTexGen(GL11.GL_S, GL11.GL_EYE_PLANE, tempFloatBuffer);
	GL11.glEnable(GL11.GL_TEXTURE_GEN_S);

	GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);

	tempFloatBuffer.put(0, textureMatrix.m01);
	tempFloatBuffer.put(1, textureMatrix.m11);
	tempFloatBuffer.put(2, textureMatrix.m21);
	tempFloatBuffer.put(3, textureMatrix.m31);

	GL11.glTexGen(GL11.GL_T, GL11.GL_EYE_PLANE, tempFloatBuffer);
	GL11.glEnable(GL11.GL_TEXTURE_GEN_T);

	GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);

	tempFloatBuffer.put(0, textureMatrix.m02);
	tempFloatBuffer.put(1, textureMatrix.m12);
	tempFloatBuffer.put(2, textureMatrix.m22);
	tempFloatBuffer.put(3, textureMatrix.m32);

	GL11.glTexGen(GL11.GL_R, GL11.GL_EYE_PLANE, tempFloatBuffer);
	GL11.glEnable(GL11.GL_TEXTURE_GEN_R);

	GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);

	tempFloatBuffer.put(0, textureMatrix.m03);
	tempFloatBuffer.put(1, textureMatrix.m13);
	tempFloatBuffer.put(2, textureMatrix.m23);
	tempFloatBuffer.put(3, textureMatrix.m33);

	GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, tempFloatBuffer);
	GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);

	GL11.glPushMatrix();
	GL11.glTranslatef(lightPosition.get(0), lightPosition.get(1), lightPosition.get(2));

	s.draw(0.2f, 24, 24);
	GL11.glPopMatrix();

	GL11.glEnable(GL11.GL_TEXTURE_2D);
	GL11.glMatrixMode(GL11.GL_TEXTURE);
	GL11.glPushMatrix();

	GL11.glScalef(1f / scale, 1 / scale, 1);
	GL11.glTranslatef((scale - 1) * 0.5f, (scale - 1) * 0.5f, (scale - 1) * 0.5f);
	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	// Enable shadow comparison
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, ARBShadow.GL_TEXTURE_COMPARE_MODE_ARB, GL14.GL_COMPARE_R_TO_TEXTURE);

	// Shadow comparison should be true (ie not in shadow) if r<=texture
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, ARBShadow.GL_TEXTURE_COMPARE_FUNC_ARB, GL11.GL_LEQUAL);

	// Shadow comparison should generate an INTENSITY result
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, ARBDepthTexture.GL_DEPTH_TEXTURE_MODE_ARB, GL11.GL_INTENSITY);

	// Set alpha test to discard false comparisons
	GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.99f);
	GL11.glEnable(GL11.GL_ALPHA_TEST);

	DrawScene(angle, true);

	GL11.glMatrixMode(GL11.GL_TEXTURE);
	GL11.glPopMatrix();
	GL11.glMatrixMode(GL11.GL_MODELVIEW);

	GL11.glDisable(GL11.GL_TEXTURE_2D);

	GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
	GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
	GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
	GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);

	// Restore other states
	GL11.glDisable(GL11.GL_LIGHTING);
	GL11.glDisable(GL11.GL_ALPHA_TEST);

    }

    static void DrawScene(float angle, boolean ground)
    {
	// Display lists for objects

	// Create spheres list if necessary
	if (spheresList == 0)
	{
	    spheresList = GL11.glGenLists(1);
	    GL11.glNewList(spheresList, GL11.GL_COMPILE);
	    {
		GL11.glColor3f(0.0f, 1.0f, 0.0f);
		GL11.glPushMatrix();

		GL11.glTranslatef(0, 1.0f, 0);
		s.draw(0.2f, 24, 24);

		GL11.glTranslatef(0.45f, 0.0f, 0.45f);
		s.draw(0.2f, 24, 24);

		GL11.glTranslatef(-0.9f, 0.0f, 0.0f);
		s.draw(0.2f, 24, 24);

		GL11.glTranslatef(0.0f, 0.0f, -0.9f);
		s.draw(0.2f, 24, 24);

		GL11.glTranslatef(0.9f, 0.0f, 0.0f);
		s.draw(0.2f, 24, 24);

		GL11.glPopMatrix();
	    }
	    GL11.glEndList();
	}

	if (baseList == 0)
	{
	    baseList = GL11.glGenLists(1);
	    GL11.glNewList(baseList, GL11.GL_COMPILE);
	    {
		GL11.glColor3f(0.0f, 0.0f, 1.0f);
		GL11.glPushMatrix();

		GL11.glScalef(1.5f, 0.25f, 1.5f);

		GL11.glBegin(GL11.GL_QUADS);
		// Front Face
		GL11.glNormal3f(0, 0, 1);
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The
						     // Texture and Quad
		GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The
						    // Texture and Quad
		GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Texture
						   // and Quad
		GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Texture
						    // and Quad
		// Back Face
		GL11.glNormal3f(0, 0, -1);
		GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The
						      // Texture and Quad
		GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Texture
						     // and Quad
		GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Texture
						    // and Quad
		GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The
						     // Texture and Quad
		// Top Face
		GL11.glNormal3f(0, 1, 0);
		GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Texture
						     // and Quad
		GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The
						    // Texture and Quad
		GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The
						   // Texture and Quad
		GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Texture
						    // and Quad
		// Bottom Face
		GL11.glNormal3f(0, -1, 0);
		GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Top Right Of The
						      // Texture and Quad
		GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Top Left Of The Texture
						     // and Quad
		GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The
						    // Texture and Quad
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The
						     // Texture and Quad
		// Right face
		GL11.glNormal3f(1, 0, 0);
		GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The
						     // Texture and Quad
		GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Texture
						    // and Quad
		GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Texture
						   // and Quad
		GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The
						    // Texture and Quad
		// Left Face
		GL11.glNormal3f(-1, 0, 0);
		GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The
						      // Texture and Quad
		GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The
						     // Texture and Quad
		GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Texture
						    // and Quad
		GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Texture
						     // and Quad
		GL11.glEnd();

		GL11.glPopMatrix();

	    }
	    GL11.glEndList();
	}

	// Draw objects
	if (ground)
	{
	    GL11.glPushMatrix();
	    GL11.glCallList(baseList);
	    // GL11.glCallList(torusList);
	    GL11.glPopMatrix();
	}

	GL11.glPushMatrix();
	GL11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
	GL11.glCallList(spheresList);
	GL11.glPopMatrix();
    }

    public static void cleanup()
    {
	Keyboard.destroy();
	Mouse.destroy();
	Display.destroy();
	System.gc();
	System.exit(0);
    }
}

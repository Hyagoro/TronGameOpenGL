package ui;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;

public class Launcher {

    private Menu menu;

    public void start() {
        initGL(1000, 600);
        init();

        while (true) {

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            menu.pollInput();
            menu.update();
            menu.render();

            Display.update();
            Display.sync(35);//35

            if (Display.isCloseRequested()) {
                Display.destroy();
                System.exit(0);
            }
        }
    }

    /**
     * Initialise the GL display
     *
     * @param width  The width of the display
     * @param height The height of the display
     */
    private void initGL(int width, int height) {
        try {
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
            //Display.setVSyncEnabled(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        // Reset the current Viewport
        GL11.glViewport(0, 0, width, height);
        // Select the model view matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        // Select the projection matrix
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        // Reset the projection matrix
        GL11.glLoadIdentity();
        // Calculate the aspect ratio of the windows
        GLU.gluPerspective(70, (float) width / height, 1, 1350);
        // GL11.glOrtho(0, width, height, 0, 1, -1);
        // Select the model view matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        // Reset the model view matrix
        GL11.glLoadIdentity();
        // Enable texture 2D
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // Enable smooth shading
        GL11.glShadeModel(GL11.GL_SMOOTH);
        // GL11.glShadeModel(GL11.GL_FLAT);
        // Black background
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//	GL11.glEnable(GL15.GL_ARRAY_BUFFER_BINDING);
        // Depth Buffer setup
        GL11.glClearDepth(1.0f);
        // Enable depth testing //pour fusionner les carrés -> important
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // The type of depth test to use
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        // Really nice perspective calculation
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        // Set the blending function for translucency
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);// GL11.GL_ONE_MINUS_SRC_ALPHA
        // enable alpha blending, je pense qu'il faut de la lumière pour ça
//	//GL11.glEnable(GL11.GL_BLEND);
//	
//	float colorLightAmbient[] = {0.1f, 0.1f, 0.1f, 1.0f};
//	float colorLightDiffuse[] = {1.0f, 1.0f, 1.0f, 1.0f};
//	float lightPosition[] = {0.0f, 0.0f, 150.0f, 1.0f};
//	float lightSpecular[] = {1.0f, 1.0f, 1.0f, 1.0f};
//	
//	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, floatBuffer(colorLightAmbient));
//	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, floatBuffer(colorLightDiffuse));
//	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, floatBuffer(lightPosition));
//	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, floatBuffer(lightSpecular));
//	
//	GL11.glEnable(GL11.GL_LIGHT1);
//	GL11.glEnable(GL11.GL_LIGHTING);
//	
//	
//	//en test
////	GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, GL11.GL_TRUE);
//	
//	GL11.glEnable(GL11.GL_COLOR_MATERIAL);
//	GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
        //Setup the ambiant light
//	GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, lightAmbient);
        //Selon light ou pas GL11.glEnable(GL11.GL_LIGHTING);
        // ou GL11.glDisable(GL11.GL_LIGHTING);
    }

    public void init() {
        this.menu = new Menu();
    }

    public FloatBuffer floatBuffer(float a, float b, float c, float d) {
        float data[] = new float[]{a, b, c, d};
        FloatBuffer fb = BufferUtils.createFloatBuffer(data.length);
        fb.put(data);
        fb.flip();
        return fb;
    }

    public FloatBuffer floatBuffer(float[] a) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(a.length);
        fb.put(a);
        fb.flip();
        return fb;
    }

    void dessinerRepere() {
        int echelle = 50;
        GL11.glPushMatrix();
        GL11.glScalef(echelle, echelle, echelle);
        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glColor3f(0, 0, 1f);
            GL11.glVertex2i(0, 0);
            GL11.glVertex2i(1, 0);
            GL11.glColor3f(0, 1f, 0);
            GL11.glVertex2i(0, 0);
            GL11.glVertex2i(0, 1);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    /**
     * Main Class
     */
    public static void main(String[] argv) {
        Launcher game = new Launcher();
        game.start();
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}

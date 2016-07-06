package ui;

import controler.ControleurCamera;
import modele.Camera;
import network.ClientTron;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import utils.StringDraw;

public class Game {
    private Menu menu;

    private Map map;
    private Camera camera;
    private ClientTron client;

    public Game(ClientTron client, Camera camera, Menu menu) {
        initialisation();
        this.client = client;
        this.camera = camera;
        this.menu = menu;
    }

    public void initialisation() {
        int xCarte = 2000;
        int yCarte = 2000;

        this.map = new Map(xCarte, yCarte, Map.Type.GAME);
    }

    public void update() {
        Player player = client.getPlateforme().getJoueurClient();
        int ID = client.getPlateforme().getIdClient();

        camera.update(player.getX(), player.getY());

        // update et analyse inversée (EN TEST)
        client.getPlateforme().analyserDeplacements(map, player);
        client.getPlateforme().update(ID, client);
        client.getPlateforme().getJoueurClient().update();

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            client.getPlateforme().relancerClient(map);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
            System.out.println("GAME - touche Y");
            if (!client.getPlateforme().getJoueurClient().isPret()) {
                client.getPlateforme().envoyerJoueurPret(client);
                client.getPlateforme().getJoueurClient().setPret(true);
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            menu.stopGame();
        }
    }

    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // GL11.glPushMatrix();
        // GL11.glPopMatrix();

        GLU.gluLookAt(camera.getEyex(), camera.getEyey(), camera.getEyez(), camera.getCenterx(), camera.getCentery(),
                camera.getCenterz(), camera.getUpx(), camera.getUpy(), camera.getUpz());

        // dessinerRepere();
        map.render();
        client.getPlateforme().render();

        if (!client.getPlateforme().getJoueurClient().isPret()) {
            int x = (int) client.getPlateforme().getJoueurClient().getX();
            int y = (int) client.getPlateforme().getJoueurClient().getY();
            StringDraw.drawString("appuyer sur y pour lancer la partie", x, y + 15);
        } else {
            if (!client.getPlateforme().isJoueursPret()) {
                int x = (int) client.getPlateforme().getJoueurClient().getX();
                int y = (int) client.getPlateforme().getJoueurClient().getY();
                StringDraw.drawString("en attente des autres joueurs", x, y + 15);
            }
        }

        GL11.glFlush();
    }

    public void pollInput() {
        ControleurCamera.updateCameraOnEvent(camera);
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}

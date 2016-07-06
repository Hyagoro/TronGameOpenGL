package ui;

import controler.ControleurCamera;
import controler.ControleurChoixMenu;
import modele.Camera;
import modele.Plateforme;
import network.ClientTron;
import network.ServeurKryoNetTron;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.glu.GLU;
import utils.StringDraw;

import java.io.IOException;

public class Menu {
    private Game game;
    private ClientTron client;
    private ServeurKryoNetTron serveur;

    private Camera camera;
    private Map map;
    private Player player;

    private boolean showGame;
    private boolean serveurLance;
    private boolean clientConnecte;

    private int distanceDeVue = 1300;
    private int distanceDeVueStandard = 320;

    public Menu() {
        this.camera = new Camera(0, 0, distanceDeVue, 2, 2, 0, 0, 1, 0);
        this.serveurLance = false;
        this.clientConnecte = false;
        this.showGame = false;

        int xCarte = 300;
        int yCarte = 300;

        this.map = new Map(xCarte, yCarte, Map.Type.MENU);
        this.player = new Player(0, new Color(250, 250, 250), xCarte / 2, yCarte / 2, xCarte, yCarte);
    }

    public void pollInput() {
        if (showGame) {
            game.pollInput();
        } else {
            ControleurCamera.updateCameraOnEvent(camera);
        }
    }

    public void render() {
        if (showGame) {
            game.render();
        } else {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            GLU.gluLookAt(camera.getEyex(), camera.getEyey(), camera.getEyez(), camera.getCenterx(),
                    camera.getCentery(), camera.getCenterz(), camera.getUpx(), camera.getUpy(), camera.getUpz());

            map.render();
            player.render();

            StringDraw
                    .drawString("Serveur et client", map.getMatrice().getTailleX(), map.getMatrice().getTailleY() / 2);

            StringDraw.drawString("Client", -60, map.getMatrice().getTailleY() / 2);

            GL11.glFlush();
        }
    }

    public void update() {
        if (showGame) {
            game.update();
        } else {
            // TODO animation de camera à bouger
            if (distanceDeVue > distanceDeVueStandard) {
                distanceDeVue -= distanceDeVue / 70;
                camera.setEyez(distanceDeVue);
            }

            Action action = Action.AUCUNE;

            action = ControleurChoixMenu.analyserDeplacements(map, player);
            analyserAction(action);

            camera.update(player.getX(), player.getY());
            player.miseAJourDirection();

            player.update();
        }
    }

    public void stopGame() {
        showGame = false;
        client.stop();
        // client.getPlateforme().reset();
    }

    private void analyserAction(Action action) {
        int xCarte = 300;
        int yCarte = 300;
        switch (action) {
            case AUCUNE:
                break;
            case MENUCLIENT:
                lancerClient();
                this.player.reset(xCarte / 2, yCarte / 2);
                this.game = new Game(client, camera, this);
                this.showGame = true;
                break;
            case MENUSERVEUR:
                lancerServeurEtClient();
                this.player.reset(xCarte / 2, yCarte / 2);
                this.game = new Game(client, camera, this);
                this.showGame = true;
                break;
            default:
                break;

        }
    }

    private void lancerServeurEtClient() {
        if (!serveurLance) {
            try {
                serveur = new ServeurKryoNetTron();
                serveur.isRun();
                serveurLance = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lancerClient();
        }
    }

    private void lancerClient() {
        client = new ClientTron(new Plateforme());
        if (!client.isRun()) {
            client.initialisation();

            if (client.isRun()) {
                while (true) {
                    if (!client.isLogBackRecu()) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else
                        break;
                }

                setClientConnecte(true);
            }
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public ClientTron getClient() {
        return client;
    }

    public void setClient(ClientTron client) {
        this.client = client;
    }

    public ServeurKryoNetTron getServeur() {
        return serveur;
    }

    public void setServeur(ServeurKryoNetTron serveur) {
        this.serveur = serveur;
    }

    public boolean isServeurLance() {
        return serveurLance;
    }

    public void setServeurLance(boolean serveurLance) {
        this.serveurLance = serveurLance;
    }

    public boolean isClientConnecte() {
        return clientConnecte;
    }

    public void setClientConnecte(boolean clientConnecte) {
        this.clientConnecte = clientConnecte;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isShowGame() {
        return showGame;
    }

    public void setShowGame(boolean showGame) {
        this.showGame = showGame;
    }

    public int getDistanceDeVue() {
        return distanceDeVue;
    }

    public void setDistanceDeVue(int distanceDeVue) {
        this.distanceDeVue = distanceDeVue;
    }

    public int getDistanceDeVueStandard() {
        return distanceDeVueStandard;
    }

    public void setDistanceDeVueStandard(int distanceDeVueStandard) {
        this.distanceDeVueStandard = distanceDeVueStandard;
    }
}

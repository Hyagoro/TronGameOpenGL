package network;

import beans.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import modele.Plateforme;
import org.lwjgl.util.Color;
import utils.Ressources;

import java.io.IOException;
import java.util.LinkedList;

//import modele.Plateforme;
//import ui.UIJeu;

public class ClientTron {
    private boolean run = false;
    private Client client;
    private String name;
    private LinkedList<String> console; // liste chainee
    private int xCarte;
    private int yCarte;
    private boolean logBackRecu = false;

    private Plateforme plateforme;
    public static final int port = Ressources.port; // le port utilisé pour TCP

    public ClientTron(Plateforme plateforme) // Plateforme plateforme
    {
        this.plateforme = plateforme;
        this.console = new LinkedList<String>();
        this.name = Ressources.nickname;
    }

    public int random(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }

    public void stop() {
//	plateforme.stop();
        client.close();
    }

    public void initialisation() {
        client = new Client();
        client.start();
    /* on enregistre les objets ...attention à l'ordre !!! */
        Kryo kryo = client.getKryo();
        kryo.register(PacketLog.class);
        kryo.register(PacketLogBack.class);
        kryo.register(PacketCoord.class);
        kryo.register(PacketLeave.class);
        kryo.register(PacketMAJJoueurs.class);
        kryo.register(PacketAlerte.class);
        kryo.register(PacketPret.class);
	/* on prépare l'objet à envoyer lors de la connexion au serveur */
        PacketLog nouveau = new PacketLog();
        nouveau.setNom(name);

        nouveau.setR(Ressources.color.getRed());
        nouveau.setG(Ressources.color.getGreen());
        nouveau.setB(Ressources.color.getBlue());

        client.addListener(new Listener() {
            public void connected(Connection connection) {
                console.add("CLIENT - Joueur connecté");
            }

            public void received(Connection connection, Object object) {
                if (object instanceof PacketLogBack) {
                    PacketLogBack response = (PacketLogBack) object;
                    xCarte = response.getxCarte();
                    yCarte = response.getyCarte();
                    console.add("CLIENT - Taille carte  x:" + xCarte + ", y:" + yCarte + "");
                    console.add("CLIENT - " + response.getMessage());
                    // UIJeu.afficherPopUp(response.getMessage(), new
                    // Color(0,102,255));
                    logBackRecu = true;
                } else if (object instanceof PacketCoord) {
//		     System.out.println("CLIENT- Packet recus : "+object);
                    plateforme.appliquerPacketCoord((PacketCoord) object);
                } else if (object instanceof Integer) {
                    plateforme.supprimerJoueur((Integer) object);
                } else if (object instanceof PacketMAJJoueurs) {
                    PacketMAJJoueurs p = (PacketMAJJoueurs) object;

                    plateforme.ajouterJoueur(p.getID(), new Color(p.getR(), p.getG(), p.getB()));

                } else if (object instanceof PacketAlerte) {
                    PacketAlerte p = (PacketAlerte) object;
                    // UIJeu.afficherPopUp(p.getAlerte(), Color.red);
                } else if (object instanceof PacketPret) {
                    System.out.println("CLIENT - tout le monde ready");
                    plateforme.joueursPret();
                } else {
                    // System.out.println("CLIENT - Comprend pas "+connection.getID()+" et "+connection.getReturnTripTime());
                }
                // System.out.println("CLIENT - TEST !");
            }

            public void disconnected(Connection connection) {
                System.out.println("CLIENT - DECO");
            }
        });

        String host = Ressources.adresseIP;
        try {
            client.connect(5000, host, port);
            if (client.isConnected()) {
                run = true;
                client.sendTCP(nouveau);// on envoie l'objet précédemment
                // préparé
                plateforme.setIdClient(client.getID());
            }

        } catch (IOException ex) {
            console.add("CLIENT - Impossible de se connecter au serveur " + host + ":" + port);
            ex.printStackTrace();
        }
    }

    public void envoyerTCP(Object o) {
        client.sendTCP(o);
    }

    public LinkedList<String> getConsole() {
        LinkedList<String> tmp = new LinkedList<String>(console);
        console.clear();
        return tmp;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Plateforme getPlateforme() {
        return plateforme;
    }

    public void setPlateforme(Plateforme plateforme) {
        this.plateforme = plateforme;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getxCarte() {
        return xCarte;
    }

    public int getyCarte() {
        return yCarte;
    }

    public void setxCarte(int xCarte) {
        this.xCarte = xCarte;
    }

    public void setyCarte(int yCarte) {
        this.yCarte = yCarte;
    }

    public boolean isLogBackRecu() {
        return logBackRecu;
    }

    public void setLogBackRecu(boolean logBackRecu) {
        this.logBackRecu = logBackRecu;
    }

}

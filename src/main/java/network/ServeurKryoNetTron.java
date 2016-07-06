package network;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import utils.Ressources;
import beans.PacketAlerte;
import beans.PacketCoord;
import beans.PacketLeave;
import beans.PacketLog;
import beans.PacketLogBack;
import beans.PacketMAJJoueurs;
import beans.PacketPret;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class ServeurKryoNetTron
{
    private static boolean run = true;
    private Server server;
    private LinkedList<String> console;
    private HashMap<Integer, PacketLog> listeJoueurs;
    private int nbreConnexions = 0;
    static public final int port = Ressources.port; // le port utilisé pour TCP
    private int nbJoueursPret;

    private int xCarte;
    private int yCarte;

    public ServeurKryoNetTron() throws IOException
    {
	nbJoueursPret = 0;
	listeJoueurs = new HashMap<Integer, PacketLog>();
	console = new LinkedList<String>();
	chargerRessources();
	System.out.println("SERVEUR - Taille carte  x:" + xCarte + ", y:" + yCarte + "");

	console.add("SERVEUR - Lancement du serveur");
	server = new Server();

	Kryo kryo = server.getKryo();
	kryo.register(PacketLog.class);
	kryo.register(PacketLogBack.class);
	kryo.register(PacketCoord.class);
	kryo.register(PacketLeave.class);
	kryo.register(PacketMAJJoueurs.class);
	kryo.register(PacketAlerte.class);
	kryo.register(PacketPret.class);

	console.add("SERVEUR - Serveur lancé");
	server.addListener(new Listener()
	{
	    public void connected(Connection connection)
	    {
		nbreConnexions = nbreConnexions + 1;
	    }

	    public void received(Connection c, Object object)
	    {
		if (object instanceof PacketLog)
		{
		    PacketLog log = (PacketLog) object;
		    String nom = log.getNom();
		    console.add("SERVEUR - Connecté LOGIN: " + nom);
		    console.add("SERVEUR - Nombre de connection(s) :" + nbreConnexions);

		    listeJoueurs.put(c.getID(), log);

		    /* on prépare le message de retour à tous les joueurs */
		    PacketLogBack annonce = new PacketLogBack();
		    annonce.setMessage("SERVEUR - Le joueur :" + nom + " vient de se connecter !\n");
		    annonce.setxCarte(xCarte);
		    annonce.setyCarte(yCarte);
		    server.sendToAllTCP(annonce);

		    // joueurOnMap.add(c.getID());

		    PacketMAJJoueurs pmj = new PacketMAJJoueurs();
		    pmj.setR(log.getR());
		    pmj.setG(log.getG());
		    pmj.setB(log.getB());
		    pmj.setNombreJoueurs(nbreConnexions);
		    pmj.setID(c.getID());
		    server.sendToAllTCP(pmj);

		    for (int i = 0; i < server.getConnections().length; i++)
		    {
			Connection[] cs = server.getConnections();
			if (cs[i].getID() != c.getID())
			{
			    System.out.println("MAJ Recus de " + c.getID() + " et envoi à " + cs[i].getID());
			    PacketMAJJoueurs pmj2 = new PacketMAJJoueurs();
			    pmj2.setR(listeJoueurs.get(cs[i].getID()).getR());
			    pmj2.setG(listeJoueurs.get(cs[i].getID()).getG());
			    pmj2.setB(listeJoueurs.get(cs[i].getID()).getB());
			    pmj2.setNombreJoueurs(nbreConnexions);
			    pmj2.setID(cs[i].getID());
			    server.sendToTCP(c.getID(), pmj2);
			}
		    }
		}
		else if (object instanceof PacketCoord)
		{
		    int ID = c.getID();
		    PacketCoord packet = new PacketCoord((PacketCoord) object);
		    server.sendToAllExceptTCP(ID, packet);
		}
		else if (object instanceof PacketAlerte)
		{
		    PacketAlerte packet = new PacketAlerte((PacketAlerte) object);
		    server.sendToAllExceptTCP(c.getID(), packet);
		}
		else if(object instanceof PacketPret)
		{
		    nbJoueursPret++;
		    PacketPret packet = new PacketPret();
		    System.out.println("SERVEUR - nbJoueursPret : "+nbJoueursPret+" nbConnec : "+nbreConnexions);
		    if(nbJoueursPret == nbreConnexions)
		    {
			server.sendToAllTCP(packet);
		    }
		}
		else
		{
		    // System.out.println("SERVEUR - Comprend pas " +
		    // c.getID());
		}
	    }

	    public void disconnected(Connection connection)
	    {
		int ID = connection.getID();
		// PacketLeave p = new PacketLeave(ID);
		server.sendToAllExceptTCP(ID, ID);
		System.out.println("SERVEUR - déconnection de " + connection.getID());
	    }
	});
	server.bind(port);
	server.start();
    }

    public static void main(String[] args) throws IOException
    {
	new ServeurKryoNetTron();
    }

    private void chargerRessources()
    {
	SAXBuilder saxBuilder = new SAXBuilder();
	File file = new File("src/main/resources/confServer.xml");

	try
	{
	    Document document = saxBuilder.build(file);
	    Element element = document.getRootElement();

	    setxCarte(Integer.parseInt(element.getChildText("x")));
	    yCarte = Integer.parseInt(element.getChildText("y"));
	}
	catch (JDOMException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public LinkedList<String> getConsole()
    {
	LinkedList<String> tmp = new LinkedList<String>(console);
	console.clear();
	return tmp;
    }

    public boolean isRun()
    {
	return run;
    }

    public int getyCarte()
    {
	return yCarte;
    }

    public void setyCarte(int yCarte)
    {
	this.yCarte = yCarte;
    }

    public void setRun(boolean run)
    {
	ServeurKryoNetTron.run = run;
    }

    public int getxCarte()
    {
	return xCarte;
    }

    public void setxCarte(int xCarte)
    {
	this.xCarte = xCarte;
    }

    public int getNbJoueursPret()
    {
	return nbJoueursPret;
    }

    public void setNbJoueursPret(int nbJoueursPret)
    {
	this.nbJoueursPret = nbJoueursPret;
    }
}

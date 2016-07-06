package modele;

import java.util.LinkedList;

import network.ClientTron;

import org.lwjgl.util.Color;

import ui.BlocMur;
import ui.Map;
import ui.Player;
import beans.PacketAlerte;
import beans.PacketCoord;
import beans.PacketPret;
import controler.ControleurColision;

public class Plateforme
{
    private LinkedList<Player> joueurs;
    private LinkedList<Player> joueursDeco;
    private Integer idClient;
    private Player joueurClient;
    
    private boolean joueursPret;

    public Plateforme()
    {
	this.joueursPret = false;
	this.joueurs = new LinkedList<Player>();
	this.joueursDeco = new LinkedList<Player>();
    }

    public void analyserDeplacements(Map carte, Player joueur)
    {
	ControleurColision.analyserDeplacements(carte, joueurs, joueur);
    }
    
    public void stop()
    {
	for(Player joueur : joueurs)
	{
	    joueur.free();
	}
	this.joueurs = null;
	this.joueursDeco = null;
	this.joueurClient = null;
    }

    /**
     * Affichages des joueurs avec position relative au client
     * 
     * @param IDClient
     */
    public void render()
    {
	for (int i = 0; i < joueurs.size(); i++)
	{
	    joueurs.get(i).render();
	    // joueurs.get(i).afficherWithOpenGL();
	}
    }

    public boolean isBlocMur(int x, int y)
    {
	for (int i = 0; i < joueurs.size(); i++)
	{
	    if (joueurs.get(i).isBlocMur(x, y))
	    {
		return true;
	    }
	}
	return false;
    }

    public void relancerClient(Map carte)
    {
	int x, y;
	do
	{
	    x = ((int) (Math.random() * (carte.getMatrice().getTailleX() - 1)) + 1) / 10 * 10;
	    y = ((int) (Math.random() * (carte.getMatrice().getTailleY() - 1)) + 1) / 10 * 10;
	}
	while ((carte.getCase(x, y) instanceof BlocMur));

	joueurClient.respawn(x, y);
    }

    /**
     * Envoi de notre position aux autres joueurs
     * 
     * @param client
     */
    public void envoiPositionServeur(ClientTron client)
    {
	if (!joueurClient.getEvent().isEventConsumed())
	{
	    PacketCoord p = joueurClient.createPacketCoord();
	    client.envoyerTCP(p);
	    joueurClient.getEvent().setEventConsumed(true);
	}
    }

    public void envoiAlerteServeur(ClientTron client, String alerte)
    {
	PacketAlerte p = new PacketAlerte();
	p.setAlerte(alerte);
	client.envoyerTCP(p);
    }
    
    public void envoyerJoueurPret(ClientTron client)
    {
	PacketPret packet = new PacketPret();
	client.envoyerTCP(packet);
    }

    /**
     * Mise a jour des joueurs (texture, direction et position)
     * 
     * @param gc
     * @param IDClient
     */
    public void update(int IDClient, ClientTron client)
    {
	for (int i = 0; i < joueurs.size(); i++)
	{
	    if(joueursPret)
	    {
		joueurs.get(i).update();
		joueurs.get(i).miseAJourDirection(IDClient, client);
	    }
	}
    }

    public void supprimerJoueur(int ID)
    {
	for (int i = 0; i < joueurs.size(); i++)
	{
	    if (joueurs.get(i).getID() == ID)
	    {

		joueursDeco.add(joueurs.get(i));
		joueurs.remove(i);
	    }
	}
    }

    public void appliquerPacketCoord(PacketCoord packet)
    {
	Player joueur = getJoueurByID(packet.getID());
	if (joueur != null)
	{
	    joueur.appliquerPacketCoord(packet);
	}
    }
        
    public void joueursPret()
    {
	this.joueursPret = true;
    }

    public void ajouterJoueur(int ID, Color color)
    {
	System.out.println("Creation de " + ID);
	Player moto = new Player(ID, color, (double) 500, (double) 500, 2000, 2000);
	if (idClient.equals(ID))
	{
	    System.out.println("ID : "+ID+" idClient : "+idClient+" Color : "+color);
	    this.joueurClient = moto;
	}
	joueurs.add(moto);
    }

//    public void ajouterJoueur(int ID, int x, int y, boolean own, Color color)
//    {
//	joueurs.add(new Player(ID, color, (double) 500, (double) 500, 2000, 2000));
//    }

    public LinkedList<Player> getJoueurs()
    {
	return joueurs;
    }

    public Player getJoueurByID(int ID)
    {
	for (int i = 0; i < joueurs.size(); i++)
	{
	    // System.out.println("getJoueurByID : "+ID);
	    if (joueurs.get(i).getID() == ID)
	    {
		return joueurs.get(i);
	    }
	}
	return null;
    }

    public void setJoueurs(LinkedList<Player> joueurs)
    {
	this.joueurs = joueurs;
    }

    public LinkedList<Player> getJoueursDeco()
    {
	return joueursDeco;
    }

    public Integer getIdClient()
    {
	return idClient;
    }

    public Player getJoueurClient()
    {
	return joueurClient;
    }

    public void setJoueursDeco(LinkedList<Player> joueursDeco)
    {
	this.joueursDeco = joueursDeco;
    }

    public void setIdClient(Integer idClient)
    {
	this.idClient = idClient;
    }

    public void setJoueurClient(Player joueurClient)
    {
	this.joueurClient = joueurClient;
    }

    public boolean isJoueursPret()
    {
	return joueursPret;
    }

    public void setJoueursPret(boolean joueursPret)
    {
	this.joueursPret = joueursPret;
    }
}

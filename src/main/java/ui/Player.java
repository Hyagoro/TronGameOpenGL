package ui;

import beans.PacketCoord;
import controler.EventMoto;
import modele.Direction;
import network.ClientTron;
import org.lwjgl.util.Color;

import java.util.LinkedList;

public class Player extends Bloc {
    private int ID;
    private String nom = "";
    private Color color;
    private Boolean alive;
    private int nombreVie;
    private boolean pret;

    private EventMoto event;
    private LinkedList<BlocMur> mursMoto;
    private LinkedList<PacketCoord> historiquePosition;// la dernière position
    // en premier
    private Matrice matrice;


    public Player(int ID, Color color, double x, double y, int tailleCarteX, int tailleCarteY) {
        super(x, y, color);
        this.historiquePosition = new LinkedList<PacketCoord>();
        this.mursMoto = new LinkedList<BlocMur>();
        this.event = new EventMoto();
        this.alive = true;
        this.ID = ID;
        this.nombreVie = 3;
        this.color = color;
        this.matrice = new Matrice(tailleCarteX, tailleCarteY);
        this.pret = false;
        super.setDirection(Direction.AUCUNE);
        super.setX(x);
        super.setY(y);
    }

    public void reset(double x, double y) {
        this.historiquePosition = new LinkedList<PacketCoord>();
        this.matrice.initialiserMatrice();
        for (BlocMur murMoto : mursMoto) {
            murMoto.deleteVBO();
        }
        this.mursMoto = new LinkedList<BlocMur>();
        this.event = new EventMoto();
        this.alive = true;
        super.setDirection(Direction.AUCUNE);
        super.setX(x);
        super.setY(y);
    }

    public void free() {
        this.historiquePosition = null;
        this.matrice.initialiserMatrice();
        for (BlocMur murMoto : mursMoto) {
            murMoto.deleteVBO();
        }
        this.mursMoto = null;
        this.event = null;
        this.alive = true;
    }

    public void respawn(double x, double y) {
        this.event = new EventMoto();
        this.alive = true;
        super.setDirection(Direction.AUCUNE);
        super.setX(x);
        super.setY(y);
    }

    public void setPosition(double x, double y) {
        super.setX(x);
        super.setY(y);
    }

    public PacketCoord createPacketCoord() {
        return new PacketCoord(getX(), getY(), ID, getDirection());
    }

    public void appliquerPacketCoord(PacketCoord packet) {
        super.setX(packet.getX());
        super.setY(packet.getY());
        setDirection(Direction.valueOf(packet.getDirection()));
        historiquePosition.addFirst(packet);
    }

    public void miseAJourDirection() {
        double x = this.getX();
        double y = this.getY();
        if (alive) {
            super.setDirection(event.getEvent());
            deplacer(super.getDirection());
            ajouterMur(x, y);
        } else {
            ajouterMurMort(x, y);
        }
    }

    public void miseAJourDirection(int ID, ClientTron client) {
        double x = this.getX();
        double y = this.getY();
        if (alive) {
            if (this.ID == ID) {
                super.setDirection(event.getEvent());
                deplacer(super.getDirection());
                ajouterMur(x, y);
                client.getPlateforme().envoiPositionServeur(client);
            } else {
                super.setDirection(event.appliquerDirection(getDirection()));
                predictionAjouterMur();
            }
        } else {
            ajouterMurMort(x, y);
        }
    }

    private void ajouterElemBlocMur(BlocMur mm) {
        mursMoto.add(mm);
        matrice.setEntite(mm);
    }

    private void predictionAjouterMur() {
        for (int i = 0; i < historiquePosition.size() - 1; i++) {
            PacketCoord packetActuel = historiquePosition.get(i);
            Direction direction = Direction.valueOf(packetActuel.getDirection());

            if (historiquePosition.get(i + 1) != null) {
                PacketCoord packetPrecedent = historiquePosition.get(i + 1);
                Float xp = (float) packetPrecedent.getX();
                Float yp = (float) packetPrecedent.getY();
                double murAAfficherX = new Double(xp);
                double murAAfficherY = new Double(yp);

                Direction directionPrecedente = Direction.valueOf(packetPrecedent.getDirection());

                if (directionPrecedente == direction) {
                    if (direction == Direction.EST || direction == Direction.OUEST) {
                        ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                Orientation.HORIZONTAL));
                    } else if (direction == Direction.SUD || direction == Direction.NORD) {
                        ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                Orientation.VERTICAL));
                    }
                } else {
                    if (directionPrecedente == Direction.NORD) {
                        if (direction == Direction.EST) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.NORDEST));
                        } else if (direction == Direction.OUEST) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.NORDOUEST));
                        }
                    } else if (directionPrecedente == Direction.SUD) {
                        if (direction == Direction.EST) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.SUDEST));
                        } else if (direction == Direction.OUEST) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.SUDOUEST));
                        }
                    } else if (directionPrecedente == Direction.EST) {
                        if (direction == Direction.NORD) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.SUDOUEST));
                        } else if (direction == Direction.SUD) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.NORDOUEST));
                        }
                    } else if (directionPrecedente == Direction.OUEST) {
                        if (direction == Direction.NORD) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.SUDEST));
                        } else if (direction == Direction.SUD) {
                            ajouterElemBlocMur(new BlocMur(murAAfficherX, murAAfficherY, color, TypeMur.MURMOTO,
                                    Orientation.NORDOUEST));
                        }
                    } else {
                        // cas poubelle
                        // ajouterElemBlocMur(new BlocMur(BlocMur.Mur.VERTICAL,
                        // color, x, y));
                    }
                }
            }
        }
        if (historiquePosition.size() > 1) {
            historiquePosition.removeLast();
        }
    }

    private void ajouterMurMort(double x, double y) {
        Color color = new Color(255, 50, 50);
        ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.NONE));
    }

    private void ajouterMur(double x, double y) {
        if (event.getDirectionPrecedentePersistante() == getDirection()) {
            if (getDirection() == Direction.EST || getDirection() == Direction.OUEST) {
                ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.HORIZONTAL));
            } else if (getDirection() == Direction.SUD || getDirection() == Direction.NORD) {
                ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.VERTICAL));
            }
        } else {
            if (event.getDirectionPrecedentePersistante() == Direction.NORD) {
                if (getDirection() == Direction.EST) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.NORDEST));
                } else if (getDirection() == Direction.OUEST) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.NORDOUEST));
                }
            } else if (event.getDirectionPrecedentePersistante() == Direction.SUD) {
                if (getDirection() == Direction.EST) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.SUDEST));
                } else if (getDirection() == Direction.OUEST) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.SUDOUEST));
                }
            } else if (event.getDirectionPrecedentePersistante() == Direction.EST) {
                if (getDirection() == Direction.NORD) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.SUDOUEST));
                } else if (getDirection() == Direction.SUD) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.NORDOUEST));
                }
            } else if (event.getDirectionPrecedentePersistante() == Direction.OUEST) {
                if (getDirection() == Direction.NORD) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.SUDEST));
                } else if (getDirection() == Direction.SUD) {
                    ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.NORDEST));
                }
            } else {
//		 cas poubelle
//		ajouterElemBlocMur(new BlocMur(x, y, color, TypeMur.MURMOTO, Orientation.VERTICAL));
            }
        }

    }

    @Override
    public void render() {
        super.renderDynamic();
        afficherMurs();
    }

    public boolean isBlocMur(int x, int y) {
        if (matrice.getEntite(x, y) != null)
            return true;
        return false;
    }

    private void afficherMurs() {
        matrice.render();
    }

    public void update() {
        super.update();
    }

    public void deplacer(Direction direction) {
        int tailleBloc = 10;
        switch (direction) {
            case NORD:
                this.setY(this.getY() + tailleBloc);
                break;
            case SUD:
                this.setY(this.getY() - tailleBloc);
                break;
            case EST:
                this.setX(this.getX() + tailleBloc);
                break;
            case OUEST:
                this.setX(this.getX() - tailleBloc);
                break;
            case AUCUNE:
                break;
            default:
                break;
        }
    }

    // public void appliquerPacketCoord(PacketCoord packet)
    // {
    // position = new Vector2f(packet.getX(), packet.getY());
    // setDirection(Direction.valueOf(packet.getDirection()));
    // historiquePosition.addFirst(packet);
    // }
    //
    // public PacketCoord createPacketCoord()
    // {
    // return new PacketCoord(position, ID, getDirection());
    // }

    public Boolean isAlive() {
        return alive;
    }

    public EventMoto getEvent() {
        return event;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }

    public void setEvent(EventMoto event) {
        this.event = event;
    }

    public LinkedList<PacketCoord> getHistoriquePosition() {
        return historiquePosition;
    }

    public void setHistoriquePosition(LinkedList<PacketCoord> historiquePosition) {
        this.historiquePosition = historiquePosition;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getNom() {
        return nom;
    }

    public LinkedList<BlocMur> getBlocMur() {
        return mursMoto;
    }

    public void setBlocMur(LinkedList<BlocMur> murMoto) {
        this.mursMoto = murMoto;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Matrice getMatrice() {
        return matrice;
    }

    public void setMatrice(Matrice matrice) {
        this.matrice = matrice;
    }

    public int getNombreVie() {
        return nombreVie;
    }

    public void setNombreVie(int nombreVie) {
        this.nombreVie = nombreVie;
    }

    public boolean isPret() {
        return pret;
    }

    public void setPret(boolean pret) {
        this.pret = pret;
    }
}

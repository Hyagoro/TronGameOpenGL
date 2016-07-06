package beans;

import modele.Direction;

public class PacketCoord {
    private double x;
    private double y;
    private int ID;
    private String direction;

    public PacketCoord(double x, double y, int ID, Direction direction) {
        this.x = (float) x;
        this.y = (float) y;
        this.ID = ID;
        this.direction = direction.name();
    }

    public PacketCoord(PacketCoord packet) {
        this.x = packet.x;
        this.y = packet.y;
        this.ID = packet.ID;
        this.direction = packet.direction;
    }

    public PacketCoord(int ID) {
        this.x = 0;
        this.y = 0;
        this.ID = ID;
        this.direction = "AUCUNE";
    }

    public PacketCoord() {

    }


    @Override
    public String toString() {
        return "Coord : " + x + "," + y + " dir : " + direction + " ID : " + ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

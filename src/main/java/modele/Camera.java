package modele;

public class Camera {
    private float eyex;
    private float eyey;
    private float eyez;
    private float centerx;
    private float centery;
    private float centerz;
    private float upx;
    private float upy;
    private float upz;


    public Camera(float eyex, float eyey, float eyez, float centerx,
                  float centery, float centerz, float upx, float upy, float upz) {
        this.eyex = eyex;
        this.eyey = eyey;
        this.eyez = eyez;
        this.centerx = centerx;
        this.centery = centery;
        this.centerz = centerz;
        this.upx = upx;
        this.upy = upy;
        this.upz = upz;
    }

    public void update(double xCible, double yCible) {
        float distanceX = (float) (xCible - eyex);
        float distanceY = (float) (yCible - eyey);

        this.eyex = eyex + (distanceX / 2);
        this.eyey = eyey + (distanceY / 2);

//		this.eyex =  (float) xCible;
//		this.eyey =  (float) yCible;

        this.centerx = eyex;
        this.centery = eyey;
    }

    @Override
    public String toString() {
        return "(" + eyex + "," + eyey + "," + eyez + "," + centerx + "," + centery + "," + centerz + "," + upx + "," + upy + "," + upz + "," + ")";
    }

    public float getEyex() {
        return eyex;
    }

    public float getEyey() {
        return eyey;
    }

    public float getEyez() {
        return eyez;
    }

    public float getCenterx() {
        return centerx;
    }

    public float getCentery() {
        return centery;
    }

    public float getCenterz() {
        return centerz;
    }

    public float getUpx() {
        return upx;
    }

    public float getUpy() {
        return upy;
    }

    public float getUpz() {
        return upz;
    }

    public void setEyex(float eyex) {
        this.eyex = eyex;
    }

    public void setEyey(float eyey) {
        this.eyey = eyey;
    }

    public void setEyez(float eyez) {
        this.eyez = eyez;
    }

    public void setCenterx(float centerx) {
        this.centerx = centerx;
    }

    public void setCentery(float centery) {
        this.centery = centery;
    }

    public void setCenterz(float centerz) {
        this.centerz = centerz;
    }

    public void setUpx(float upx) {
        this.upx = upx;
    }

    public void setUpy(float upy) {
        this.upy = upy;
    }

    public void setUpz(float upz) {
        this.upz = upz;
    }


}

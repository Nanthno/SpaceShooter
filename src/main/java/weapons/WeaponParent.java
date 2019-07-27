package src.main.java.weapons;

public class WeaponParent {

    protected WeaponType type;

    protected double xPos;
    protected double yPos;

    protected double xSpeed;
    protected double ySpeed;

    protected int radius;

    protected int stage;
    protected int effectiveStage;
    protected int maxStage;

    protected int currentFrame = 0;

    public boolean update() {
        xPos += xSpeed;
        yPos += ySpeed;
        return false;
    }

    public int getx() {
        return (int)xPos;
    }
    public int gety() {
        return (int)yPos;
    }
    public int getRadius() {
        return radius;
    }

    public int getFrame() {
        return currentFrame;
    }
    public WeaponType getType() {
        return type;
    }

}

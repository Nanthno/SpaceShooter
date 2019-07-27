package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;
import src.main.java.weapons.playerWeapons.PlayerWeaponParent;

public class EnemyShip {

    // default x position
    double xPos = 1030;

    // defined on instantiation by the controller
    double yPos;

    double xSpeed;
    double ySpeed;

    int currentFrame = 0;
    int maxFrame;

    EnemyType shipType;
    int radius;

    public EnemyShip(int x, int y, double xSpeed, EnemyType type) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        radius = Globals.getEnemyShipRadius(type);
        shipType = type;
        maxFrame = Globals.getEnemyShipsMaxFrames(type);
        init();
    }
    // this class is to be overridden as necessary
    void init() {
    }

    // called every tick by the controller
    // returns true if the ship has fallen off the screen and so should be destroyed
    public boolean update() {
        xPos -= xSpeed;
        yPos -= ySpeed;

        currentFrame = (currentFrame + 1) % maxFrame;

        return checkDead();

    }

    protected boolean checkDead() {
        if (xPos < -20) {
            return true;
        }

        return false;
    }

    public void killShip(int catalistSeperation) {
        Controller.spawnExp((int) xPos, (int) yPos, radius, shipType, catalistSeperation);
        Controller.addKillScore(shipType, catalistSeperation);
    }

    public int getx() {
        return (int) xPos;
    }

    public int gety() {
        return (int) yPos;
    }

    public EnemyType getType() {
        return shipType;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isKillable(Class weapon) {
        return true;
    }

    public boolean collideWithWeapon(PlayerWeaponParent weapon) {

        double dist = distance(weapon.getx(), weapon.gety(), weapon.getRadius(), xPos, yPos, radius);

        double radiiSum = weapon.getRadius() + radius;

        return dist <= radiiSum;

    }

    static double distance(double x1, double y1, int r1, double x2, double y2, int r2) {
        x1 += r1;
        y1 += r1;
        x2 += r2;
        y2 += r2;

        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);

        return Math.sqrt(dx * dx + dy * dy);
    }

    public int getFrame() {
        return currentFrame;
    }


    public void setMaxFrame(int maxFrame) {
        this.maxFrame = maxFrame;
    }

}
	
    

package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;
import src.main.java.weapons.playerWeapons.PlayerWeaponParent;

import java.awt.*;

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
        return xPos < -1 * radius;
    }

    public void killShip(int catalystSeparation) {
        Controller.spawnExp((int) xPos, (int) yPos, xSpeed, ySpeed, radius, shipType, catalystSeparation);
        Controller.addKillScore(shipType, catalystSeparation);
    }

    public int getX() {
        return (int) xPos;
    }

    public int getY() {
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

        double dist = distance(weapon.getX(), weapon.getY(), weapon.getRadius(), xPos, yPos, radius);

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

    protected void setVector(double x, double y, double speed) {

        double xDiff = xPos - x;
        double yDiff = yPos - y;

        double hyp = Math.sqrt(xPos * xPos + yPos * yPos);

        double fraction = speed / hyp;

        xSpeed = fraction * xDiff;
        ySpeed = fraction * yDiff;

    }

    public double[] getSpeed() {
        return new double[]{xSpeed, ySpeed};
    }

}
	
    

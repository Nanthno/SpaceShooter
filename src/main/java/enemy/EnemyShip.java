package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;
import src.main.java.PlayerBullet;

public class EnemyShip {

    // default x position
    double xPos = 1030;

    // defined on instantiation by the controller
    double yPos;

    double xSpeed;
    double ySpeed;

    EnemyType shipType;
    int radius;


    // called every tick by the controller
    // returns true if the ship has fallen off the screen and so should be destroyed
    public boolean updateShip() {
        xPos -= xSpeed;
        yPos -= ySpeed;

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

    public boolean collideWithBullet(PlayerBullet b) {

        double dist = distance(b.getx(), b.gety(), b.getRadius(), xPos,yPos, radius);

        double radiiSum = b.getRadius() + radius;

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

}
	
    

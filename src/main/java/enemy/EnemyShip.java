package src.main.java.enemy;

import src.main.java.Controller;

public class EnemyShip {

    // default x position
    double xPos = 1030;

    // defined on instantiation by the controller
    double yPos;

    double xSpeed;
    double ySpeed;

    EnemyType shipType;
    int radius;

    int catalistSeperation = 0;

    // called every tick by the controller
    // returns true if the ship has fallen off the screen and so should be destroyed
    public boolean updateShip() {
        xPos -= xSpeed;
        yPos += ySpeed;

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


}
	
    

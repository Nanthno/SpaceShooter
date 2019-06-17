package src.main.java.enemy;

import src.main.java.Controller;

public class EnemyShip {

    // default x position
    double xPos = 1030;

    // defined on instantiation by the controller
    double yPos;

    double xSpeed;
    double ySpeed;

    int shipType;
    int radius;

    // called every tick by the controller
    // returns true if the ship has fallen off the screen and so should be destroyed
    public boolean updateShip() {
	xPos -= xSpeed;
	yPos += ySpeed;

	if(xPos < -20) {
	    return true;
	}

	return false;
    }
    public void killShip() {
	Controller.spawnExp((int)xPos, (int)yPos, radius, shipType);
    }

    public int getx() {
	return (int)xPos;
    }
    public int gety() {
	return (int)yPos;
    }
    public int getType() {
	return shipType;
    }
    public int getRadius() {
	return radius;
    }

    
}
	
    

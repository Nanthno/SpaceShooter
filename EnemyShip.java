class EnemyShip {

    // default x position
    int xPos = 1040;

    // defined on instantiation by the controller
    int yPos;

    double xSpeed;
    double ySpeed;

    int shipType;

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
    void killShip() {
	Controller.spawnExp1(xPos, yPos);
    }

    public int getx() {
	return xPos;
    }
    public int gety() {
	return yPos;
    }
    public int getType() {
	return shipType;
    }

    
}
	
    

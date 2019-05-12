class EnemyShip {

    // default x position
    double xPos = 1040;

    // defined on instantiation by the controller
    double yPos;

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
	Controller.spawnExp1((int)xPos, (int)yPos);
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

    
}
	
    

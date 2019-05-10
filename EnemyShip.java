class EnemyShip {

    // default x position
    int xPos = 1040;

    // defined on instantiation by the controller
    int yPos;

    double xSpeed;
    double ySpeed;

    static final double maxSpeed = 2.5;
    static final double minSpeed = 1;

    public EnemyShip(int y, double xSpeed, double ySpeed) {
	yPos = y;
	this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;

    }


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

    public int getx() {
	return xPos;
    }
    public int gety() {
	return yPos;
    }
}
	
    

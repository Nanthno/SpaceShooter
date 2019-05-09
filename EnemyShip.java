class EnemyShip {

    // default x position
    int xPos = 400;

    // defined on instantiation by the controller
    int yPos;

    int xSpeed;
    int ySpeed;

    public static EnemyShip(int y, int xSpeed, int ySpeed) {
	yPos = y;
	this.xSpeed = xSpeed;
	this.ySpeed = ySpeed;

    }


    // called every tick by the controller
    public void updateShip() {
	xPos -= xSpeed;
	yPos += ySpeed;
    }
}
	
    

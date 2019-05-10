class PlayerShip {

    double xPos = 30;
    double yPos = GraphicsManager.WIDTH/2;
    double xSpeed = 3;
    double ySpeed = 3;


    // manages how fast the player can fire
    int maxFire = 15;
    int fire = 0;

    Input in;
    
    public PlayerShip() {
	in = new Input();
    }

    public double getx() {
	return xPos;
    }
    public double gety() {
	return yPos;
    }

    public void update() {
	fire--;
	
	if(in.up)
	    yPos -= ySpeed;
	if(in.down)
	    yPos += ySpeed;
	if(in.right)
	    xPos += xSpeed;
	if(in.left)
	    xPos -= xSpeed;
	if(in.fire && fire < 0) {
	    Controller.addBullet(new PlayerBullet(xPos+5, yPos+7, 5, 0));
	    fire = maxFire;
	}
	
	    
    }
}

class PlayerShip {

    double xPos = 30;
    double yPos = GraphicsManager.WIDTH/2;
    double xSpeed = 3;
    double ySpeed = 3;

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
	if(in.up)
	    yPos -= ySpeed;
	if(in.down)
	    yPos += ySpeed;
	if(in.right)
	    xPos += xSpeed;
	if(in.left)
	    xPos -= xSpeed;
    }
}

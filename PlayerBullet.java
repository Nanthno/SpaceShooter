class PlayerBullet {

    double xPos;
    double yPos;

    double xSpeed;
    double ySpeed;

    public PlayerBullet(double x, double y, double dx, double dy) {
	xPos = x;
	yPos = y;
	xSpeed = dx;
	ySpeed = dy;
    }
    
    public void update() {
	xPos += xSpeed;
	yPos -= ySpeed;
    }
    
    public double getx() {
	return xPos;
    }
    public double gety() {
	return yPos;
    }
}

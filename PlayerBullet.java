class PlayerBullet {

    double xPos = -1;
    double yPos = -1;

    double xSpeed;
    double ySpeed;

    int radius = 2;

    public PlayerBullet(double x, double y, double dx, double dy) {
	xPos = x;
	yPos = y;
	xSpeed = dx;
	ySpeed = dy;
    }
    
    public void update() {
	xPos += xSpeed;
	yPos += ySpeed;
    }
    
    public double getx() {
	return xPos;
    }
    public double gety() {
	return yPos;
    }
    public int getRadius() {
	return radius;
    }
}

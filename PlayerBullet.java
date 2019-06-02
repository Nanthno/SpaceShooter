class PlayerBullet extends WeaponParent {

    public PlayerBullet(double x, double y, double dx, double dy) {
	xPos = x;
	yPos = y;
	xSpeed = dx;
	ySpeed = dy;

	radius = 2;
    }
    
    public void update() {
	xPos += xSpeed;
	yPos += ySpeed;
    }
    
}

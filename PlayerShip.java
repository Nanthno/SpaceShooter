class PlayerShip {

    int health = 100;
    int maxHealth = 100;

    double xPos = 30;
    double yPos = GraphicsManager.WIDTH/2;
    double xSpeed = 3;
    double ySpeed = 3;


    // manages how fast the player can fire
    int maxFire = 60;
    int fire = 0;

    final int radius = 16;

    Input in;
    
    public PlayerShip() {
	in = new Input();
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

    void shipCollision() {
	health -= 10;
    }
    

    public double getx() {
	return xPos;
    }
    public double gety() {
	return yPos;
    }
    
    int getHealth() {
	return health;
    }
    int getMaxHealth() {
	return maxHealth;
    }
    int getRadius() {
	return radius;
    }
}

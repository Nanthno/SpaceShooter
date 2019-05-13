class PlayerShip {

    int health = 100;
    int maxHealth = 100;

    double xPos = 30;
    double yPos = GraphicsManager.WIDTH/2;
    double xSpeed = 3;
    double ySpeed = 3;


    // manages how fast the player can fire
    int maxFire = 10;
    int fire = 0;
    int fireHeat = 20;
    int overHeat = 40;
    int cooloff = 1;
    int heat = 70;
    boolean overheated = false;

    final int radius = 16;

    Input in;
    
    public PlayerShip() {
	in = new Input();
    }

    public void update() {
	fire--;
	if(heat > 0)
	    heat--;
	
	if(heat < cooloff) {
	    overheated = false;
	}
	
	if(in.up && yPos > 0)
	    yPos -= ySpeed;
	if(in.down && yPos < GraphicsManager.HEIGHT)
	    yPos += ySpeed;
	if(in.right && xPos < GraphicsManager.WIDTH)
	    xPos += xSpeed;
	if(in.left && xPos > 0)
	    xPos -= xSpeed;
	if(in.fire && fire < 0 && !overheated) {
	    Controller.addBullet(new PlayerBullet(xPos+5, yPos+7, 5, 0));
	    fire = maxFire;
	    heat += fireHeat;
	    if(heat > overHeat) {
		overheated = true;
	    }
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
    int getHeat() {
	return heat;
    }
    int getMaxHeat() {
	return overHeat;
    }
}

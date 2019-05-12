class Enemy0 extends EnemyShip {

    
    static final double maxSpeed = 2.5;
    static final double minSpeed = 1;

    public Enemy0(int y, double xSpeed) {
	yPos = y;
	this.xSpeed = xSpeed;
	shipType = 0;
	radius = 8;
    }

}


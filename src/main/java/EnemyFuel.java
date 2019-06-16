package src.main.java;

class EnemyFuel extends EnemyShip {

    
    static final double maxSpeed = 1.5;
    static final double minSpeed = 1;

    public EnemyFuel(int y, double xSpeed) {
	yPos = y;
	this.xSpeed = xSpeed;
	shipType = 1;
	radius = 16;
    }

}


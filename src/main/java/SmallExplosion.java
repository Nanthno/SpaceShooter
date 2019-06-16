package src.main.java;

class SmallExplosion extends Explosion {
    public SmallExplosion(int x, int y) {
	xPos = x;
	yPos = y;
	maxStage = 10;
	maxDuration = 35;
	duration = maxDuration;
	radius = 8;
	expType = 0;
    }
    
}

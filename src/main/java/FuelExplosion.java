package src.main.java;

class FuelExplosion extends Explosion {

    public FuelExplosion(int x, int y, int catalistSeperation) {
	xPos = x;
	yPos = y;
	maxStage = 7;
	maxDuration = 40;
	duration = maxDuration;
	radius = 32;
	expType = 1;
	this.catalystSeparation = catalistSeperation;
    }
}

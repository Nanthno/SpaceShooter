class Explosion {

    int xPos = -1;
    int yPos = -1;
    
    int maxDuration = 45;
    int duration = maxDuration;

    int stage = 0;
    int maxStage = 10;

    public Explosion(int x, int y) {
	xPos = x;
	yPos = y;
    }

    // returns true if the explosion should be destroyed
    boolean update() {
	duration--;
	stage = stage = maxStage - duration/(maxDuration/maxStage);
	if(stage < 0) {
	    stage = 0;
	}

	if(duration < 0) {
	    return true;
	}
	return false;
    }

    int getx() {
	return xPos;
    }
    int gety() {
	return yPos;
    }
    int getStage() {
	return stage;
    }
}

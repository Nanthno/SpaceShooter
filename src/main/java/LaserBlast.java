package src.main.java;

public class LaserBlast extends WeaponParent {
    
    public LaserBlast(int x) {
	xPos = x;

	radius = 3;
	stage = 0;
	effectiveStage = 2;
	maxStage = 7;
	
    }

    
    // returns true if the blast should be destroyed
    public boolean update() {
	stage++;
	if(stage == maxStage) {
	    return true;
	}
	return false;
    }
}

package src.main.java;

public class WeaponParent {

    WeaponType type;

    double xPos;
    double yPos;

    double xSpeed;
    double ySpeed;

    int radius;

    int stage;
    int effectiveStage;
    int maxStage;

    int currentFrame = 0;
    int maxFrame;

    public int getx() {
	return (int)xPos;
    }
    public int gety() {
	return (int)yPos;
    }
    public int getRadius() {
	return radius;
    }

    public int getFrame() {
        return currentFrame;
    }
	
}

package src.main.java;

public class Explosion {

    int xPos = -1;
    int yPos = -1;

    int maxDuration;
    int duration;

    int stage = 0;
    int maxStage;

    int radius;

    int expType;

    int catalystSeparation = 0;

    static int[] expRadiusArray = new int[]{8, 32, 24};

    // returns true if the explosion should be destroyed
    boolean update() {
        duration--;
        stage = stage  = maxStage - duration / (maxDuration / maxStage);
        if (stage < 0) {
            stage = 0;
        }

        if (duration < 0) {
            return true;
        }
        return false;
    }

    public int getx() {
        return xPos;
    }

    public int gety() {
        return yPos;
    }

    public int getStage() {
        return stage;
    }

    int getRadius() {
        return radius;
    }

    public int getCatalystSeparation() {
        return catalystSeparation;
    }

    public int getExpType() {
        return expType;
    }
}

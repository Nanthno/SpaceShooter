package src.main.java;

class Explosion {

    int xPos = -1;
    int yPos = -1;

    int maxDuration;
    int duration;

    int stage = 0;
    int maxStage;

    int radius;

    int expType;

    static int[] expRadiusArray = new int[]{8, 32};

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

    int getx() {
        return xPos;
    }

    int gety() {
        return yPos;
    }

    int getStage() {
        return stage;
    }

    int getRadius() {
        return radius;
    }
}

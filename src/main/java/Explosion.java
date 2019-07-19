package src.main.java;

import java.util.Map;

public class Explosion {

    int xPos = -1;
    int yPos = -1;

    int maxDuration;
    int duration;

    int stage = 0;
    int maxStage;

    int radius;

    ExplosionType expType;

    int catalystSeparation = 0;

    static Map<ExplosionType, Integer> expRadii = Map.of(
            ExplosionType.SMALL, 8,
            ExplosionType.MEDIUM, 24,
            ExplosionType.FUEL, 32
    );
    static Map<ExplosionType, Integer> expDuration = Map.of(
            ExplosionType.SMALL, 35,
            ExplosionType.MEDIUM, 40,
            ExplosionType.FUEL, 40
    );

    public Explosion(int x, int y, int catalistSeperation, ExplosionType type) {
        xPos = x;
        yPos = y;
        expType = type;
        this.catalystSeparation = catalistSeperation;
        radius = expRadii.get(type);
        maxStage = Globals.getExplosionMaxFrame(type);
        maxDuration = expDuration.get(type);
        duration = maxDuration;
    }

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

    public ExplosionType getExpType() {
        return expType;
    }
}

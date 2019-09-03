package src.main.java;

import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Explosion {

    int xPos = -1;
    int yPos = -1;

    double xSpeed;
    double ySpeed;

    int maxDuration;
    int duration;

    int stage = 0;
    int effectiveStage = 2;
    int maxStage;

    int radius;

    int rotation = -1;

    ExplosionType expType;

    int catalystSeparation = 0;

    static final Map<ExplosionType, Integer> effectiveStageTypes = new HashMap<ExplosionType, Integer>() {{
        put(ExplosionType.PROJECTILE, 3);
    }};

    static Map<ExplosionType, Integer> expRadii = new HashMap<ExplosionType, Integer>() {{
        put(ExplosionType.SMALL, 8);
        put(ExplosionType.MEDIUM, 24);
        put(ExplosionType.FUEL, 32);
        put(ExplosionType.PROJECTILE, 48);
    }};
    static Map<ExplosionType, Integer> expDuration = new HashMap<ExplosionType, Integer>() {{
        put(ExplosionType.SMALL, 35);
        put(ExplosionType.MEDIUM, 40);
        put(ExplosionType.FUEL, 40);
        put(ExplosionType.PROJECTILE, 40);
    }};

    //TODO: migrate instantiation to the other constructor
    public Explosion(int x, int y, double xSpeed, double ySpeed, int catalystSeparation, ExplosionType type) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        expType = type;
        this.catalystSeparation = catalystSeparation;
        radius = expRadii.get(type);
        maxStage = Globals.getExplosionMaxFrame(type) - 1;
        maxDuration = expDuration.get(type);
        duration = maxDuration;
        if (effectiveStageTypes.containsKey(type))
            effectiveStage = effectiveStageTypes.get(type);
    }

    public Explosion(EnemyShip ship, int catalystSeparation) {
        double[] speed = ship.getSpeed();
        new Explosion(ship.getX(), ship.getY(), speed[0], speed[1], catalystSeparation, EnemyType.getExplosionType(ship.getType()));
    }

    // returns true if the explosion should be destroyed
    boolean update() {
        xPos -= xSpeed;
        yPos -= ySpeed;

        xSpeed /= 2;
        ySpeed /= 4;

        duration--;
        stage = maxStage - duration / (maxDuration / maxStage);
        if (stage < 0) {
            stage = 0;
        }

        return duration < 0;
    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public int getStage() {
        return stage;
    }

    public int getEffectiveStage() {
        return effectiveStage;
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

    public int getRotation() {
        if (expType == ExplosionType.MEDIUM || expType == ExplosionType.SMALL) {
            if (rotation == -1) {
                Random rand = new Random();
                rotation = rand.nextInt(3);
            }
            return rotation;
        }

        return 0;
    }
}

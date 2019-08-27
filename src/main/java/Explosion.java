package src.main.java;

import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import java.util.Map;
import java.util.Random;

public class Explosion {

    int xPos = -1;
    int yPos = -1;

    int maxDuration;
    int duration;

    int stage = 0;
    int effectiveStage = 2;
    int maxStage;

    int radius;

    int rotation = -1;

    ExplosionType expType;

    int catalystSeparation = 0;

    static final Map<ExplosionType, Integer> effectiveStageTypes = Map.of(
        ExplosionType.PROJECTILE, 3
    );

    static Map<ExplosionType, Integer> expRadii = Map.of(
            ExplosionType.SMALL, 8,
            ExplosionType.MEDIUM, 24,
            ExplosionType.FUEL, 32,
            ExplosionType.PROJECTILE, 48
    );
    static Map<ExplosionType, Integer> expDuration = Map.of(
            ExplosionType.SMALL, 35,
            ExplosionType.MEDIUM, 40,
            ExplosionType.FUEL, 40,
            ExplosionType.PROJECTILE, 40
    );

    //TODO: migrate instantiaion to the other constructor
    public Explosion(int x, int y, int catalystSeparation, ExplosionType type) {
        xPos = x;
        yPos = y;
        expType = type;
        this.catalystSeparation = catalystSeparation;
        radius = expRadii.get(type);
        maxStage = Globals.getExplosionMaxFrame(type)-1;
        maxDuration = expDuration.get(type);
        duration = maxDuration;
        if(effectiveStageTypes.containsKey(type))
            effectiveStage = effectiveStageTypes.get(type);
    }

    public Explosion(EnemyShip ship, int catalystSeparation) {
        xPos = ship.getx();
        yPos = ship.gety();
        expType = EnemyType.getExplosionType(ship.getType());
        this.catalystSeparation = catalystSeparation;
        radius = expRadii.get(expType);
        maxStage = Globals.getExplosionMaxFrame(expType)-1;
        maxDuration = expDuration.get(expType);
        duration = maxDuration;
        if(effectiveStageTypes.containsKey(expType))
            effectiveStage = effectiveStageTypes.get(expType);
    }

    // returns true if the explosion should be destroyed
    boolean update() {
        duration--;
        stage = maxStage - duration / (maxDuration / maxStage);
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
        if(expType == ExplosionType.MEDIUM || expType == ExplosionType.SMALL) {
            if(rotation == -1) {
                Random rand = new Random();
                rotation = rand.nextInt(3);
            }
            return rotation;
        }

        return 0;
    }
}

package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;
import src.main.java.density.DensityMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnemyAgile extends EnemyShip {

    private double speed;
    private static final int pointOfNoReturn = 50;
    private static int initialMovement = 100;

    public EnemyAgile(int x, int y, double speed) {
        super(x, y, speed, EnemyType.AGILE);
    }

    @Override
    void init() {
        speed = xSpeed;
    }

    @Override
    public boolean update() {
        int[] targetPos = findTargetPos();
        if (xPos < pointOfNoReturn || initialMovement > 0) {
            xSpeed = speed;
            ySpeed = 0;

            initialMovement -= speed;
        } else {
            setVector(targetPos[0], targetPos[1], speed);
        }

        xPos -= xSpeed;
        yPos -= ySpeed;

        if (yPos < radius) {
            yPos = radius;
        } else if (yPos > Globals.gameHeight - radius) {
            yPos = Globals.gameHeight - radius;
        }

        return checkDead();
    }

    private int[] findTargetPos() {
        DensityMap densityMap = Controller.getDensityMap();

        Map<int[], Double> holes = densityMap.getHoles();

        List<int[]> holeKeys = new ArrayList<>(holes.keySet());

        int[] targetPos = new int[2];
        double targetPosValue = -1;
        for (int[] hole : holeKeys) {
            double newTargetPosValue = calcPosValue(hole, holes.get(hole));
            if (targetPosValue < newTargetPosValue) {
                targetPos = hole;
                targetPosValue = newTargetPosValue;
            }
        }
        if (targetPosValue == -1) {
            return new int[]{0, (int) yPos};
        }
        return targetPos;
    }

    private double calcPosValue(int[] pos, double posDensity) {
        if (pos[0] > xPos) {
            return -1;
        }

        double dist = Globals.distance(pos, new int[]{(int) xPos, (int) yPos});

        return posDensity / dist;
    }

    @Override
    public double[] getSpeed() {
        return new double[]{0, 0};
    }

}

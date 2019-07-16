package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;
import src.main.java.density.DensityMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnemyAgile extends EnemyShip {

    double speed;

    // for testing
    public static void main(String[] args) {
        EnemyAgile agile = new EnemyAgile(8, -5, 3);
        agile.setVector(new int[]{3, -7});
        System.out.println(agile.xSpeed);
        System.out.println(agile.ySpeed);
    }

    public EnemyAgile(int x, int y, double speed) {
        yPos = y;
        xPos = x;
        this.speed = speed;
        radius = Globals.enemyAgileRadius;
        shipType = EnemyType.AGILE;
    }

    @Override
    public boolean updateShip() {
        int[] targetPos = findTargetPos();

        setVector(targetPos);

        xPos -= xSpeed;
        yPos += ySpeed;

        // TODO: could make this more efficient by storing maximum and minimum ship positions in global
        if(yPos < radius) {
            yPos = radius;
        }
        else if(yPos > Globals.screenHeight-radius) {
            yPos = Globals.screenHeight-radius;
        }

        return checkDead();
    }

    private void setVector(int[] targetPos) {

        // yes, the subtraction reverses
        double xDiff = xPos - targetPos[0];
        double yDiff = targetPos[1] - yPos;

        double bearing = Math.atan(yDiff / xDiff);

        xSpeed = speed * Math.cos(bearing);
        ySpeed = speed * Math.sin(bearing);
    }

    private int[] findTargetPos() {
        DensityMap densityMap = Controller.getDensityMap();

        Map<int[], Double> holes = densityMap.getHoles();

        List<int[]> holeKeys = new ArrayList<>();
        holeKeys.addAll(holes.keySet());

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

        double posValue = posDensity / dist;

        return posValue;
    }

}

package src.main.java;


import src.main.java.enemy.EnemyType;

import java.util.Map;

public class Globals {

    public static final int screenHeight = 640;
    public static final int screenWidth = 1024;

    public static final int enemyBasicRadius = 8;
    public static final int enemyAgileRadius = 12;
    public static final int enemyFuelRadius = 16;

    static final Map<EnemyType, Integer> enemyShipRadius = Map.of(
            EnemyType.BASIC, enemyBasicRadius,
            EnemyType.FUEL, enemyFuelRadius,
            EnemyType.AGILE, enemyAgileRadius);


    static final Map<EnemyType, Integer> enemyShipPointValue = Map.of(
            EnemyType.BASIC, 100,
            EnemyType.FUEL, 200,
            EnemyType.AGILE, 300);

    public static int getEnemyShipRadius(EnemyType type) {
        return enemyShipRadius.get(type);
    }

    public static int getEnemyShipPointValue(EnemyType type) {
        return enemyShipPointValue.get(type);
    }

    public static double distance(int[] pointA, int[] pointB) {
        int x1 = pointA[0];
        int y1 = pointA[1];
        if (pointA.length > 2) {
            int r1 = pointA[2];
            x1 += r1;
            y1 += r1;
        }
        int x2 = pointB[0];
        int y2 = pointB[1];
        if (pointB.length > 2) {
            int r2 = pointB[2];
            y2 += r2;
            x2 += r2;
        }

        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);

        return Math.sqrt(dx * dx + dy * dy);
    }
}

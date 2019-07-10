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
            EnemyType.FUEL, enemyFuelRadius);

    static final Map<EnemyType, Integer> enemyShipPointValue = Map.of(
            EnemyType.BASIC, 100,
            EnemyType.FUEL, 200);

    public static int getEnemyShipRadius(EnemyType type) {
        return enemyShipRadius.get(type);
    }

    public static int getEnemyShipPointValue(EnemyType type) {
        return enemyShipPointValue.get(type);
    }
}

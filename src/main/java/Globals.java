package src.main.java;


import src.main.java.enemy.EnemyType;

import java.util.HashMap;
import java.util.Map;

public class Globals {

    public static final int enemyBasicRadius = 8;
    public static final int enemyFuelRadius = 16;

    static final Map<EnemyType, Integer> enemyShipRadius = Map.of(
            EnemyType.BASIC, enemyBasicRadius,
            EnemyType.FUEL, enemyFuelRadius);

    public static int getEnemyShipRadius(EnemyType type) {
        return enemyShipRadius.get(type);
    }
}

package src.main.java.enemy;

import src.main.java.Globals;

import java.util.Map;

public enum EnemyType {

    BASIC,
    FUEL;

    Map<String, String> enemyHash = Map.of(
            "b", "BASIC",
            "f", "FUEL"
    );

    static Map<EnemyType, Integer> explosionTypes = Map.of(
            BASIC, 0,
            FUEL, 1);

    public EnemyType getType(String e) {
        return this.valueOf(enemyHash.get(e));
    }

    public static int getExplosionType(EnemyType type) {
        return explosionTypes.get(type);
    }

}

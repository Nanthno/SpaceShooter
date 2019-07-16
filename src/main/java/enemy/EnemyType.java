package src.main.java.enemy;

import java.util.Map;

public enum EnemyType {

    BASIC,
    FUEL,
    AGILE,
    SHIELD,
    SHIELDER;

    static Map<String, String> enemyHash = Map.of(
            "b", "BASIC",
            "f", "FUEL",
            "a", "AGILE",
            "s", "SHIELDER"
    );

    static Map<EnemyType, Integer> explosionTypes = Map.of(
            BASIC, 0,
            FUEL, 1,
            AGILE, 2,
            SHIELDER, 1,
            SHIELD, 0);

    public static EnemyType getType(String e) {
        return valueOf(enemyHash.get(e));
    }

    public static int getExplosionType(EnemyType type) {
        return explosionTypes.get(type);
    }

}

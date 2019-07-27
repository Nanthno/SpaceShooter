package src.main.java.enemy;

import src.main.java.ExplosionType;

import java.util.Map;

public enum EnemyType {

    BASIC,
    FUEL,
    AGILE,
    SHIELD,
    SHIELDER,
    ARMORED1,
    SHOOTER,
    PILOTED;

    static Map<String, String> enemyHash = Map.of(
            "b", "BASIC",
            "f", "FUEL",
            "a", "AGILE",
            "s", "SHIELDER",
            "1", "ARMORED1",
            "g", "SHOOTER", // 'g' as in gun
            "p", "PILOTED" // 'p' as in piloted
    );

    static Map<EnemyType, ExplosionType> explosionTypes = Map.of(
            BASIC, ExplosionType.SMALL,
            FUEL, ExplosionType.FUEL,
            AGILE, ExplosionType.MEDIUM,
            SHIELDER, ExplosionType.MEDIUM,
            SHIELD, ExplosionType.SMALL,
            ARMORED1, ExplosionType.MEDIUM,
            SHOOTER, ExplosionType.MEDIUM,
            PILOTED, ExplosionType.MEDIUM);

    public static EnemyType getType(String e) {
        return valueOf(enemyHash.get(e));
    }

    public static ExplosionType getExplosionType(EnemyType type) {
        return explosionTypes.get(type);
    }

}

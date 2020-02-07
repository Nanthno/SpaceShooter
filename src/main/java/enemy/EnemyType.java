package src.main.java.enemy;

import src.main.java.ExplosionType;

import java.util.HashMap;
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

    private static final Map<String, String> enemyHash = new HashMap<String, String>() {{
        put("b", "BASIC");
        put("f", "FUEL");
        put("a", "AGILE");
        put("s", "SHIELDER");
        put("1", "ARMORED1");
        put("g", "SHOOTER"); // 'g' as in gun
        put("p", "PILOTED");
    }};

    private static final Map<EnemyType, ExplosionType> explosionTypes = new HashMap<EnemyType, ExplosionType>() {{
        put(BASIC, ExplosionType.SMALL);
        put(FUEL, ExplosionType.FUEL);
        put(AGILE, ExplosionType.MEDIUM);
        put(SHIELDER, ExplosionType.MEDIUM);
        put(SHIELD, ExplosionType.SMALL);
        put(ARMORED1, ExplosionType.MEDIUM);
        put(SHOOTER, ExplosionType.SMALL);
        put(PILOTED, ExplosionType.MEDIUM);
    }};

    public static EnemyType getType(String e) {
        return valueOf(enemyHash.get(e));
    }

    public static ExplosionType getExplosionType(EnemyType type) {
        return explosionTypes.get(type);
    }

}

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

    public EnemyType getType(String e) {
        return this.valueOf(enemyHash.get(e));
    }

}

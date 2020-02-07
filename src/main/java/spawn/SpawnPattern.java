package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.ArrayList;
import java.util.List;

class SpawnPattern {

    private final List<SpawnCluster> spawnClusters = new ArrayList<SpawnCluster>();

    List<EnemyShip> makePattern() {
        List<EnemyShip> ships = new ArrayList<>();
        for (SpawnCluster cluster : spawnClusters) {
            ships.addAll(cluster.makeSpawns());
        }

        return ships;

    }

}

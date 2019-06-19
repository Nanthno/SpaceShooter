package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SpawnController {

    SpawnPattern[] patterns;
    double[] patternProbability;

    static HashMap<String, SpawnCluster> allClusters;

    public SpawnController() {
        allClusters = spawnUtil.createSpawnClusters();
    }

    public static SpawnCluster getSpawnCluster(String clusterID) {
        return allClusters.get(clusterID);
    }

    public List<EnemyShip> onTick() {

        Random rand = new Random();

        int y = rand.nextInt(500);

        return allClusters.get("AA")
                .makeSpawns(1049, y);
    }


}

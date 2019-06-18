package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnController {

    SpawnPattern[] patterns;
    double[] patternProbability;

    static HashMap<String, SpawnCluster> allClusters;

    public SpawnController() {
        allClusters = spawnUtil.createSpawnClusters();

        patterns = spawnUtil.createSpawnPatterns(allClusters);

        patternProbability = new double[patterns.length];
        for(int i = 0; i < patterns.length; i++) {
            patternProbability[i] = 0.1;
        }
    }

    public static SpawnCluster getSpawnCluster(String clusterID) {
        return allClusters.get(clusterID);
    }

    List<EnemyShip> onTick() {


        return null;
    }



}

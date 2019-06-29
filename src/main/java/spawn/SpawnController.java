package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.*;

public class SpawnController {

    static int minY = 100;
    static int maxY = 425;

    //static float AASpawnChance = 0.01F;
    //static float ABSpawnChance = 0.04F;

    Map<String, Double> spawnProbabilities = new HashMap<>();

    static HashMap<String, SpawnCluster> allClusters;

    public SpawnController() {
        allClusters = ClusterUtil.createSpawnClusters();
    }

    public static SpawnCluster getSpawnCluster(String clusterID) {
        return allClusters.get(clusterID);
    }

    public List<EnemyShip> onTick() {

        List<EnemyShip> enemiesToSpawn = new ArrayList<>();

        Random rand = new Random();

        for (String clusterCode : spawnProbabilities.keySet()) {
            if (rand.nextDouble() < spawnProbabilities.get(clusterCode)) {
                int y = chooseY();

                enemiesToSpawn.addAll(allClusters.get(clusterCode).makeSpawns(1049, y));
            }
        }

        return enemiesToSpawn;
    }

    private int chooseY() {
        Random rand = new Random();
        int y = rand.nextInt(maxY + minY) - minY;
        return y;
    }

    public void updateSpawnProbabilities(TimeStampEvent event) {
        Map<String, Double> newSpawnProbabilities = event.getSpawnProbabilityMap();
        for (String clusterCode : newSpawnProbabilities.keySet()) {
            spawnProbabilities.put(clusterCode, newSpawnProbabilities.get(clusterCode));
        }
    }
}

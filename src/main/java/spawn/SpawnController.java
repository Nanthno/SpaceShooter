package src.main.java.spawn;

import src.main.java.Globals;
import src.main.java.enemy.EnemyShip;

import java.util.*;

public class SpawnController {

    static int minY = 32;
    static int maxY = Globals.screenHeight - 32;

    Map<String, Double> spawnProbabilities = new HashMap<>();

    static HashMap<String, SpawnCluster> allClusters;
    private static long startTime = 0;

    public SpawnController() {
        startTime = System.currentTimeMillis();

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
                enemiesToSpawn.addAll(allClusters.get(clusterCode).makeSpawns(minY, maxY));
            }
        }

        return enemiesToSpawn;
    }

    public void updateSpawnProbabilities(TimeStampEvent event) {
        Map<String, Double> newSpawnProbabilities = event.getSpawnProbabilityMap();
        for (String clusterCode : newSpawnProbabilities.keySet()) {
            spawnProbabilities.put(clusterCode, newSpawnProbabilities.get(clusterCode));
        }
    }
    public Queue<TimeStampEvent> updateSpawnProbabilities(Queue<TimeStampEvent> events) {
        TimeStampEvent event = events.peek();

        long secondsPassed = (System.currentTimeMillis() - startTime)/1000;

        if(event.getTriggerTime() < secondsPassed) {
            events.remove();

            updateSpawnProbabilities(event);
        }
        return events;
    }

    public static void setStartTime(long startTime) {
        SpawnController.startTime = startTime;
    }
}

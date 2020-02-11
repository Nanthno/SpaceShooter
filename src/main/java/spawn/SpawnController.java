package src.main.java.spawn;

import src.main.java.Globals;
import src.main.java.enemy.EnemyShip;

import java.util.*;

public class SpawnController {

    private final Map<String, Double> spawnProbabilities = new HashMap<>();

    private static HashMap<String, SpawnCluster> allClusters;
    private static long startTime = 0;

    private static long lastShieldSpawn;

    private double speedMultiplier = 1;
    private double speedIncrease = 0;

    public SpawnController() {
        startTime = System.currentTimeMillis();

        allClusters = ClusterUtil.createSpawnClusters();
    }

    public static SpawnCluster getSpawnCluster(String clusterID) {
        return allClusters.get(clusterID);
    }

    public List<EnemyShip> onTick() {

        speedMultiplier += speedIncrease;

        Long time = System.currentTimeMillis();

        List<EnemyShip> enemiesToSpawn = new ArrayList<>();

        Random rand = new Random();

        for (String clusterCode : spawnProbabilities.keySet()) {
            if (rand.nextDouble() < spawnProbabilities.get(clusterCode)) {
                String shieldClusterCode = "OS";
                if (clusterCode.equals(shieldClusterCode)) {
                    // in milliseconds
                    int shieldSpawnMinDelay = 1000;
                    if (lastShieldSpawn - shieldSpawnMinDelay < lastShieldSpawn)
                        continue;

                    lastShieldSpawn = time;
                }
                int maxY = Globals.gameHeight - 32;
                int minY = 32;
                enemiesToSpawn.addAll(allClusters.get(clusterCode).makeSpawns(minY, maxY, speedMultiplier));
            }
        }
        return enemiesToSpawn;
    }

    public void updateSpawnProbabilities(TimeStampEvent event) {
        Map<String, Double> newSpawnProbabilities = event.getSpawnProbabilityMap();
        for (String clusterCode : newSpawnProbabilities.keySet()) {
            spawnProbabilities.put(clusterCode, newSpawnProbabilities.get(clusterCode));
        }

        speedMultiplier = event.getSpeedMultiplier();
        speedIncrease = event.getSpeedIncrease();
    }

    public Queue<TimeStampEvent> updateSpawnProbabilities(Queue<TimeStampEvent> events) {
        TimeStampEvent event = events.peek();

        long secondsPassed = (System.currentTimeMillis() - startTime) / 1000;

        if (Objects.requireNonNull(event).getTriggerTime() < secondsPassed) {
            events.remove();

            updateSpawnProbabilities(event);
        }
        return events;
    }

    public static void setStartTime(long startTime) {
        SpawnController.startTime = startTime;
    }
}

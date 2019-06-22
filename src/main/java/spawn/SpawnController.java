package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SpawnController {

    static int minY = 100;
    static int maxY = 425;

    static float AASpawnChance = 0.01F;
    static float ABSpawnChance = 0.04F;

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

        List<EnemyShip> enemiesToSpawn = new ArrayList<>();

        Random rand = new Random();

        if(rand.nextFloat() < AASpawnChance) {


            int y = chooseY();
            enemiesToSpawn.addAll(allClusters.get("AA").makeSpawns(1049, y));
        }
        if(rand.nextFloat() < ABSpawnChance) {

            int y = chooseY();
            enemiesToSpawn.addAll(allClusters.get("AB").makeSpawns(1049, y));
        }

        return enemiesToSpawn;
    }

    private int chooseY() {
        Random rand = new Random();
        int y = rand.nextInt(maxY + minY) - minY;
        return y;
    }
}

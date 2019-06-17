package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnController {

    SpawnPattern[] patterns;
    double[] patternProbability;

    public SpawnController() {
         HashMap<String, SpawnCluster> clusters = spawnUtil.createSpawnClusters();

        patterns = spawnUtil.createSpawnPatterns(clusters);

        patternProbability = new double[patterns.length];
        for(int i = 0; i < patterns.length; i++) {
            patternProbability[i] = 0.1;
        }
    }


    List<EnemyShip> onTick() {


        return null;
    }



}

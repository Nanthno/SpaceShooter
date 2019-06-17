package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SpawnCluster {

    int xPos;
    int yPos;
    int positionVariance;

    List<Spawn> spawns = new ArrayList<Spawn>();


    // used for finishing creation of the cluster by adding other clusters
    int spacing;
    Map<String, String> otherClusters = new HashMap<>();

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
    public void addOtherCluster(String position, String clusterID) {
        otherClusters.put(position, clusterID);
    }
    public void addSpawn(Spawn spawn) {
        spawns.add(spawn);
    }

    List<EnemyShip> makeSpawns() {
       List<EnemyShip> ships = new ArrayList<EnemyShip>();


        for(Spawn spawn : spawns) {
            ships.add(spawn.makeEnemy(xPos, yPos));
        }

        return ships;
    }


}

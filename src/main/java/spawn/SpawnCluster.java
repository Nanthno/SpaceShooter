package src.main.java.spawn;

import src.main.java.enemy.EnemyShip;

import java.util.*;

class SpawnCluster {

    int xPos;
    int yPos;
    int positionVariance;

    List<Spawn> spawns = new ArrayList<>();


    // used for finishing creation of the cluster by adding other clusters
    int spacing;
    Map<String, List<int[]>> nestedClusters = new HashMap<>();

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public void addOtherCluster(String position, String clusterID) {
        String[] splitPosition = position.split(",");
        int x = Integer.parseInt(splitPosition[0]);
        int y = Integer.parseInt(splitPosition[1]);
        int[] formattedPosition = new int[]{x, y};

        if (nestedClusters.containsKey(clusterID))
            nestedClusters.get(clusterID).add(formattedPosition);
        else {
            List<int[]> newNestedCluster = new ArrayList<>();
            newNestedCluster.add(formattedPosition);
            nestedClusters.put(clusterID, newNestedCluster);
        }
    }

    public void addSpawn(Spawn spawn) {
        spawns.add(spawn);
    }

    List<EnemyShip> makeSpawns() {

        List<EnemyShip> ships = new ArrayList<>();

        for (Spawn spawn : spawns) {
            ships.add(spawn.makeEnemy(xPos, yPos));
        }

        ships.addAll(unpackOtherClusters());

        return ships;
    }

    List<EnemyShip> makeSpawns(int xCenter, int yCenter) {

        List<EnemyShip> ships = new ArrayList<>();

        int xOffset = 0;
        int yOffset = 0;

        // finds the offset by which to shift the ships such that they are centered properly
        for (Spawn spawn : spawns) {
            int x = spawn.getxPos();
            int y = spawn.getyPos();
            if (x > xOffset)
                xOffset = x;
            if (y > yOffset)
                yOffset = y;

        }

        // center of spawn cluster
        xOffset /= 2;
        yOffset /= 2;

        xOffset += xCenter;
        yOffset += yCenter;

        for (Spawn spawn : spawns) {
            ships.add(spawn.makeEnemy(xPos + xOffset, yPos + yOffset));
        }

        ships.addAll(unpackOtherClusters());

        return ships;

    }

    private List<EnemyShip> unpackOtherClusters() {
        Set<String> clusterIDList = nestedClusters.keySet();
        List<EnemyShip> nestedSpawns = new ArrayList<>();

        for (String id : clusterIDList) {
            List<int[]> clusterPositions = nestedClusters.get(id);
            for (int[] clusterCenter : clusterPositions) {
                SpawnCluster cluster = SpawnController.getSpawnCluster(id);
                nestedSpawns.addAll(cluster.makeSpawns(clusterCenter[0], clusterCenter[1]));
            }
        }

        return nestedSpawns;

    }


}

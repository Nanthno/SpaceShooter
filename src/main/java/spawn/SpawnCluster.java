package src.main.java.spawn;

import src.main.java.Globals;
import src.main.java.enemy.EnemyShip;

import java.util.*;

class SpawnCluster {

    int xPos;
    int yPos;
    int positionVariance;
    double minSpeed;
    double maxSpeed;
    int height;

    List<Spawn> spawns = new ArrayList<>();


    // used for finishing creation of the cluster by adding other clusters
    Map<String, List<int[]>> nestedClusters = new HashMap<>();


    // changes all the spawns such that the top-left one is at position (0,0)
    public void initialize() {
        int minYPos = Integer.MAX_VALUE;
        int minXPos = Integer.MAX_VALUE;

        int maxYPos = 0;

        for(Spawn spawn : spawns) {
            int x = spawn.getxPos();
            int y = spawn.getyPos();
            if(x < minXPos) {
                minXPos = x;
            }
            if(y < minYPos) {
                minYPos = y;
            }
            if(y > maxYPos) {
                maxYPos = y;
            }
        }

        for(Spawn spawn : spawns) {
            spawn.offset(minXPos, minYPos);
        }

        maxYPos -= minYPos;

        height = maxYPos;
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

        double speed = chooseSpeed();

        for (Spawn spawn : spawns) {
            ships.add(spawn.makeEnemy(xPos, yPos, speed));
        }

        ships.addAll(unpackOtherClusters());

        return ships;
    }

    List<EnemyShip> makeSpawnsByCenter(int xCenter, int yCenter) {

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


        double speed = chooseSpeed();

        for (Spawn spawn : spawns) {
            ships.add(spawn.makeEnemy(xPos + xOffset, yPos + yOffset, speed));
        }

        ships.addAll(unpackOtherClusters());

        return ships;

    }

    List<EnemyShip> makeSpawns(int minY, int maxY) {

        List<EnemyShip> ships = new ArrayList<>();

        int xOrigin = Globals.screenWidth+42; // addition prevents ships appearing partially on screen
        int yOrigin = chooseY(minY, maxY);

        double speed = chooseSpeed();

        for (Spawn spawn : spawns) {
            ships.add(spawn.makeEnemy(xPos+xOrigin, yPos+yOrigin, speed));
        }

        ships.addAll(unpackOtherClusters());

        return ships;

    }

    private double chooseSpeed() {
        Random rand = new Random();
        double speed = minSpeed + (rand.nextDouble() * (maxSpeed - minSpeed));
        return speed;
    }

    private List<EnemyShip> unpackOtherClusters() {
        Set<String> clusterIDList = nestedClusters.keySet();
        List<EnemyShip> nestedSpawns = new ArrayList<>();

        for (String id : clusterIDList) {
            List<int[]> clusterPositions = nestedClusters.get(id);
            for (int[] clusterCenter : clusterPositions) {
                SpawnCluster cluster = SpawnController.getSpawnCluster(id);
                nestedSpawns.addAll(cluster.makeSpawnsByCenter(clusterCenter[0], clusterCenter[1]));
            }
        }

        return nestedSpawns;

    }

    private int chooseY(int minY, int maxY) {

        Random rand = new Random();
        int y = rand.nextInt((maxY-height) - minY) + minY;
        return y;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}

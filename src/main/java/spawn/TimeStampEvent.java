package src.main.java.spawn;

import java.util.HashMap;
import java.util.Map;

public class TimeStampEvent {

    private Map<String, Double> spawnProbabilityMap = new HashMap<>();

    private int triggerTime;

    private double speedMultiplier = 1;

    // need to add functionality for specific spawns

    TimeStampEvent(int time) {
        triggerTime = time;
    }

    void addSet(String line) {
        String[] splitLine = line.split(" ");

        String clusterCode = splitLine[1];
        double spawnProbability = Double.parseDouble(splitLine[2]);
        spawnProbabilityMap.put(clusterCode, spawnProbability);
    }

    void addSpawn(String line) {
        System.out.println("WARNING: timeline does not yet support spawns");
    }

    Map<String, Double> getSpawnProbabilityMap() {
        return spawnProbabilityMap;
    }

    int getTriggerTime() {
        return triggerTime;
    }

    protected void setSpeed(double speed) {
        speedMultiplier = speed;
    }

    double getSpeedMultiplier() {
        return speedMultiplier;
    }
}

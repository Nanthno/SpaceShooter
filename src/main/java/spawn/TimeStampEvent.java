package src.main.java.spawn;

import java.util.HashMap;
import java.util.Map;

public class TimeStampEvent {

    Map<String, Double> spawnProbabilityMap = new HashMap<>();

    int triggerTime;

    // need to add functionality for specific spawns

    public TimeStampEvent(int time) {
        triggerTime = time;
    }

    public void addSet(String line) {
        String[] splitLine = line.split(" ");

        String clusterCode = splitLine[1];
        double spawnProbability = Double.parseDouble(splitLine[2]);
        spawnProbabilityMap.put(clusterCode, spawnProbability);
    }

    public void addSpawn(String line) {
        System.out.println("WARNING: timeline does not yet support spawns");
    }

    public Map<String, Double> getSpawnProbabilityMap() {
        return spawnProbabilityMap;
    }

    public int getTriggerTime() {
        return triggerTime;
    }
}

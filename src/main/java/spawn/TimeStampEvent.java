package src.main.java.spawn;

import java.util.HashMap;
import java.util.Map;

public class TimeStampEvent {

    Map<String, Double> spawnProbabilityMap = new HashMap<>();
    // need to add functionality for specific spawns

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
}

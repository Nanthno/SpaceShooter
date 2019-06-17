package src.main.java.spawn;

import src.main.java.enemy.EnemyType;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class spawnUtil {

    static File clusterDirectory = new File("src/main/resources/spawn/cluster");

    static Map<String, String> enemyHash = Map.of(
            "b", "BASIC",
            "f", "FUEL"
    );

    public static EnemyType getEnemyType(String e) {
        return EnemyType.valueOf(enemyHash.get(e));
    }

    static HashMap<String, SpawnCluster> createSpawnClusters() {
        HashMap<String, SpawnCluster> clusters = new HashMap<String, SpawnCluster>();

        File[] clusterFiles = clusterDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.toLowerCase().endsWith(".cluster");
            }
        });

        for (File clusterFile : clusterFiles) {
            try {
                SpawnCluster cluster = new SpawnCluster();
                List<String> rawClusterData = Files.readAllLines(clusterFile.toPath());
                if (clusterFileFormattedCorrectly(rawClusterData)) {
                    int marker = rawClusterData.get(0).indexOf(":");
                    String id = rawClusterData.get(0).substring(marker + 1);
                    marker = rawClusterData.get(1).indexOf(":");
                    int spacing = Integer.parseInt(rawClusterData.get(1)
                            .substring(marker + 1));
                    marker = rawClusterData.get(2).indexOf(":");
                    double speed = Double.parseDouble(rawClusterData.get(2)
                            .substring(marker));
                    marker = rawClusterData.get(3).indexOf(":");
                    String size = rawClusterData.get(3).substring(marker + 1);
                    marker = size.indexOf(",");
                    int width = Integer.parseInt(size.substring(0, marker));
                    int height = Integer.parseInt(size.substring(marker + 1));

                    if (rawClusterData.size() != height + 3) {
                        System.out.println("Problem with file " + clusterFile.toString()
                                + ": number of lines is incorrect");
                        continue;
                    }

                    for (int y = 4; y < rawClusterData.size(); y++) {
                        String line = rawClusterData.get(y);
                        for (int x = 0; x < line.length(); x++) {
                            String clusterPoint = line.substring(x * 2, x * 2 + 1).strip();
                            EnemyType type = null;
                            if (clusterPoint.equals(".")) {
                                continue;
                            }
                            if (clusterPoint.length() == 1) {
                                type = getEnemyType(clusterPoint);

                                Spawn spawn = new Spawn(x * spacing,
                                        y * spacing,
                                        speed,
                                        type);

                                cluster.addSpawn(spawn);

                            }
                            if (clusterPoint.length() == 2) {
                                cluster.addOtherCluster(y + "," + x, clusterPoint);
                            }
                        }
                    }

                    clusters.put(id, cluster);

                } else {
                    System.out.println("Problem reading file: " +
                            clusterFile.toString()
                            + ". Improperly formatted");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return clusters;


        }


        return null;
    }

    private static boolean clusterFileFormattedCorrectly(List<String> file) {

        return file.size() >= 4
                && file.get(0).matches("id:[a-zA-Z][a-zA-Z]")
                && file.get(1).matches("spacing:[0-9]+")
                && file.get(2).matches("speed:[0-9]+[\\?[0-9]*]?")
                && file.get(2).matches("size:[0-9]+,[0-9]+");

    }

    static SpawnPattern[] createSpawnPatterns(HashMap<String, SpawnCluster> clusters) {


        return null;
    }

}

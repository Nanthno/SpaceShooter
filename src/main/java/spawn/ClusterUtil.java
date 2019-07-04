package src.main.java.spawn;

import src.main.java.enemy.EnemyType;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterUtil {

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
                int marker = rawClusterData.get(0).indexOf(":");
                String id = rawClusterData.get(0).substring(marker + 1);
                marker = rawClusterData.get(1).indexOf(":");
                int spacing = Integer.parseInt(rawClusterData.get(1)
                        .substring(marker + 1));
                marker = rawClusterData.get(2).indexOf(":");
                int marker2 = rawClusterData.get(2).indexOf(",");
                double minSpeed = Double.parseDouble(rawClusterData.get(2)
                        .substring(marker + 1, marker2));
                double maxSpeed = Double.parseDouble(rawClusterData.get(2).substring(marker2 + 1));
                marker = rawClusterData.get(3).indexOf(":");
                String size = rawClusterData.get(3).substring(marker + 1);

                cluster.setMinSpeed(minSpeed);
                cluster.setMaxSpeed(maxSpeed);


                // processes the grid
                for (int y = 4; y < rawClusterData.size(); y++) {
                    String line = rawClusterData.get(y);
                    for (int x = 0; x < line.length(); x += 2) {
                        String clusterPoint = line.substring(x, x + 1).strip();
                        EnemyType type = null;
                        if (clusterPoint.equals(".")) {
                            continue;
                        }
                        if (clusterPoint.length() == 1) {
                            type = getEnemyType(clusterPoint);

                            Spawn spawn = new Spawn(x * spacing,
                                    y * spacing,
                                    minSpeed,
                                    maxSpeed,
                                    type);

                            cluster.addSpawn(spawn);

                        }
                        if (clusterPoint.length() == 2) {
                            System.out.println("Issue with " + clusterFile.toString()
                                    + "The ability to nest clusters has not yet been fully implemented");
                            cluster.addOtherCluster(y + "," + x, clusterPoint);
                        }
                    }
                }
                cluster.initialize();
                clusters.put(id, cluster);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return clusters;
    }

    private static boolean clusterFileFormattedCorrectly(List<String> file) {

        boolean correct = file.size() >= 4;
        correct &= file.get(0).matches("id:[a-zA-Z][a-zA-Z]");
        correct &= file.get(1).matches("spacing:[0-9]+");
        correct &= file.get(2).matches("speed:[0-9]+[\\.[0-9]+]?,[0-9]+[\\.[0-9]+]?");
        correct &= file.get(3).matches("size:[0-9]+,[0-9]+");


        return correct;

    }

    static SpawnPattern[] createSpawnPatterns(HashMap<String, SpawnCluster> clusters) {


        return null;
    }

}

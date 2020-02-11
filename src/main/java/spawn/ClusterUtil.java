package src.main.java.spawn;

import src.main.java.Globals;
import src.main.java.ResourceFileType;
import src.main.java.enemy.EnemyType;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

class ClusterUtil {

    private static final File clusterDirectory = new File(Globals.getResourceFile(ResourceFileType.CLUSTER));

    static HashMap<String, SpawnCluster> createSpawnClusters() {
        HashMap<String, SpawnCluster> clusters = new HashMap<>();

        File[] clusterFiles = clusterDirectory.listFiles((file, name) -> name.toLowerCase().endsWith(".cluster"));

        System.out.println("Loading cluster data from " + clusterDirectory);

        for (File clusterFile : Objects.requireNonNull(clusterFiles)) {
            try {
                SpawnCluster cluster = new SpawnCluster();
                List<String> rawClusterData = Files.readAllLines(clusterFile.toPath());
                String id = clusterFile.toString();
                int marker = id.lastIndexOf('/');
                int marker2 = id.indexOf(".cluster");
                id = id.substring(marker + 1, marker2);

                marker = rawClusterData.get(0).indexOf(":");

                int spacing = Integer.parseInt(rawClusterData.get(0)
                        .substring(marker + 1));

                marker = rawClusterData.get(1).indexOf(":");
                marker2 = rawClusterData.get(1).indexOf(",");
                double minSpeed = Double.parseDouble(rawClusterData.get(1)
                        .substring(marker + 1, marker2));
                double maxSpeed = Double.parseDouble(rawClusterData.get(1).substring(marker2 + 1));

                cluster.setMinSpeed(minSpeed);
                cluster.setMaxSpeed(maxSpeed);


                // processes the grid
                for (int y = 2; y < rawClusterData.size(); y++) {
                    String line = rawClusterData.get(y);
                    if (line.equals("END")) {
                        y = rawClusterData.size();
                        continue;
                    }

                    for (int x = 0; x < line.length(); x += 2) {
                        String clusterPoint = line.substring(x, x + 1).trim();
                        if (clusterPoint.equals(".")) {
                            continue;
                        }
                        if (clusterPoint.length() == 1) {
                            EnemyType type = EnemyType.getType(clusterPoint);

                            Spawn spawn = new Spawn(x * spacing,
                                    y * spacing,
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

    /*private static boolean clusterFileFormattedCorrectly(List<String> file) {

        boolean correct = file.size() >= 4;
        correct &= file.get(0).matches("id:[a-zA-Z][a-zA-Z]");
        correct &= file.get(1).matches("spacing:[0-9]+");
        correct &= file.get(2).matches("speed:[0-9]+[\\.[0-9]+]?,[0-9]+[\\.[0-9]+]?");
        correct &= file.get(3).matches("size:[0-9]+,[0-9]+");


        return correct;

    }*/
}

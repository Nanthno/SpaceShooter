package src.main.java.density;

import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DensityMap {

    final static int holeRadius = 32;

    HashMap<int[], Double> density;
    HashMap<int[], Double> holes;

    public DensityMap(List<EnemyShip> enemies) {
        density = new HashMap<>();
        holes = new HashMap<>();

        populateDensity(enemies);
        populateHoles();
    }

    private void populateDensity(List<EnemyShip> enemies) {
        List<int[]> densityKeys = new ArrayList<>();
        for (EnemyShip ship : enemies) {
            if(ship.getType() == EnemyType.AGILE)
                continue;
            int x = ship.getx();
            int y = ship.gety();
            int radius = ship.getRadius();
            int[] key = {x, y, radius};
            density.put(key, 0.0);
            densityKeys.add(key);
        }

        for (int a = 0; a < densityKeys.size(); a++) {
            int[] shipA = densityKeys.get(a);
            for (int b = a + 1; b < densityKeys.size(); b++) {
                int[] shipB = densityKeys.get(b);
                double dist = distance(shipA, shipB);

                int radiusSum = shipA[2] + shipB[2];
                if (dist < radiusSum) {
                    double weightValue = Math.pow((radiusSum - dist), 0.75);

                    density.put(shipA, density.get(shipA) + weightValue);
                    density.put(shipB, density.get(shipB) + weightValue);
                }
            }
        }
    }

    static double distance(int[] pointA, int[] pointB) {
        int x1 = pointA[0];
        int y1 = pointA[1];
        if (pointA.length > 2) {
            int r1 = pointA[2];
            x1 += r1;
            y1 += r1;
        }
        int x2 = pointB[0];
        int y2 = pointB[1];
        if (pointB.length > 2) {
            int r2 = pointB[2];
            y2 += r2;
            x2 += r2;
        }

        double dx = Math.abs(x1 - x2);
        double dy = Math.abs(y1 - y2);

        return Math.sqrt(dx * dx + dy * dy);
    }

    private int[] midPoint(int[] pointA, int[] pointB) {
        int x = ((pointA[0]-pointA[2]) + (pointB[0]-pointB[2]))/2;
        int y = ((pointA[1]-pointA[2]) + (pointB[1]-pointB[2]))/2;

        return new int[]{x, y};
    }

    private boolean overlappingShip(int[] pos, int[][] keys) {
        for (int[] key : keys) {
            if (distance(key, pos)*2 < key[2]) {
                return true;
            }
        }
        return false;
    }

    private void populateHoles() {
        // creates all holes
        Set<int[]> keySet = density.keySet();
        int[][] keys = keySet.stream().toArray(int[][]::new);

        for (int i = 0; i < keys.length; i++) {
            int[] key1 = keys[i];
            for (int k = i + 1; k < keys.length; k++) {
                int[] key2 = keys[k];

                double dist = distance(key1, key2);

                int radii = key1[2] + key2[2];

                if (dist < radii) {
                    double diff = radii - dist;

                    int[] middle = midPoint(key1, key2);
                    if (!overlappingShip(middle, keys)) {

                        double weight = diff * (density.get(key1) + density.get(key2));

                        holes.put(middle, weight);
                    }
                }
            }
        }

        // finds holes which are near each others and mutually strengthens them
        List<int[]> holeKeys = new ArrayList<>();
        holeKeys.addAll(holes.keySet());
        for (int i = 0; i < holeKeys.size(); i++) {
            int[] key1 = holeKeys.get(i);
            for (int k = i + 1; k < holeKeys.size(); k++) {
                int[] key2 = holeKeys.get(k);
                double dist = distance(key1, key2);
                if (dist <= holeRadius && density.containsKey(key1) && density.containsKey(key2)) { // TODO:the containsKey things shouldn't need to be here but otherwise a null pointer exception is thrown
                    double density1 = density.get(key1);
                    double density2 = density.get(key2);

                    double newDensity1 = density1 + density1 * dist / holeRadius;
                    double newDensity2 = density2 + density2 * dist / holeRadius;

                    holes.put(key1, newDensity1);
                    holes.put(key2, newDensity2);
                }
            }
        }
    }

    public HashMap<int[], Double> getDensity() {
        return density;
    }

    public HashMap<int[], Double> getHoles() {
        return holes;
    }

}

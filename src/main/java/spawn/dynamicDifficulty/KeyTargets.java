package src.main.java.spawn.dynamicDifficulty;

import src.main.java.enemy.EnemyBasic;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import java.util.*;

public class KeyTargets {

    private static final int minSilhouetteOverlap = 5;


    // for testing
    public static void main(String[] args) {
        List<EnemyShip> ships = new ArrayList<>();

        ships.add(new EnemyBasic(0, 10, 0));
        ships.add(new EnemyBasic(0, 20, 0));
        ships.add(new EnemyBasic(10, 20, 0));
        ships.add(new EnemyBasic(10, 14, 0));

        List<AnnihilationPath> solution = findAnnihilationPaths(ships);

        return;
    }

    protected static List<AnnihilationPath> findAnnihilationPaths(List<EnemyShip> ships) {
        Queue<AnnihilationPath> que = new LinkedList<>();

        que.add(new AnnihilationPath(ships));

        while(que.size() > 0) {
            //TODO: make a breadth first search on the annihilationpaths to all paths in the first layer of possible annihilation
        }

        return null;
    }

    private static List<EnemyShip> filterInvalidTargets(List<EnemyShip> ships) {

        List<EnemyShip> validTargets = new ArrayList<>();

        for (int i = 0; i < ships.size(); i++) {
            EnemyShip ship1 = ships.get(i);
            Silhouette sil1 = new Silhouette(ship1);

            boolean silValid = true;
            int topOverlap = 0;
            int botOverlap = Integer.MAX_VALUE;
            for (int k = 0; k < ships.size(); k++) {
                if (i == k)
                    continue;

                EnemyShip ship2 = ships.get(k);
                Silhouette sil2 = new Silhouette(ship2);
                if (sil2.x < sil1.x) {

                    boolean isTopOverlap = sil2.topY <= sil1.topY && sil2.botY >= sil1.topY;
                    boolean isBotOverlap = sil2.topY <= sil1.botY && sil2.botY >= sil1.botY;

                    if (isBotOverlap && isTopOverlap) {
                        silValid = false;
                        continue;
                    } else if (isTopOverlap) {
                        int overlap = sil1.topY - sil2.botY;
                        topOverlap = overlap > topOverlap ? overlap : topOverlap;
                    } else if (isBotOverlap) {
                        int overlap = sil2.topY - sil1.botY;
                        botOverlap = overlap < botOverlap ? overlap : botOverlap;
                    }
                }
            }

            if (silValid && botOverlap - topOverlap > minSilhouetteOverlap)
                validTargets.add(ship1);

        }

        return validTargets;

    }

    private static Map<EnemyType, Integer> copyMap(Map<EnemyType, Integer> map) {
        Map<EnemyType, Integer> newMap = new HashMap<>();

        for (EnemyType type : newMap.keySet()) {
            newMap.put(type, map.get(type));
        }

        return newMap;
    }


}

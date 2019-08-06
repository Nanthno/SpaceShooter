package src.main.java.spawn.dynamicDifficulty;

import src.main.java.Explosion;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnihilationPath {

    private static final int minSilhouetteOverlap = 5;

    List<EnemyShip> ships;

    List<Explosion> explosions;

    Map<EnemyType, Integer> path;

    public AnnihilationPath(List<EnemyShip> ships) {
        this.ships = ships;
    }

    public AnnihilationPath(List<EnemyShip> ships, Map<EnemyType, Integer> path, List<Explosion> explosions) {
        this.ships = ships;
        this.path = path;
        this.explosions = explosions;
    }



    public List<AnnihilationPath> findNextPaths() {

        List<AnnihilationPath> nextPaths = new ArrayList<>();

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

            if (silValid && botOverlap - topOverlap > minSilhouetteOverlap) {
                Explosion exp = new Explosion(ship1, 0);
                List<EnemyShip> newShips = copyList(ships);
                newShips.remove(ship1);
                List<Explosion> newExplosions = new ArrayList<>();
                newExplosions.add(exp);
                nextPaths.add(new AnnihilationPath(newShips, copyMap(path), newExplosions));
            }

        }

        return nextPaths;

    }

    private static Map<EnemyType, Integer> copyMap(Map<EnemyType, Integer> map) {
        Map<EnemyType, Integer> newMap = new HashMap<>();

        for (EnemyType type : newMap.keySet()) {
            newMap.put(type, map.get(type));
        }

        return newMap;
    }
    private static List<EnemyShip> copyList(List<EnemyShip> ships) {
        List<EnemyShip> newShips = new ArrayList<>();

        for(EnemyShip e : ships) {
            newShips.add(e);
        }

        return newShips;
    }


}
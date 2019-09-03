package src.main.java.spawn.dynamicDifficulty;

import src.main.java.enemy.EnemyShip;

class Silhouette {
    final int topY;
    final int botY;
    final int x;

    protected Silhouette(EnemyShip ship) {

        topY = ship.getY();
        botY = topY + ship.getRadius() * 2;

        x = ship.getX();
    }

    public int[] makeHashable() {
        return new int[]{topY, botY, x};
    }
}

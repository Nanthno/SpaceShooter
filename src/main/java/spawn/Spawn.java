package src.main.java.spawn;
import src.main.java.enemy.EnemyBasic;
import src.main.java.enemy.EnemyFuel;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

class Spawn {

    // these positions are relative to the spawn cluster position
    int xPos;
    int yPos;

    double speed;

    EnemyType type;

    public Spawn(int xPos, int yPos, double speed, EnemyType type) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.speed = speed;
        this.type = type;

    }

    EnemyShip makeEnemy(int clusterX, int clusterY) {
        EnemyShip ship = null;

        if (type == EnemyType.BASIC)
            ship = new EnemyBasic(clusterX + xPos, clusterY + yPos, speed);
        if (type == EnemyType.FUEL)
            ship = new EnemyFuel(clusterX + xPos, clusterY + yPos, speed);

        return ship;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}

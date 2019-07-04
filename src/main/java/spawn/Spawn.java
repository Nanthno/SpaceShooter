package src.main.java.spawn;
import src.main.java.Globals;
import src.main.java.enemy.EnemyBasic;
import src.main.java.enemy.EnemyFuel;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

class Spawn {

    // these positions are relative to the spawn cluster position
    int xPos;
    int yPos;

    double minSpeed;
    double maxSpeed;

    EnemyType type;

    public Spawn(int xCenter, int yCenter, double minSpeed, double maxSpeed, EnemyType type) {

        int xPos = xCenter - Globals.getEnemyShipRadius(type);
        int yPos = yCenter - Globals.getEnemyShipRadius(type);

        this.xPos = xPos;
        this.yPos = yPos;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.type = type;

    }

    EnemyShip makeEnemy(int clusterX, int clusterY, double speed) {
        EnemyShip ship = null;

        if (type == EnemyType.BASIC)
            ship = new EnemyBasic(clusterX + xPos, clusterY + yPos, speed);
        if (type == EnemyType.FUEL)
            ship = new EnemyFuel(clusterX + xPos, clusterY + yPos, speed);

        return ship;
    }

    public void offset(int x, int y) {
        xPos -= x;
        yPos -= y;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}

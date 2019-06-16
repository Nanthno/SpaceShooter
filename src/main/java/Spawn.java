package src.main.java;

class Spawn {

    // these positions are relative to the spawn cluster position
    int xPos;
    int yPos;

    int speed;

    EnemyType type;

    EnemyShip makeEnemy(int clusterX, int clusterY) {
        EnemyShip ship = null;

        if (type == EnemyType.BASIC)
            ship = new EnemyBasic(clusterX + xPos, clusterY + yPos, speed);
        if (type == EnemyType.FUEL)
            ship = new EnemyFuel(clusterX + xPos, clusterY + yPos, speed);

        return ship;
    }


}

package src.main.java.spawn;

import src.main.java.Globals;
import src.main.java.enemy.*;

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

        if (type == EnemyType.AGILE)
            ship = new EnemyAgile(clusterX + xPos, clusterY + yPos, speed);
        else if (type == EnemyType.SHIELDER)
            ship = new EnemyShielder(clusterX + xPos, clusterY + yPos, speed);
        else if (type == EnemyType.ARMORED1)
            ship = new EnemyArmored1(clusterX + xPos, clusterY + yPos, speed);
        else if (type == EnemyType.SHOOTER)
            ship = new EnemyShooter(clusterX + xPos, clusterY + yPos, speed);
        else if (type == EnemyType.PILOTED)
            ship = new EnemyPiloted(clusterX + xPos, clusterY + yPos, speed);
        else
            ship = new EnemyShip(clusterX + xPos, clusterY + yPos, speed, type);
/*
        Class<EnemyShip> shipClass = Globals.getEnemyClass(type);
        try {
            Constructor<EnemyShip> constructor = shipClass.getConstructor(Integer.class, Integer.class, Double.class, EnemyType.class);
            ship = constructor.newInstance(clusterX + xPos, clusterY + yPos, speed, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        /*if (type == EnemyType.BASIC)
            ship = new EnemyBasic(clusterX + xPos, clusterY + yPos, speed);
        if (type == EnemyType.FUEL)
            ship = new EnemyFuel(clusterX + xPos, clusterY + yPos, speed);
        if (type == EnemyType.AGILE)
            ship = new EnemyAgile(clusterX + xPos, clusterY + yPos, speed);
        if(type == EnemyType.SHIELDER) {
            ship = new EnemyShielder(clusterX + xPos, clusterY + yPos, speed);
        }*/

        return ship;
    }

    public void offset(int x, int y) {
        xPos -= x;
        yPos -= y;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}

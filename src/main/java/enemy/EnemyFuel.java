package src.main.java.enemy;

import src.main.java.Globals;

public class EnemyFuel extends EnemyShip {

    public EnemyFuel(int x, int y, double xSpeed) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = EnemyType.FUEL;
        radius = Globals.enemyFuelRadius;
    }

}


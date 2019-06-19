package src.main.java.enemy;

import src.main.java.Globals;

public class EnemyFuel extends EnemyShip {


    public static final double maxSpeed = 1.5;
    public static final double minSpeed = 1;

    public EnemyFuel(int x, int y, double xSpeed) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = 1;
        radius = Globals.enemyFuelRadius;
    }


    public EnemyFuel(int y, double xSpeed) {

        yPos = y;
        this.xSpeed = xSpeed;
        shipType = 1;
        radius = Globals.enemyFuelRadius;
    }

}


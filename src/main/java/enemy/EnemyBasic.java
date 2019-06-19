package src.main.java.enemy;

import src.main.java.Globals;

public class EnemyBasic extends EnemyShip {


    public static final double maxSpeed = 2.5;
    public static final double minSpeed = 1;

    public EnemyBasic(int y, double xSpeed) {
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = 0;
        radius = Globals.enemyBasicRadius;
    }

    public EnemyBasic(int x, int y, double xSpeed) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = 0;
        radius = Globals.enemyBasicRadius;
    }

}


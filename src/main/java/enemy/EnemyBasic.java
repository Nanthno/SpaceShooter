package src.main.java.enemy;

import src.main.java.Globals;

public class EnemyBasic extends EnemyShip {

    public EnemyBasic(int x, int y, double xSpeed) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = EnemyType.BASIC;
        radius = Globals.enemyBasicRadius;
    }

}


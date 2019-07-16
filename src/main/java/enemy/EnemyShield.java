package src.main.java.enemy;

import src.main.java.Globals;
import src.main.java.PlayerBullet;

public class EnemyShield extends EnemyShip {

    double innerRadius = 5;

    protected EnemyShield(int x, int y, double xSpeed) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = EnemyType.SHIELD;
        radius = Globals.enemyShieldRadius;
        innerRadius = radius - innerRadius;
    }

    @Override
    public boolean isKillable(Class weapon) {
        return false;
    }

    @Override
    protected boolean checkDead() {
        return true;
    }

    @Override
    public boolean collideWithBullet(PlayerBullet b) {
        double dist = distance(b.getx(), b.gety(), b.getRadius(), xPos,yPos, radius);
        double radiiSum = b.getRadius() + radius;

        boolean insideRadius = dist < radiiSum;
        boolean outsideInnerRadius = dist > innerRadius;

        System.out.printf("%f : %f - %f%n", dist, (double)radius, innerRadius);

        return insideRadius && outsideInnerRadius;
    }

}

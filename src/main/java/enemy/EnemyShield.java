package src.main.java.enemy;

import src.main.java.PlayerBullet;

import java.util.Random;

public class EnemyShield extends EnemyShip {

    double innerRadius = 5;

    public EnemyShield(int x, int y, double xSpeed) {
        super(x, y, xSpeed, EnemyType.SHIELD);
        innerRadius = radius - innerRadius;
        currentFrame = (new Random()).nextInt(maxFrame);
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
        return insideRadius && outsideInnerRadius;
    }

}

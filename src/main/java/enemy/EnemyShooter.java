package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;

import java.util.Random;

public class EnemyShooter extends EnemyShip {

    boolean shooting = false;

    private static final int maxShootingProbability = 300;
    double shootingProbability = 1;

    public EnemyShooter(int x, int y, double xSpeed) {
        super(x, y, xSpeed, EnemyType.SHOOTER);
        maxFrame = Globals.getEnemyShipsMaxFrames(EnemyType.SHOOTER);
        currentFrame = 0;
    }

    @Override
    public boolean update() {
        boolean checkDead = super.update();

        setShooting();


        if (!shooting)
            currentFrame = 0;
        return checkDead;
    }

    private void setShooting() {
        if (shooting && currentFrame == maxFrame - 1) {
            shooting = false;
            return;
        }


        Random rand = new Random();
        int val = rand.nextInt(maxShootingProbability);
        if (val < shootingProbability) {
            shooting = true;
            fireBullet();
        }

    }

    private void fireBullet() {
        Controller.spawnShooterEmp((int) xPos + radius - 2, (int) yPos + radius + 2, xSpeed);
    }

}

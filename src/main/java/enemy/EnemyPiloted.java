package src.main.java.enemy;

import src.main.java.Controller;

public class EnemyPiloted extends EnemyShip {

    double speed;

    int xDist = 100;
    int xTolerance = 0;
    double yLeadPerX = 0.1;

    int firingTolerance = 30;

    int maxBurstLength = 3;
    int currentBurst = 0;
    int maxBurstCooldown = 5;
    int burstCooldown = 0;
    int maxCooldown = 40;
    int cooldown = 0;

    public EnemyPiloted(int x, int y, double speed) {
        super(x, y, speed, EnemyType.PILOTED);
    }

    @Override
    void init() {
        speed = xSpeed;
    }

    @Override
    public boolean update() {
        cooldown--;

        double playerX = Controller.getPlayerXPos();
        double playerY = Controller.getPlayerYPos();
        double playerYSpeed = Controller.getPlayerYSpeed();

        double targetX = playerX + xDist;
        double targetY = playerY + playerYSpeed * yLeadPerX*(xPos - playerX);

        if(Math.abs(targetX-xPos) < xTolerance) {
            targetX = xPos;
        }

        setVector(targetX, targetY, speed);


        xPos -= xSpeed;
        yPos -= ySpeed;

        if(cooldown < 0 && Math.abs(playerY - yPos) < firingTolerance) {
            cooldown = maxCooldown;
            currentBurst = maxBurstLength;
            burstCooldown = 0;
        }

        if(currentBurst > 0) {
            burstCooldown--;
            if(burstCooldown < 0) {
                fire();
                burstCooldown = maxBurstCooldown;
                currentBurst--;
            }

        }

        return false;
    }

    private void fire() {
        Controller.spawnShooterEmp((int)xPos, (int)yPos + 2, xSpeed);
        Controller.spawnShooterEmp((int)xPos, (int)yPos + radius*2 - 2, xSpeed);
    }

}

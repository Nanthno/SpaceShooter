package src.main.java.enemy;

import src.main.java.Controller;

public class EnemyPiloted extends EnemyShip {

    private double speed;

    private int currentBurst = 0;
    private int burstCoolDown = 0;
    private int coolDown = 0;

    private double targetX;
    private double targetY;

    public EnemyPiloted(int x, int y, double speed) {
        super(x, y, speed, EnemyType.PILOTED);
    }

    @Override
    void init() {
        speed = xSpeed;
    }

    private void setTargetPos() {
        int xDist = 100;
        targetX = Controller.getPlayerXPos() + xDist;
        targetY = Controller.getPlayerYPos();
    }

    @Override
    public boolean update() {
        coolDown--;
        setTargetPos();

        //double playerX = Controller.getPlayerXPos();
        double playerY = Controller.getPlayerYPos();
        double playerYSpeed = Controller.getPlayerYSpeed();
        double yLeadPerX = 0.1;
        double trueTargetY = targetY + playerYSpeed * yLeadPerX * (xPos - targetX);
        //double targetX = playerX + xDist;
        //double targetY = playerY + playerYSpeed * yLeadPerX * (xPos - playerX);

        int xTolerance = 0;
        if (Math.abs(targetX - xPos) < xTolerance) {
            targetX = xPos;
        }

        //setVector(targetX, targetY, speed);
        setVector(targetX, trueTargetY, speed);


        xPos -= xSpeed;
        yPos -= ySpeed;

        int firingTolerance = 30;
        if (coolDown < 0 && Math.abs(playerY - yPos) < firingTolerance) {
            coolDown = 40;
            currentBurst = 3;
            burstCoolDown = 0;
        }

        if (currentBurst > 0) {
            burstCoolDown--;
            if (burstCoolDown < 0) {
                fire();
                burstCoolDown = 5;
                currentBurst--;
            }

        }

        return false;
    }

    private void fire() {
        Controller.spawnShooterEmp((int) xPos, (int) yPos + 2, xSpeed);
        Controller.spawnShooterEmp((int) xPos, (int) yPos + radius * 2 - 2, xSpeed);
    }

}

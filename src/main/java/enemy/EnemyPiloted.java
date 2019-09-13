package src.main.java.enemy;

import src.main.java.Controller;

import java.util.Random;

public class EnemyPiloted extends EnemyShip {

    double speed;

    int xDist = 100;
    int xTolerance = 0;
    double yLeadPerX = 0.1;

    int firingTolerance = 30;

    int maxBurstLength = 3;
    int currentBurst = 0;
    int maxBurstCoolDown = 5;
    int burstCoolDown = 0;
    int maxCoolDown = 40;
    int coolDown = 0;

    Assignment assignment;
    int id;

    double targetX;
    double targetY;
    double targetYSpeed;

    public EnemyPiloted(int x, int y, double speed) {
        super(x, y, speed, EnemyType.PILOTED);
    }

    @Override
    void init() {
        speed = xSpeed;
        id = EnemyMaster.requestID();
    }

    private void updateAssignment() {
        Random rand = new Random();
        if(rand.nextDouble() < 0.1) {
            assignment = EnemyMaster.requestAssignment(id, this.shipType);
        }
    }

    private void setTargetPos() {
        //if(assignment == Assignment.ATTACK) {
            targetX = Controller.getPlayerXPos() + xDist;
            targetY = Controller.getPlayerYPos();
            targetYSpeed = Controller.getPlayerYSpeed();
        //}
    }

    @Override
    public boolean update() {
        coolDown--;
        updateAssignment();
        setTargetPos();

        //double playerX = Controller.getPlayerXPos();
        double playerY = Controller.getPlayerYPos();
        double playerYSpeed = Controller.getPlayerYSpeed();
        double trueTargetY = targetY + playerYSpeed * yLeadPerX * (xPos - targetX);
        //double targetX = playerX + xDist;
        //double targetY = playerY + playerYSpeed * yLeadPerX * (xPos - playerX);

        if (Math.abs(targetX - xPos) < xTolerance) {
            targetX = xPos;
        }

        //setVector(targetX, targetY, speed);
        setVector(targetX, trueTargetY, speed);


        xPos -= xSpeed;
        yPos -= ySpeed;

        if (coolDown < 0 && Math.abs(playerY - yPos) < firingTolerance) {
            coolDown = maxCoolDown;
            currentBurst = maxBurstLength;
            burstCoolDown = 0;
        }

        if (currentBurst > 0) {
            burstCoolDown--;
            if (burstCoolDown < 0) {
                fire();
                burstCoolDown = maxBurstCoolDown;
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

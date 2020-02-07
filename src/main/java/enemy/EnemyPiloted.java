package src.main.java.enemy;

import src.main.java.Controller;

import java.util.Random;

public class EnemyPiloted extends EnemyShip {

    private double speed;

    private int currentBurst = 0;
    private int burstCoolDown = 0;
    private int coolDown = 0;

    private int id;

    private double targetX;
    private double targetY;

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
            Assignment assignment = EnemyMaster.requestAssignment(this.shipType);
        }
    }

    private void setTargetPos() {
        //if(assignment == Assignment.ATTACK) {
        int xDist = 100;
        targetX = Controller.getPlayerXPos() + xDist;
            targetY = Controller.getPlayerYPos();
        double targetYSpeed = Controller.getPlayerYSpeed();
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
            int maxCoolDown = 40;
            coolDown = maxCoolDown;
            int maxBurstLength = 3;
            currentBurst = maxBurstLength;
            burstCoolDown = 0;
        }

        if (currentBurst > 0) {
            burstCoolDown--;
            if (burstCoolDown < 0) {
                fire();
                int maxBurstCoolDown = 5;
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

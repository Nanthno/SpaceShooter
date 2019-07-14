package src.main.java;

import src.main.java.graphics.GraphicsManager;

import java.util.Arrays;

public class PlayerShip {

    int health = 100;
    int maxHealth = 100;

    double xPos = 30;
    double yPos = GraphicsManager.getWidth() / 2;
    double xSpeed = 4;
    double ySpeed = 4;


    // manages how fast the player can fire
    int maxFire = 3;
    int fire = 0;
    int fireHeat = 20;
    int overHeat = 19;
    int cooloff = 3;
    int heat = 70;
    boolean overheated = false;

    // manages special actions
    int charge = 0;
    int maxCharge = 100;
    int chargeCount = 0;
    int maxChargeCount = 1;

    final int radius = 12;

    Input in;

    public PlayerShip() {
        in = new Input();
    }

    public void update() {
        fire--;
        if (heat > 0)
            heat--;

        if (heat < cooloff) {
            overheated = false;
        }

        // special action charge
        if (charge < maxCharge) {
            charge++;

        }

        if (charge == maxCharge && chargeCount < maxChargeCount) {
            chargeCount++;
            if (chargeCount < maxChargeCount) {
                charge = 0;
            }
        }


        // movement
        if (in.up && yPos > 0)
            yPos -= ySpeed;
        else if (in.down && yPos < Globals.screenHeight-radius*4) // not really sure why it has to be 4 not 2 but it works
            yPos += ySpeed;
        if (in.right && xPos < GraphicsManager.getWidth()-radius*4)
            xPos += xSpeed;
        else if (in.left && xPos > 0)
            xPos -= xSpeed;
        // firing
        if (in.fire && fire < 0 && !overheated) {
            Controller.addBullet(new PlayerBullet(xPos + 5, yPos + radius - 1, 5, 0));
            fire = maxFire;
            heat += fireHeat;
            if (heat > overHeat) {
                overheated = true;
            }
        }
        if (in.laserBlast && chargeCount > 0) {
            Controller.fireBlast((int) xPos + radius / 2);
            chargeCount--;
            charge = 0;
        }


    }

    void shipCollision() {
        health -= 10;
    }

    void looseHealth(int healthLoss) {
        health -= healthLoss;
    }


    public double getx() {
        return xPos;
    }

    public double gety() {
        return yPos;
    }

    int getHealth() {
        return health;
    }

    int getMaxHealth() {
        return maxHealth;
    }

    int getRadius() {
        return radius;
    }

    int getHeat() {
        return heat;
    }

    int getMaxHeat() {
        return overHeat;
    }

    int getCharge() {
        return charge;
    }

    int getMaxCharge() {
        return maxCharge;
    }

}

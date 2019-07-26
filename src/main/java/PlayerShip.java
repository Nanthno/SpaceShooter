package src.main.java;

import src.main.java.graphics.GraphicsManager;

public class PlayerShip {

    int health = 100;
    int maxHealth = 100;

    int enemyShipCollisionDamage = 3;

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

    int maxFrames;
    int currentFrame = 0;

    int immobile = 0;
    int invinciblePeriod = -10;
    int empDelay = 30;

    public PlayerShip() {
        in = new Input();
        maxFrames = Globals.getPlayerMaxFrames();
    }

    public void update() {
        immobile--;
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

        if (immobile < 0) {
            // movement
            if (in.up && yPos > 0)
                yPos -= ySpeed;
            else if (in.down && yPos < Globals.screenHeight - radius * 4) // not really sure why it has to be 4 not 2 but it works
                yPos += ySpeed;
            if (in.right && xPos < GraphicsManager.getWidth() - radius * 4)
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
            if (in.special1 && chargeCount > 0) {
                Controller.fireBlast((int) xPos + radius / 2);
                chargeCount--;
                charge = 0;
            }
            if (in.special2 && chargeCount > 0) {
                Controller.createMissile((int) xPos + radius / 2, (int) yPos + radius / 2);
                chargeCount--;
                charge = 0;
            }
        }

    }

    void hitByWeapon(EnemyWeaponParent weapon) {
        if (weapon.getType() == WeaponType.SHOOTER_BULLET) {
            if (immobile < invinciblePeriod)
                immobile = empDelay;
        } else { // this should never happen
            System.out.println("WARNING: player hit by weapon " + weapon.getType() + " but not programmed to handle it");
        }
    }

    void shipCollision() {
        health -= enemyShipCollisionDamage;
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

    public int getFrame() {
        return currentFrame;
    }
}

package src.main.java;

import src.main.java.graphics.GraphicsManager;
import src.main.java.weapons.WeaponType;
import src.main.java.weapons.enemyWeapons.EnemyWeaponParent;
import src.main.java.weapons.playerWeapons.PlayerBullet;

public class PlayerShip {

    double xPos = 64;
    double yPos = GraphicsManager.getHeight() / 2;
    double xSpeed = 4;
    double ySpeed = 4;

    double yMove = 0;


    // manages how fast the player can fire
    int maxFire = 3;
    int fire = 0;
    int fireHeat = 15;
    int overHeat = 12;
    int coolOff = 3;
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

    // used to ensure that the player releases the weapon_player_missile fire button before sending a killMissile to Controller
    //// 0 = no missiles, 1 = missiles exist but not primed, 2 = missiles exist and primed
    static int missileArmed = 0;

    public PlayerShip() {
        in = new Input();
        maxFrames = Globals.getPlayerMaxFrames();
    }

    public void update() {
        immobile--;
        fire--;
        if (heat > 0)
            heat--;

        if (heat < coolOff) {
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
            if (Input.up && yPos > 0) {
                yPos -= ySpeed;
                yMove = -1;
            } else if (Input.down && yPos < Globals.screenHeight - radius * 4) { // not really sure why it has to be 4 not 2 but it works
                yPos += ySpeed;
                yMove = 1;
            } else {
                yMove = 0;
            }
            if (Input.right && xPos < GraphicsManager.getWidth() - radius * 4)
                xPos += xSpeed;
            else if (Input.left && xPos > 0)
                xPos -= xSpeed;
            // firing
            if (Input.fire && fire < 0 && !overheated) {
                Controller.firePlayerBullet(new PlayerBullet(xPos + 5, yPos + radius - 1, fire < -40));
                fire = maxFire;
                heat += fireHeat;
                if (heat > overHeat) {
                    overheated = true;
                }
            }
            if (Input.special1 && chargeCount > 0) {
                Controller.fireBlast((int) xPos + radius / 2);
                chargeCount--;
                charge = 0;
            }
            if (Input.special2) {
                if (missileArmed == 2) {
                    Controller.killMissiles();
                    missileArmed = 0;
                }

                if (chargeCount > 0) {
                    Controller.createMissile((int) xPos + radius / 2, (int) yPos + radius / 2);
                    chargeCount--;
                    charge = 0;
                    missileArmed = 1;
                }
            } else if (missileArmed == 1) {
                missileArmed = 2;
            }
            if (Input.special3 && chargeCount > 0) {
                Controller.createBurst((int) xPos + radius, (int) yPos + radius);
                chargeCount--;
                charge = 0;
            }
        }

    }

    void hitByWeapon(EnemyWeaponParent weapon) {
        if (weapon.getType() == WeaponType.ENEMY_EMP) {
            if (immobile < invinciblePeriod)
                immobile = empDelay;
        } else { // this should never happen
            System.out.println("WARNING: player hit by weapon " + weapon.getType() + " but not programmed to handle it");
        }
    }


    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
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

    public int getChargeFrame(int maxFrame) {
        int frame = (int)(maxFrame*1.0*charge/maxCharge);
        return frame;
    }
    public int getFrame(int maxFrame) {
        currentFrame++;
        currentFrame = currentFrame % maxFrame;
        return currentFrame;
    }

    public boolean isZapped() {
        return immobile > 0;
    }
    public int getZapFrame(int maxFrame) {
        return (int)(maxFrame - (1.0*immobile/empDelay)*maxFrame);
    }
}

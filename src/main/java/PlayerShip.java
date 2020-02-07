package src.main.java;

import src.main.java.graphics.GraphicsManager;
import src.main.java.weapons.WeaponType;
import src.main.java.weapons.enemyWeapons.EnemyWeaponParent;
import src.main.java.weapons.playerWeapons.PlayerBullet;

import java.util.HashMap;
import java.util.Map;

public class PlayerShip {

    private double xPos = 64;
    private double yPos = GraphicsManager.getHeight() / 2;
    final double ySpeed = 4;

    double yMove = 0;


    private int fire = 0;
    private final int overHeat = 12;
    private int heat = 70;
    private boolean overheated = false;

    // manages special actions
    private int charge = 0;
    private int delay = 0;
    private final int maxCharge = 200;
    private final Map<WeaponType, Integer> weaponCost = new HashMap<WeaponType, Integer>() {{
        put(WeaponType.PLAYER_LASER_BLAST, 50);
        put(WeaponType.PLAYER_BURST, 100);
        put(WeaponType.PLAYER_MISSILE, 75);

    }};

    private final int radius = 12;

    private int currentFrame = 0;

    private int immobile = 0;
    private final int empDelay = 30;

    // used to ensure that the player releases the weapon_player_missile fire button before sending a killMissile to Controller
    //// 0 = no missiles, 1 = missiles exist but not primed, 2 = missiles exist and primed
    private static int missileArmed = 0;

    private boolean shieldOn = false;
    private int minShieldRemaining = 0;
    private int shieldFrame = 0;

    public PlayerShip() {
        Input in = new Input();
        int maxFrames = Globals.getPlayerMaxFrames();
    }

    public void update() {
        immobile--;
        fire--;
        delay--;
        if (heat > 0)
            heat--;

        int coolOff = 3;
        if (heat < coolOff) {
            overheated = false;
        }

        // special action charge
        if (charge < maxCharge) {
            charge++;

        }

        if (immobile < 0) {
            // movement
            if (Input.up && yPos > 0) {
                yPos -= ySpeed;
                yMove = -1;
            } else if (Input.down && yPos < Globals.gameHeight - radius * 2) {
                yPos += ySpeed;
                yMove = 1;
            } else {
                yMove = 0;
            }
            double xSpeed = 4;
            if (Input.right && xPos < GraphicsManager.getWidth() - radius * 4)
                xPos += xSpeed;
            else if (Input.left && xPos > 0)
                xPos -= xSpeed;
            // firing
            if (Input.fire && fire < 0 && !overheated) {
                Controller.firePlayerBullet(new PlayerBullet(xPos + 5, yPos + radius - 1, fire < -40));
                // manages how fast the player can fire
                int maxFire = 3;
                fire = maxFire;
                int fireHeat = 15;
                heat += fireHeat;
                if (heat > overHeat) {
                    overheated = true;
                }
            }

            if (delay < 0) {
                int minDelay = 15;
                if (Input.special1 && charge > weaponCost.get(WeaponType.PLAYER_LASER_BLAST)) {
                    Controller.fireBlast((int) xPos + radius / 2);
                    charge -= weaponCost.get(WeaponType.PLAYER_LASER_BLAST);
                    delay = minDelay;
                }
                if (Input.special2) {
                    if (missileArmed == 2) {
                        Controller.killMissiles();
                        missileArmed = 0;
                    }

                    if (charge > weaponCost.get(WeaponType.PLAYER_MISSILE)) {
                        Controller.createMissile((int) xPos + radius / 2, (int) yPos + radius / 2);
                        charge -= weaponCost.get(WeaponType.PLAYER_MISSILE);
                        delay = minDelay;
                        missileArmed = 1;
                    }
                } else if (missileArmed == 1) {
                    missileArmed = 2;
                }
                if (Input.special3 && charge > weaponCost.get(WeaponType.PLAYER_BURST)) {
                    Controller.createBurst((int) xPos + radius, (int) yPos + radius);
                    charge -= weaponCost.get(WeaponType.PLAYER_BURST);
                    delay = minDelay;
                }
            }

            if (Input.special4) {
                int minShieldCost = 20;
                if (!shieldOn && charge >= minShieldCost) {
                    shieldOn = true;
                    minShieldRemaining = minShieldCost;
                    shieldFrame = 0;
                }
            } else if (minShieldRemaining <= 0) {
                shieldOn = false;
            }
            if (shieldOn) {
                int shieldCost = 2;
                charge -= shieldCost;
                minShieldRemaining -= shieldCost;
                shieldFrame++;
            }

            if (charge < 0) {
                shieldOn = false;
                charge = 0;
            }

        }
    }


    void hitByWeapon(EnemyWeaponParent weapon) {
        if (shieldOn)
            return;


        if (weapon.getType() == WeaponType.ENEMY_EMP) {
            int invinciblePeriod = -10;
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
        int frame = (int) (maxFrame * 1.0 * charge / maxCharge);
        if(frame > maxFrame)
            frame %= maxFrame;
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
        return (int) (maxFrame - (1.0 * immobile / empDelay) * maxFrame);
    }

    public int getShieldFrame(int maxFrame) {
        return shieldFrame % maxFrame;
    }

    public boolean getShieldOn() {
        return shieldOn;
    }
}

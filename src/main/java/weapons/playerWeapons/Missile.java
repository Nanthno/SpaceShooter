package src.main.java.weapons.playerWeapons;

import src.main.java.Controller;
import src.main.java.ExplosionType;
import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class Missile extends PlayerWeaponParent {

    private static final double defaultSpeed = 10;

    private final long spawnTime;

    private boolean armed = false;
    private int armDelay = 50;

    public Missile(double x, double y) {
        type = WeaponType.PLAYER_MISSILE;

        xPos = x;
        yPos = y;

        ySpeed = 0;
        xSpeed = defaultSpeed;
        radius = 6;
        maxStage = Globals.getWeaponMaxFrames(WeaponType.PLAYER_MISSILE);

        spawnTime = System.currentTimeMillis();
    }

    public void hitEnemy() {
        Controller.spawnExp((int) xPos, (int) yPos, radius, ExplosionType.PROJECTILE, 0);
    }

    @Override
    public boolean update() {
        xPos += xSpeed;
        yPos += ySpeed;

        if (!armed)
            arm();

        return false;
    }

    private void arm() {
        if (System.currentTimeMillis() - spawnTime > armDelay) {
            armed = true;
        }
    }

    public boolean isArmed() {
        return armed;
    }

}

package src.main.java.weapons.playerWeapons;

import src.main.java.Controller;
import src.main.java.ExplosionType;
import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class Missile extends PlayerWeaponParent {

    private static final double defaultSpeed = 5;

    public Missile(double x, double y, double dx, double dy) {
        type = WeaponType.MISSILE;

        xPos = x;
        yPos = y;
        xSpeed = dx;
        ySpeed = dy;

        radius = 2;

        maxStage = Globals.getWeaponMaxFrames(WeaponType.MISSILE);
    }

    public Missile(double x, double y) {
        type = WeaponType.MISSILE;

        xPos = x;
        yPos = y;

        ySpeed = 0;
        xSpeed = defaultSpeed;
        radius = 6;
        maxStage = Globals.getWeaponMaxFrames(WeaponType.MISSILE);
    }

    public void hitEnemy() {
        Controller.spawnExp((int)xPos, (int)yPos, radius, ExplosionType.PROJECTILE, 0);
    }

    @Override
    public boolean update() {
        xPos += xSpeed;
        yPos += ySpeed;
        return false;
    }

}

package src.main.java.weapons.playerWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

import java.util.Random;

public class PlayerBullet extends PlayerWeaponParent {

    private static final double bulletXSpeed = 7;
    private static final double yVariance = 0.2;

    public PlayerBullet(double x, double y, boolean straightShot) {
        type = WeaponType.PLAYER_BULLET;

        xPos = x;
        yPos = y;

        Random rand = new Random();
        if (straightShot)
            ySpeed = 0;
        else {
            ySpeed = (rand.nextDouble() - 0.5) * yVariance;
        }
        xSpeed = bulletXSpeed;

        radius = 2;
        maxStage = Globals.getWeaponMaxFrames(WeaponType.PLAYER_BULLET);
    }

}

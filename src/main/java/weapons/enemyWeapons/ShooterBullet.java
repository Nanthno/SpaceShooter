package src.main.java.weapons.enemyWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class ShooterBullet extends EnemyWeaponParent {

    private static final int baseXSpeed = 4;

    public ShooterBullet(double x, double y, double dx) {
        type = WeaponType.SHOOTER_BULLET;

        xPos = x;
        yPos = y;
        xSpeed = dx - baseXSpeed;
        ySpeed = 0;

        radius = 2;

        maxStage = Globals.getWeaponMaxFrames(type);
    }

}

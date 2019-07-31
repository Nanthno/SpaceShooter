package src.main.java.weapons.enemyWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class ShooterEmp extends EnemyWeaponParent {

    private static final int baseXSpeed = 4;

    public ShooterEmp(double x, double y, double dx) {
        type = WeaponType.ENEMY_EMP;

        xPos = x;
        yPos = y;
        xSpeed = dx - baseXSpeed;
        ySpeed = 0;

        radius = 2;

        maxStage = Globals.getWeaponMaxFrames(type);
    }

}

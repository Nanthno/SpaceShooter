package src.main.java.weapons.playerWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class PlayerBullet extends PlayerWeaponParent {

    public PlayerBullet(double x, double y, double dx, double dy) {
        type = WeaponType.PLAYER_BULLET;

        xPos = x;
        yPos = y;
        xSpeed = dx;
        ySpeed = dy;

        radius = 2;

        maxStage = Globals.getWeaponMaxFrames(WeaponType.PLAYER_BULLET);
    }

}

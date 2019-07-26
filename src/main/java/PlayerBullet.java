package src.main.java;

public class PlayerBullet extends PlayerWeaponParent {

    public PlayerBullet(double x, double y, double dx, double dy) {
        type = WeaponType.BULLET;

        xPos = x;
        yPos = y;
        xSpeed = dx;
        ySpeed = dy;

        radius = 2;

        maxStage = Globals.getWeaponMaxFrames(WeaponType.BULLET);
    }

}

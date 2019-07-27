package src.main.java;

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

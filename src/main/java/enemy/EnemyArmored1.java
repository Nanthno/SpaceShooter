package src.main.java.enemy;

import src.main.java.weapons.playerWeapons.PlayerBullet;

public class EnemyArmored1 extends EnemyShip {

    public EnemyArmored1(int x, int y, double xSpeed) {
        super(x, y, xSpeed, EnemyType.ARMORED1);
        currentFrame = 0;
    }

    @Override
    public boolean isKillable(Class weapon) {
        return !(weapon == PlayerBullet.class);
    }


}

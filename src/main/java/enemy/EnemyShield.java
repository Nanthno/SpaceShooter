package src.main.java.enemy;

import src.main.java.weapons.WeaponType;
import src.main.java.weapons.playerWeapons.PlayerWeaponParent;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EnemyShield extends EnemyShip {

    double innerRadius = 5;

    static Set<WeaponType> indestructibleWeapons;

    private static Set<WeaponType> buildIndestructibleWeaponsSet() {
        HashSet<WeaponType> output = new HashSet<>();
        output.add(WeaponType.PLAYER_MISSILE);
        return output;
    }

    public EnemyShield(int x, int y, double xSpeed) {
        super(x, y, xSpeed, EnemyType.SHIELD);
        indestructibleWeapons = buildIndestructibleWeaponsSet();
        innerRadius = radius - innerRadius;
        currentFrame = (new Random()).nextInt(maxFrame);
    }

    @Override
    public boolean isKillable(Class weapon) {
        return false;
    }

    @Override
    protected boolean checkDead() {
        return true;
    }

    @Override
    public boolean collideWithWeapon(PlayerWeaponParent weapon) {
        if (indestructibleWeapons.contains(weapon.getType())) {
            return false;
        }
        double dist = distance(weapon.getX(), weapon.getY(), weapon.getRadius(), xPos, yPos, radius);
        double radiiSum = weapon.getRadius() + radius;

        boolean insideRadius = dist < radiiSum;
        boolean outsideInnerRadius = dist > innerRadius;

        return insideRadius && outsideInnerRadius;
    }

}

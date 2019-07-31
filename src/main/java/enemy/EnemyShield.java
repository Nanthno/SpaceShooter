package src.main.java.enemy;

import src.main.java.weapons.playerWeapons.PlayerWeaponParent;
import src.main.java.weapons.WeaponType;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EnemyShield extends EnemyShip {

    double innerRadius = 5;

    static Set<WeaponType> indestructableWeapons;

    private static Set<WeaponType> buildIndestructibleWeaponsSet() {
        HashSet<WeaponType> output = new HashSet<>();
        output.add(WeaponType.PLAYER_MISSILE);
        return output;
    }

    public EnemyShield(int x, int y, double xSpeed) {
        super(x, y, xSpeed, EnemyType.SHIELD);
        indestructableWeapons = buildIndestructibleWeaponsSet();
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
        if(indestructableWeapons.contains(weapon.getType())) {
            return false;
        }
        double dist = distance(weapon.getx(), weapon.gety(), weapon.getRadius(), xPos,yPos, radius);
        double radiiSum = weapon.getRadius() + radius;

        boolean insideRadius = dist < radiiSum;
        boolean outsideInnerRadius = dist > innerRadius;

        return insideRadius && outsideInnerRadius;
    }

}

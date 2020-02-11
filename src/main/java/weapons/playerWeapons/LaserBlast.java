package src.main.java.weapons.playerWeapons;

import src.main.java.weapons.WeaponType;

public class LaserBlast extends PlayerWeaponParent {

    public LaserBlast(int x) {
        type = WeaponType.PLAYER_LASER_BLAST;
        xPos = x;

        radius = 7;
        stage = 0;
        effectiveStage = 2;
        maxStage = 7;
    }


    // returns true if the blast should be destroyed
    public boolean update(double xPos) {
        this.xPos = xPos;
        stage++;
        return stage == maxStage;
    }
}

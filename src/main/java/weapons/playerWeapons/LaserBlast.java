package src.main.java.weapons.playerWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class LaserBlast extends PlayerWeaponParent {

    int maxFrames;

    public LaserBlast(int x) {
        type = WeaponType.PLAYER_LASER_BLAST;
        xPos = x;

        radius = 7;
        stage = 0;
        effectiveStage = 2;
        maxStage = 7;

        maxFrames = Globals.getWeaponMaxFrames(WeaponType.PLAYER_LASER_BLAST);
    }


    // returns true if the blast should be destroyed
    public boolean update(double xPos) {
        this.xPos = xPos;
        stage++;
        return stage == maxStage;
    }
}

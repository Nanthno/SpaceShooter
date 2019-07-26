package src.main.java;

public class LaserBlast extends PlayerWeaponParent {


    public LaserBlast(int x) {
        type = WeaponType.LASER_BLAST;
        xPos = x;

        radius = 4;
        stage = 0;
        effectiveStage = 2;
        maxStage = 7;

        maxFrame = Globals.getWeaponMaxFrames(WeaponType.LASER_BLAST);
    }


    // returns true if the blast should be destroyed
    @Override
    public boolean update() {
        stage++;
        if (stage == maxStage) {
            return true;
        }
        return false;
    }
}

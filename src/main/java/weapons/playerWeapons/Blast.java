package src.main.java.weapons.playerWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class Blast extends PlayerWeaponParent {

    static final int startRadius = 4;
    static final int maxRadius = 128;

    static final int duration = 20;

    int maxFrames;

    int currentState = 0;

    static final int expansionRate = (maxRadius-startRadius)/duration;

    public Blast (double x, double y) {
        type = WeaponType.BLAST;

        xPos = x;
        yPos = y;

        maxFrames = Globals.getWeaponMaxFrames(WeaponType.BLAST);

        radius = startRadius;
    }

    public boolean update() {
        currentState++;
        radius = expansionRate*currentState + startRadius;
        currentFrame = (int)(currentState * maxFrames/(duration*1.0));

        return currentState > duration;
    }

    public static int getMaxRadius() {
        return maxRadius;
    }
}

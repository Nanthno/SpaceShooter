package src.main.java.weapons.playerWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class Burst extends PlayerWeaponParent {

    static final int startRadius = 4;
    static final int maxRadius = 128;

    static final int duration = 20;

    int maxFrames;

    int currentState = 0;

    static final int expansionRate = (maxRadius - startRadius) / duration;

    public Burst(double x, double y) {
        type = WeaponType.PLAYER_BURST;

        xPos = x;
        yPos = y;

        maxFrames = Globals.getWeaponMaxFrames(WeaponType.PLAYER_BURST) - 1;

        radius = startRadius;
    }

    public boolean update() {
        currentState++;
        radius = expansionRate * currentState + startRadius;
        currentFrame = (int) (currentState * maxFrames / (duration * 1.0));

        return currentState > duration;
    }

    public static int getMaxRadius() {
        return maxRadius;
    }
}

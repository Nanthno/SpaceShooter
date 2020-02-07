package src.main.java.weapons.playerWeapons;

import src.main.java.Globals;
import src.main.java.weapons.WeaponType;

public class Burst extends PlayerWeaponParent {

    private static final int startRadius = 4;
    private static final int maxRadius = 128;

    private static final int duration = 20;

    private final int maxFrames;

    private int currentState = 0;

    private static final int expansionRate = (maxRadius - startRadius) / duration;

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

package src.main.java.weapons.playerWeapons;

import java.util.Map;

public enum SpecialWeapon {

    LASER_BLAST;

    static Map<SpecialWeapon, String> weaponDescriptions = Map.of(
            LASER_BLAST, "Laser Blast: fires a laser from top to bottom of the screen destroying anything in its path"
    );

}

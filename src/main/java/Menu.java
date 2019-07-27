package src.main.java;

import src.main.java.weapons.playerWeapons.SpecialWeapon;

public class Menu {

    String[] instructions;
    double instructionDisplayTime = 1;

    SpecialWeapon[] selectedWeapons;

    public Menu() {
        instructions = makeInstructions();
        selectedWeapons = new SpecialWeapon[4];
    }


    private String[] makeInstructions() {
        return null;
    }

}

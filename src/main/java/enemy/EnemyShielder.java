package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;

public class EnemyShielder extends EnemyShip {

    public EnemyShielder(int x, int y, double xSpeed) {
        super(x, y, xSpeed, EnemyType.SHIELDER);
    }

    @Override
    public boolean update() {
        createShield();

        return super.update();
    }

    private void createShield() {
        int x = (int) xPos - Globals.getEnemyShipRadius(EnemyType.SHIELD) + radius;
        int y = (int) yPos - Globals.getEnemyShipRadius(EnemyType.SHIELD) + radius;

        Controller.addEnemy(new EnemyShield(x, y, 0));
    }

    @Override
    public boolean checkDead() {
        return xPos < -1.3 * Globals.getEnemyShipRadius(EnemyType.SHIELD);
    }


}

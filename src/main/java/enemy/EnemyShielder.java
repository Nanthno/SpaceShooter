package src.main.java.enemy;

import src.main.java.Controller;
import src.main.java.Globals;

public class EnemyShielder extends EnemyShip {

    public EnemyShielder(int x, int y, double xSpeed) {
        xPos = x;
        yPos = y;
        this.xSpeed = xSpeed;
        shipType = EnemyType.SHIELDER;
        radius = Globals.enemyShielderRadius;
    }

    private void createShield() {
        int x = (int)xPos - Globals.enemyShieldRadius + radius;
        int y = (int)yPos - Globals.enemyShieldRadius + radius;

        Controller.addEnemy(new EnemyShield(x, y, 0));
    }

    @Override
    public boolean updateShip() {
        createShield();

        return super.updateShip();
    }


}

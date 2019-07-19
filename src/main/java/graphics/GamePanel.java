package src.main.java.graphics;

import src.main.java.*;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

class GamePanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage screenshot = drawGameScreenShot();
        g.drawImage(screenshot, 0, 0, this);

    }

    protected BufferedImage drawGameScreenShot() {
        BufferedImage screenshot = ImageUtil.copyImage(GraphicsManager.background);

        // draw enemy ships
        List<EnemyShip> enemyShips = Controller.getEnemyArray();
        Graphics g = screenshot.getGraphics();
        for (EnemyShip enemy : enemyShips) {
            BufferedImage shipImage = GraphicsManager.imageNotFound;
            if (enemy.getType() == EnemyType.BASIC)
                shipImage = GraphicsManager.enemyBasicImages[enemy.getFrame()];
            else if (enemy.getType() == EnemyType.FUEL)
                shipImage = GraphicsManager.enemyFuelImages[enemy.getFrame()];
            else if (enemy.getType() == EnemyType.AGILE)
                shipImage = GraphicsManager.enemyAgileImages[enemy.getFrame()];
            else if (enemy.getType() == EnemyType.SHIELDER) {
                shipImage = GraphicsManager.enemyShielderImages[enemy.getFrame()];
            }
            else if(enemy.getType() == EnemyType.SHIELD)
                shipImage = GraphicsManager.enemyShieldImages[enemy.getFrame()];

            g.drawImage(shipImage, enemy.getx(), enemy.gety(), null);
        }

        // draw player bullets
        ArrayList<PlayerBullet> pb = Controller.getPlayerBullets();
        for (PlayerBullet b : pb) {
            g.drawImage(GraphicsManager.playerBulletImages[b.getFrame()], (int) b.getx(), (int) b.gety(), null);
        }

        // draw explosions
        ArrayList<Explosion> exp = Controller.getExplosions();
        for (int i = 0; i < exp.size(); i++) {
            Explosion e = exp.get(i);
            if (e.getExpType() == ExplosionType.SMALL) {
                g.drawImage(GraphicsManager.smallExplosionImages[e.getStage()], e.getx(), e.gety(), null);
            }
            if (e.getExpType() == ExplosionType.MEDIUM) {
                g.drawImage(GraphicsManager.mediumExplosionImages[e.getStage()], e.getx(), e.gety(), null);
            }
            if (e.getExpType() == ExplosionType.FUEL) {
                g.drawImage(GraphicsManager.fuelExplosionImages[e.getStage()], e.getx(), e.gety(), null);
            }
        }


        LaserBlast blast = Controller.getLaserBlast();
        if (blast != null)
            g.drawImage(GraphicsManager.laserBlastImages[blast.getFrame()], blast.getx(), 0, null);

        // draw player ship
        PlayerShip ship = Controller.getPlayerShip();
        g.drawImage(GraphicsManager.playerImages[ship.getFrame()], (int) ship.getx(), (int) ship.gety(), null);

        // at end
        g.dispose();

        return screenshot;
    }

}
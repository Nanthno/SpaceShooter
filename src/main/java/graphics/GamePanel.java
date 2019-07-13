package src.main.java.graphics;

import src.main.java.*;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
        ArrayList<EnemyShip> enemyShips = Controller.getEnemyArray();
        Graphics g = screenshot.getGraphics();
        for (EnemyShip enemy : enemyShips) {
            if (enemy.getType() == EnemyType.BASIC)
                g.drawImage(GraphicsManager.enemyBasicImage, enemy.getx(), enemy.gety(), null);
            else if (enemy.getType() == EnemyType.FUEL)
                g.drawImage(GraphicsManager.enemyFuelImage, enemy.getx(), enemy.gety(), null);
            else if (enemy.getType() == EnemyType.AGILE)
                g.drawImage(GraphicsManager.enemyAgileImage, enemy.getx(), enemy.gety(), null);
        }

        // draw player bullets
        ArrayList<PlayerBullet> pb = Controller.getPlayerBullets();
        for (PlayerBullet b : pb) {
            g.drawImage(GraphicsManager.playerBullet, (int) b.getx(), (int) b.gety(), null);
        }

        // draw explosions
        ArrayList<Explosion> exp = Controller.getExplosions();
        for (int i = 0; i < exp.size(); i++) {
            Explosion e = exp.get(i);
            if (e.getExpType() == 0) {
                g.drawImage(GraphicsManager.smallExplosion[e.getStage()], e.getx(), e.gety(), null);
            }
            if (e.getExpType() == 1) {
                g.drawImage(GraphicsManager.fuelExplosion[e.getStage()], e.getx(), e.gety(), null);
            }
            if (e.getExpType() == 2) {
                g.drawImage(GraphicsManager.mediumExplosion[e.getStage()], e.getx(), e.gety(), null);
            }
        }


        LaserBlast blast = Controller.getLaserBlast();
        if (blast != null)
            g.drawImage(GraphicsManager.laserBlast, blast.getx(), 0, null);

        // draw player ship
        PlayerShip ship = Controller.getPlayerShip();
        g.drawImage(GraphicsManager.playerImg, (int) ship.getx(), (int) ship.gety(), null);

        // at end
        g.dispose();

        return screenshot;
    }

}
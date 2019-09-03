package src.main.java.graphics;

import src.main.java.Controller;
import src.main.java.Explosion;
import src.main.java.PlayerShip;
import src.main.java.enemy.EnemyShip;
import src.main.java.weapons.WeaponType;
import src.main.java.weapons.enemyWeapons.EnemyWeaponParent;
import src.main.java.weapons.playerWeapons.LaserBlast;
import src.main.java.weapons.playerWeapons.PlayerWeaponParent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.List;

class GamePanel extends JPanel {


    protected BufferedImage drawGameScreenShot(int[] playerShake) throws ConcurrentModificationException {
        BufferedImage screenshot = new BufferedImage(GraphicsManager.getWidth(), GraphicsManager.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // draw enemy ships
        Graphics g = screenshot.getGraphics();
        for (EnemyShip enemy : Controller.getEnemyArray()) {
            BufferedImage shipImage = GraphicsManager.getFrame(enemy.getType(), enemy.getFrame());

            g.drawImage(shipImage, enemy.getX(), enemy.getY(), null);
        }

        // draw player fired weapons
        List<PlayerWeaponParent> playerWeapons = Controller.getPlayerFiredWeapons();
        for (PlayerWeaponParent w : playerWeapons) {
            BufferedImage img = GraphicsManager.getFrame(w.getType(), w.getFrame());

            g.drawImage(img, w.getX(), w.getY(), null);
        }

        // draw enemy fired weapons
        List<EnemyWeaponParent> enemyWeapons = Controller.getEnemyFiredWeapons();
        for (EnemyWeaponParent w : enemyWeapons) {
            BufferedImage img = GraphicsManager.getFrame(w.getType(), w.getFrame());

            g.drawImage(img, w.getX(), w.getY(), null);
        }

        // draw explosions
        List<Explosion> exp = Controller.getExplosions();
        for (int i = 0; i < exp.size(); i++) {
            Explosion e = exp.get(i);
            BufferedImage img = GraphicsManager.getFrame(e.getExpType(), e.getStage());
            int height = img.getHeight();
            int width = img.getWidth();
            BufferedImage drawnImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = drawnImg.createGraphics();
            g2d.translate((height - width) / 2, (height - width) / 2);
            int rotation = e.getRotation();
            g2d.rotate((Math.PI / 2) * rotation, img.getHeight() / 2, img.getWidth() / 2);
            g2d.drawRenderedImage(img, null);
            g2d.dispose();
            g.drawImage(drawnImg, e.getX(), e.getY(), null);
        }


        LaserBlast blast = Controller.getLaserBlast();
        if (blast != null)
            g.drawImage(GraphicsManager.getFrame(WeaponType.PLAYER_LASER_BLAST, blast.getFrame()), blast.getX(), 0, null);

        // draw player ship
        PlayerShip ship = Controller.getPlayerShip();
        g.drawImage(GraphicsManager.playerImages[ship.getFrame()], (int) ship.getX() + playerShake[0], (int) ship.getY() + playerShake[1], null);

        // at end
        g.dispose();

        return screenshot;
    }

}
package src.main.java.graphics;

import src.main.java.*;
import src.main.java.enemy.EnemyShip;
import src.main.java.enemy.EnemyType;
import src.main.java.weapons.WeaponType;
import src.main.java.weapons.enemyWeapons.EnemyWeaponParent;
import src.main.java.weapons.playerWeapons.LaserBlast;
import src.main.java.weapons.playerWeapons.PlayerWeaponParent;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class GamePanel extends JPanel {


    protected BufferedImage drawGameScreenShot(int[] playerShake) {
        BufferedImage screenshot = new BufferedImage(GraphicsManager.getWidth(), GraphicsManager.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // draw enemy ships
        Graphics g = screenshot.getGraphics();
        for (EnemyShip enemy : Controller.getEnemyArray()) {
            BufferedImage shipImage = GraphicsManager.getFrame(enemy.getType(), enemy.getFrame());

            g.drawImage(shipImage, enemy.getx(), enemy.gety(), null);
        }

        // draw player fired weapons
        List<PlayerWeaponParent> playerWeapons = Controller.getPlayerFiredWeapons();
        for (PlayerWeaponParent w : playerWeapons) {
            BufferedImage img = GraphicsManager.getFrame(w.getType(), w.getFrame());

            g.drawImage(img, (int) w.getx(), (int) w.gety(), null);
        }

        // draw enemy fired weapons
        List<EnemyWeaponParent> enemyWeapons = Controller.getEnemyFiredWeapons();
        for (EnemyWeaponParent w : enemyWeapons) {
            BufferedImage img = GraphicsManager.getFrame(w.getType(), w.getFrame());

            g.drawImage(img, (int) w.getx(), (int) w.gety(), null);
        }

        // draw explosions
        List<Explosion> exp = Controller.getExplosions();
        for (int i = 0; i < exp.size(); i++) {
            Explosion e = exp.get(i);
            BufferedImage img = GraphicsManager.getFrame(e.getExpType(), e.getStage());

            g.drawImage(img, e.getx(), e.gety(), null);
        }


        LaserBlast blast = Controller.getLaserBlast();
        if (blast != null)
            g.drawImage(GraphicsManager.getFrame(WeaponType.PLAYER_LASER_BLAST, blast.getFrame()), blast.getx(), 0, null);

        // draw player ship
        PlayerShip ship = Controller.getPlayerShip();
        g.drawImage(GraphicsManager.playerImages[ship.getFrame()], (int) ship.getx() + playerShake[0], (int) ship.gety() + playerShake[1], null);

        // at end
        g.dispose();

        return screenshot;
    }

}
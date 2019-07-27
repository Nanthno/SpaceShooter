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
            else if (enemy.getType() == EnemyType.SHIELDER)
                shipImage = GraphicsManager.enemyShielderImages[enemy.getFrame()];
            else if (enemy.getType() == EnemyType.SHIELD)
                shipImage = GraphicsManager.enemyShieldImages[enemy.getFrame()];
            else if (enemy.getType() == EnemyType.ARMORED1)
                shipImage = GraphicsManager.enemyArmored1Images[enemy.getFrame()];
            else if (enemy.getType() == EnemyType.SHOOTER)
                shipImage = GraphicsManager.enemyShooterImages[enemy.getFrame()];

            g.drawImage(shipImage, enemy.getx(), enemy.gety(), null);
        }

        // draw player fired weapons
        List<PlayerWeaponParent> playerWeapons = Controller.getPlayerFiredWeapons();
        for (PlayerWeaponParent w : playerWeapons) {
            BufferedImage img = GraphicsManager.imageNotFound;
            if (w.getType() == WeaponType.BULLET)
                img = GraphicsManager.playerBulletImages[w.getFrame()];
            else if (w.getType() == WeaponType.MISSILE)
                img = GraphicsManager.missileImages[w.getFrame()];
            else if (w.getType() == WeaponType.BLAST)
                img = GraphicsManager.blastImages[w.getFrame()];

            g.drawImage(img, (int) w.getx(), (int) w.gety(), null);
        }

        // draw enemy fired weapons
        List<EnemyWeaponParent> enemyWeapons = Controller.getEnemyFiredWeapons();
        for (EnemyWeaponParent w : enemyWeapons) {
            BufferedImage img = GraphicsManager.imageNotFound;

            if (w.getType() == WeaponType.SHOOTER_BULLET)
                img = GraphicsManager.shooterBulletImages[w.getFrame()];

            g.drawImage(img, (int) w.getx(), (int) w.gety(), null);
        }

        // draw explosions
        List<Explosion> exp = Controller.getExplosions();
        for (int i = 0; i < exp.size(); i++) {
            Explosion e = exp.get(i);
            BufferedImage img = GraphicsManager.imageNotFound;
            if (e.getExpType() == ExplosionType.SMALL)
                img = GraphicsManager.smallExplosionImages[e.getStage()];
            else if (e.getExpType() == ExplosionType.MEDIUM)
                img = GraphicsManager.mediumExplosionImages[e.getStage()];
            else if (e.getExpType() == ExplosionType.FUEL)
                img = GraphicsManager.fuelExplosionImages[e.getStage()];
            else if (e.getExpType() == ExplosionType.PROJECTILE)
                img = GraphicsManager.projectileExplosionImages[e.getStage()];

            g.drawImage(img, e.getx(), e.gety(), null);
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
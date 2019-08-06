package src.main.java.graphics;

import src.main.java.*;
import src.main.java.enemy.EnemyType;
import src.main.java.weapons.WeaponType;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsManager {


    // size of the screen
    static int GAME_WIDTH = Globals.screenWidth;
    static int GAME_HEIGHT = Globals.screenHeight;
    static int STATUS_WIDTH = 64;
    static int WIDTH = GAME_WIDTH + STATUS_WIDTH;
    static int HEIGHT = Globals.screenHeight;

    JFrame frame;
    ScreenPanel screenPanel;

    // images
    static BufferedImage background;
    static BufferedImage statusBars;
    static BufferedImage[] enemyBasicImages;
    static BufferedImage[] enemyFuelImages;
    static BufferedImage[] enemyAgileImages;
    static BufferedImage[] enemyShielderImages;
    static BufferedImage[] enemyShieldImages;
    static BufferedImage[] enemyArmored1Images;
    static BufferedImage[] enemyShooterImages;
    static BufferedImage[] enemyFighterImages;

    static BufferedImage[] playerBulletImages;
    static BufferedImage[] missileImages;
    static BufferedImage[] laserBlastImages;
    static BufferedImage[] blastImages;
    static BufferedImage[] shooterBulletImages;

    static BufferedImage[] smallExplosionImages;
    static BufferedImage[] fuelExplosionImages;
    static BufferedImage[] mediumExplosionImages;
    static BufferedImage[] projectileExplosionImages;

    static BufferedImage[] playerImages;

    static BufferedImage imageNotFound = makeImageNotFound();

    public GraphicsManager() {

        // load the images for the game
        loadImages();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Space Shooter");
        frame.setSize(WIDTH, HEIGHT);
        frame.addMouseListener(Controller.getMouseListener());

        screenPanel = new ScreenPanel();
        frame.add(screenPanel, BorderLayout.CENTER);

        // makes the frame visible
        frame.setVisible(true);
    }


    public void drawScreen() {

        screenPanel.removeAll();
        screenPanel.validate();
        screenPanel.repaint();


    }

    // loads the images for the game
    void loadImages() {
        background = ImageUtil.loadImage("images/spaceLong.png");
        statusBars = ImageUtil.loadImage("images/statusPanel.png");

        enemyBasicImages = ImageUtil.loadAnimation("images/enemyBasic");
        enemyFuelImages = ImageUtil.loadAnimation("images/enemyFuel");
        enemyAgileImages = ImageUtil.loadAnimation("images/enemyAgile");
        enemyShielderImages = ImageUtil.loadAnimation("images/enemyShielder");
        enemyShieldImages = ImageUtil.loadAnimation("images/enemyShield", 100);
        enemyArmored1Images = ImageUtil.loadAnimation("images/enemyArmored1");
        enemyShooterImages = ImageUtil.loadAnimation("images/enemyShooter");
        enemyFighterImages = ImageUtil.loadAnimation("images/enemyFighter");


        playerImages = ImageUtil.loadAnimation("images/player");

        playerBulletImages = ImageUtil.loadAnimation("images/playerBullet");
        missileImages = ImageUtil.loadAnimation("images/missile");
        laserBlastImages = ImageUtil.loadAnimation("images/laserBlast");
        blastImages = ImageUtil.loadAnimation("images/blast");
        shooterBulletImages = ImageUtil.loadAnimation("images/shooterBullet");


        smallExplosionImages = ImageUtil.loadAnimation("images/smallExplosion");
        mediumExplosionImages = ImageUtil.loadAnimation("images/mediumExplosion");
        fuelExplosionImages = ImageUtil.loadAnimation("images/fuelExplosion");
        projectileExplosionImages = ImageUtil.loadAnimation("images/projectileExplosion");

        Globals.addToExplosionMaxFrames(ExplosionType.SMALL, smallExplosionImages.length-1);
        Globals.addToExplosionMaxFrames(ExplosionType.MEDIUM, mediumExplosionImages.length-1);
        Globals.addToExplosionMaxFrames(ExplosionType.FUEL, fuelExplosionImages.length-1);
        Globals.addToExplosionMaxFrames(ExplosionType.PROJECTILE, projectileExplosionImages.length-1);

        Globals.addToEnemyShipMaxFrames(EnemyType.BASIC, enemyBasicImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.FUEL, enemyFuelImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.AGILE, enemyAgileImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.SHIELDER, enemyShielderImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.SHIELD, enemyShieldImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.ARMORED1, enemyArmored1Images.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.SHOOTER, enemyShooterImages.length);
        Globals.addToEnemyShipMaxFrames(EnemyType.PILOTED, enemyFighterImages.length);

        Globals.setPlayerMaxFrames(playerImages.length);
        Globals.addToWeaponMaxFrames(WeaponType.PLAYER_BULLET, playerBulletImages.length);
        Globals.addToWeaponMaxFrames(WeaponType.PLAYER_LASER_BLAST, laserBlastImages.length);
        Globals.addToWeaponMaxFrames(WeaponType.PLAYER_MISSILE, missileImages.length);
        Globals.addToWeaponMaxFrames(WeaponType.PLAYER_BURST, blastImages.length);
        Globals.addToWeaponMaxFrames(WeaponType.ENEMY_EMP, shooterBulletImages.length);
    }

    private static BufferedImage makeImageNotFound() {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();
        g.setColor(new Color(255, 0, 200));
        g.fillRect(0, 0, 32, 32);
        g.dispose();
        return img;
    }

    public Point getFramePosition() {
        return frame.getLocationOnScreen();
    }

    public static int getWidth() {
        return WIDTH;
    }

    static int getHeight() {
        return HEIGHT;
    }

    public void addScreenShake(int x, int y) {
       screenPanel.addScreenShake(x, y);
    }
    public void addScreenShake(int[] pos) {
        addScreenShake(pos[0], pos[1]);
    }
    public void addPlayerShake(int x, int y) {
        screenPanel.addPlayerShake(x, y);
    }
}
